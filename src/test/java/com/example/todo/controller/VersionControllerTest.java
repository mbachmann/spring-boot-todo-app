package com.example.todo.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VersionController.class)
class VersionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void index_ShouldReturnVersionViewAndContainVersionAttributes() throws Exception {

        mockMvc.perform(get("/version"))
                .andExpect(status().isOk())
                .andExpect(view().name("version-template"))
                .andExpect(model().attributeExists("projectVersion"))
                .andExpect(model().attributeExists("springBootVersion"))
                .andExpect(model().attributeExists("springDocVersion"))
                .andExpect(model().attributeExists("javaVersion"));
    }
}
