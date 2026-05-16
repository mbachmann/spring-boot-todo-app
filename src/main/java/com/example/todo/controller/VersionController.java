package com.example.todo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

@Controller
public class VersionController {

    private static final Path POM_PATH = Path.of("pom.xml");
    private static final String UNKNOWN = "Unknown";

    private final ObjectProvider<BuildProperties> buildPropertiesProvider;

    public VersionController(ObjectProvider<BuildProperties> buildPropertiesProvider) {
        this.buildPropertiesProvider = buildPropertiesProvider;
    }

    @Value("${project.version:Unknown}")
    private String projectVersion;

    @Value("${java.version:Unknown}")
    private String javaVersion;

    @GetMapping("/version")
    public String showVersionPage(Model model) {
        PomMetadata pomMetadata = readPomMetadata();
        model.addAttribute("projectVersion", resolveProjectVersion(pomMetadata));
        model.addAttribute("springBootVersion", resolveSpringBootVersion(pomMetadata));
        model.addAttribute("javaVersion", resolveJavaVersion(pomMetadata));
        model.addAttribute("springDocVersion", resolveSpringDocVersion(pomMetadata));
        model.addAttribute("buildTime", resolveBuildTime(pomMetadata));
        return "version-template";
    }

    private String resolveProjectVersion(PomMetadata pomMetadata) {
        if (isResolvedValue(pomMetadata.projectVersion())) return pomMetadata.projectVersion();
        if (isResolvedValue(projectVersion)) return projectVersion;
        BuildProperties bp = buildPropertiesProvider.getIfAvailable();
        if (bp != null && isResolvedValue(bp.getVersion())) return bp.getVersion();
        String implVersion = VersionController.class.getPackage().getImplementationVersion();
        if (isResolvedValue(implVersion)) return implVersion;
        String pomPropVersion = getPomVersion();
        if (isResolvedValue(pomPropVersion)) return pomPropVersion;
        return UNKNOWN;
    }

    private String resolveSpringBootVersion(PomMetadata pomMetadata) {
        if (isResolvedValue(pomMetadata.springBootVersion())) return pomMetadata.springBootVersion();
        return defaultIfBlank(SpringBootVersion.getVersion());
    }

    private String resolveJavaVersion(PomMetadata pomMetadata) {
        if (isResolvedValue(pomMetadata.javaVersion())) return pomMetadata.javaVersion();
        if (isResolvedValue(javaVersion)) return javaVersion;
        return System.getProperty("java.version", UNKNOWN);
    }

    private String resolveSpringDocVersion(PomMetadata pomMetadata) {
        if (isResolvedValue(pomMetadata.springDocVersion())) return pomMetadata.springDocVersion();
        return UNKNOWN;
    }

    private String resolveBuildTime(PomMetadata pomMetadata) {
        String ts = pomMetadata.buildTimestamp();
        if (isResolvedValue(ts) && !ts.contains("${")) return ts;
        Instant buildInstant = resolveBuildInstant();
        if (buildInstant != null) return createBuildTimeFormatter(pomMetadata).format(buildInstant);
        return UNKNOWN;
    }

    private Instant resolveBuildInstant() {
        BuildProperties bp = buildPropertiesProvider.getIfAvailable();
        if (bp != null && bp.getTime() != null) return bp.getTime();
        try (InputStream input = VersionController.class.getResourceAsStream("/META-INF/build-info.properties")) {
            if (input == null) return null;
            Properties properties = new Properties();
            properties.load(input);
            String buildTime = properties.getProperty("build.time");
            if (!isResolvedValue(buildTime)) return null;
            return Instant.parse(buildTime);
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private DateTimeFormatter createBuildTimeFormatter(PomMetadata pomMetadata) {
        String pattern = isResolvedValue(pomMetadata.buildTimestampFormat())
                ? pomMetadata.buildTimestampFormat()
                : "yyyy-MM-dd HH:mm";
        return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
    }

    private String getPomVersion() {
        try (InputStream input = VersionController.class.getResourceAsStream(
                "/META-INF/maven/com.example/todo/pom.properties")) {
            if (input != null) {
                Properties props = new Properties();
                props.load(input);
                return props.getProperty("version", UNKNOWN);
            }
        } catch (IOException e) {
            // Fallback
        }
        return UNKNOWN;
    }

    private PomMetadata readPomMetadata() {
        if (!Files.exists(POM_PATH)) return PomMetadata.unknown();
        try (InputStream input = Files.newInputStream(POM_PATH)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setNamespaceAware(true);
            Document document = factory.newDocumentBuilder().parse(input);
            XPath xPath = XPathFactory.newInstance().newXPath();
            return new PomMetadata(
                    evaluateXPath(xPath, document, "/*[local-name()='project']/*[local-name()='version']/text()"),
                    evaluateXPath(xPath, document, "/*[local-name()='project']/*[local-name()='parent']/*[local-name()='version']/text()"),
                    evaluateXPath(xPath, document, "/*[local-name()='project']/*[local-name()='properties']/*[local-name()='java.version']/text()"),
                    evaluateXPath(xPath, document, "/*[local-name()='project']/*[local-name()='dependencies']/*[local-name()='dependency'][*[local-name()='groupId']='org.springdoc' and *[local-name()='artifactId']='springdoc-openapi-starter-webmvc-ui']/*[local-name()='version']/text()"),
                    evaluateXPath(xPath, document, "/*[local-name()='project']/*[local-name()='properties']/*[local-name()='timestamp']/text()"),
                    evaluateXPath(xPath, document, "/*[local-name()='project']/*[local-name()='properties']/*[local-name()='maven.build.timestamp.format']/text()")
            );
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException e) {
            return PomMetadata.unknown();
        }
    }

    private String evaluateXPath(XPath xPath, Document document, String expression) throws XPathExpressionException {
        String value = (String) xPath.evaluate(expression, document, XPathConstants.STRING);
        return defaultIfBlank(value);
    }

    private boolean isResolvedValue(String value) {
        return value != null && !value.isBlank() && !UNKNOWN.equals(value) && !value.startsWith("${");
    }

    private String defaultIfBlank(String value) {
        return value == null || value.isBlank() ? UNKNOWN : value;
    }

    private record PomMetadata(String projectVersion, String springBootVersion, String javaVersion,
                               String springDocVersion, String buildTimestamp, String buildTimestampFormat) {

        private static PomMetadata unknown() {
            return new PomMetadata(UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN, UNKNOWN);
        }
    }
}
