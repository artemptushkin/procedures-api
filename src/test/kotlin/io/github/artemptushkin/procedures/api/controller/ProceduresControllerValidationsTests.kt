package io.github.artemptushkin.procedures.api.controller

import io.github.artemptushkin.procedures.api.helper.json
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
internal class ProceduresControllerValidationsTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun itReturnsBadRequestOnMissingRequiredValue() {
        mockMvc.perform(
            post("/procedures/cat-h2/create-dog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    mapOf(
                        "lastName" to "The Great"
                    ).json()
                )
        ).andExpect(
            status().isBadRequest
        ).andExpect(
            header().string("Content-Type", "application/json")
        ).andExpect(
            jsonPath("$.message", Matchers.containsString("The following parameters are invalid: [name]"))
        )
    }

    @Test
    fun itReturnsBadRequestOnInvalidDataSourceName() {
        mockMvc.perform(
            post("/procedures/invalid/create-dog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    mapOf(
                        "name" to "Piotr",
                        "lastName" to "The Great"
                    ).json()
                )
        ).andExpect(
            status().isBadRequest
        ).andExpect(
            header().string("Content-Type", "application/json")
        ).andExpect(
            jsonPath("$.message", Matchers.containsString("DataSource name is unexpected"))
        )
    }

    @Test
    fun itReturnsBadRequestOnInvalidProcedureName() {
        mockMvc.perform(
            post("/procedures/cat-h2/invalid-procedure")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    mapOf(
                        "name" to "Piotr",
                        "lastName" to "The Great"
                    ).json()
                )
        ).andExpect(
            status().isBadRequest
        ).andExpect(
            header().string("Content-Type", "application/json")
        ).andExpect(
            jsonPath("$.message", Matchers.containsString("Procedure name is unexpected"))
        )
    }
}
