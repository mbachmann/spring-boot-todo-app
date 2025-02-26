package com.example.todo.controller;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;

@Controller
public class VersionController {

    @GetMapping("/version")
    public String showVersionPage(org.springframework.ui.Model model) {
        try {

            MavenXpp3Reader mavenXpp3Reader = new MavenXpp3Reader();
            Model pomModel;
            if ((new File("pom.xml")).exists()) {
                pomModel = mavenXpp3Reader.read(new FileReader("pom.xml"));
            }
            else {
                // Packaged artifacts contain a META- INF/maven/${groupId}/${artifactId}/pom.properties
                pomModel = mavenXpp3Reader.read(new
                        InputStreamReader(VersionController.class.getResourceAsStream(
                        "/META-INF/maven/com.example/todo/pom.xml")));
            }
            model.addAttribute("projectVersion", pomModel.getVersion());
            model.addAttribute("springBootVersion", pomModel.getParent().getVersion());
            model.addAttribute("javaVersion", pomModel.getProperties().getProperty("java.version"));
            model.addAttribute("springDocVersion", pomModel.getDependencies().stream()
                    .filter(dep -> "springdoc-openapi-starter-webmvc-ui".equals(dep.getArtifactId()))
                    .findFirst()
                    .map(Dependency::getVersion)
                    .orElse("Unknown"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "version-template";
    }
}
