package io.github.artemptushkin.procedures.api.controller

import io.github.artemptushkin.procedures.api.helper.json
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
        ).andExpect (
                jsonPath("").value("")
        )
    }
}
