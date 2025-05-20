package com.management.demo.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(properties = "spring.profiles.active=test") // Usar perfil de testes
@AutoConfigureMockMvc // Configura MockMvc automaticamente
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Para simular chamadas HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para converter Java -> JSON

    @Test
    void createUser_ShouldReturn201Created_WhenInputIsValid() throws Exception {
        // GIVEN - Preparar o UserDTO que vamos enviar
        UserDTO userDTO = new UserDTO(null, "John Doe", "john@example.com");

        // WHEN + THEN - Fazer o POST para /users e verificar a resposta
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON) // Diz que vamos enviar JSON
                        .content(objectMapper.writeValueAsString(userDTO))) // Envia o DTO como JSON
                .andExpect(status().isCreated()) // Espera HTTP 201 Created
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Resposta em JSON
                .andExpect(jsonPath("$.id").exists()) // Espera que exista um campo "id" no JSON
                .andExpect(jsonPath("$.name").value("John Doe")) // Nome correto
                .andExpect(jsonPath("$.email").value("john@example.com")) // Email correto
                .andDo(print()); // Opcional: imprime o request/response no console para debugging
    }

    @Test
    void createUser_ShouldReturn409Conflict_WhenEmailAlreadyExists() throws Exception {
        // GIVEN - Criamos um utilizador primeiro
        UserDTO userDTO = new UserDTO(null, "Jane Doe", "jane@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated()); // O primeiro POST deve criar normalmente

        // WHEN - Tentamos criar outro utilizador com o mesmo email
        UserDTO duplicateUserDTO = new UserDTO(null, "Another Jane", "jane@example.com");

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateUserDTO)))
                // THEN - Esperamos HTTP 409 Conflict
                .andExpect(status().isConflict())
                .andDo(print()); // Opcional: imprime o request/response para debugging
    }
}

