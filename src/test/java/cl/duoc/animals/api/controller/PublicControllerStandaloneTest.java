package cl.duoc.animals.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// AJUSTA: importa tu PublicController real y la ruta real
class PublicControllerStandaloneTest {

    private MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(new PublicController()).build();
    }

    @Test
    void health_ok() throws Exception {
        // AJUSTA: cambia la URL y el body esperado al que tengas en tu controlador
        mvc.perform(get("/api/public/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

    @Test
    void landing_ok() throws Exception {
        mvc.perform(get("/api/public/landing"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to United for Animals API"));
    }

}
