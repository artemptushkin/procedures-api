package io.github.artemptushkin.procedures.api.controller

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
internal class ProceduresControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun itExecutesCreatingCats() {
        mockMvc.perform(
                post("/procedures/cat-h2/create-cat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """{
                                        "name": "John",
                                        "lastName": "Taylor"
                                    }
                                    """
                        )
        ).andExpect(
                status().isCreated
        )
    }
}