package com.example.todo.controller;


import com.example.todo.TodoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes= TodoApplication.class)
@WebMvcTest(VersionController.class)
class VersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index_ShouldReturnVersionViewAndContainVersionAttributes() throws Exception {

        mockMvc.perform(get("/version"))
                .andExpect(status().isOk())
                .andExpect(view().name("version-template"))
                .andExpect(model().attribute("projectVersion", "0.0.1-SNAPSHOT"))
                .andExpect(model().attribute("springBootVersion", "4.0.6"))
                .andExpect(model().attribute("springDocVersion", "3.0.3"))
                .andExpect(model().attribute("javaVersion", "25"))
                .andExpect(model().attribute("buildTime", matchesPattern("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}")));
    }
}
