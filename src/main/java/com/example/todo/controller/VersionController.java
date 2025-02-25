package com.example.todo.controller;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.FileReader;

@Controller
public class VersionController {

    @GetMapping("/version")
    public String showVersionPage(org.springframework.ui.Model model) {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model pomModel = reader.read(new FileReader("pom.xml"));

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
