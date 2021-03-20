package io.github.artemptushkin.procedures.api.controller

import io.github.artemptushkin.procedures.api.configuration.getJdbcTemplateBeanName
import io.github.artemptushkin.procedures.api.helper.json
import org.assertj.core.api.AssertionsForClassTypes
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension::class)
internal class ProceduresControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Test
    fun itExecutesCreatingCats() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/procedures/cat/create-cat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    mapOf(
                        "name" to "John",
                        "lastName" to "Taylor"
                    ).json()
                )
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        )

        val cat = getJdbcTemplateByKey("cat")
            .queryForObject(
                "select id, name, last_name from cat where name = :name",
                MapSqlParameterSource(
                    mapOf("name" to "John")
                ),
                { rs, _ ->
                    Cat(rs.getLong("ID"), rs.getString("name"), rs.getString("last_name"))
                }
            )

        AssertionsForClassTypes.assertThat(cat).isNotNull
        AssertionsForClassTypes.assertThat(cat!!.name).isEqualTo("John")
        AssertionsForClassTypes.assertThat(cat.lastName).isEqualTo("Taylor")
    }

    @Test
    fun itExecutesCreatingDogs() {
        mockMvc.perform(
            MockMvcRequestBuilders.post("/procedures/dog/create-dog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    mapOf(
                        "name" to "Tomas",
                        "lastName" to "The Great"
                    ).json()
                )
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        )

        val dog = getJdbcTemplateByKey("dog")
            .queryForObject(
                "select id, name, last_name from cat where name = :name",
                MapSqlParameterSource(
                    mapOf("name" to "John")
                ),
                { rs, _ ->
                    Dog(rs.getLong("ID"), rs.getString("name"), rs.getString("last_name"))
                }
            )

        AssertionsForClassTypes.assertThat(dog).isNotNull
        AssertionsForClassTypes.assertThat(dog!!.name).isEqualTo("John")
        AssertionsForClassTypes.assertThat(dog.lastName).isEqualTo("Taylor")
    }

    class Cat(val id: Long, val name: String, val lastName: String?)
    class Dog(val id: Long, val name: String, val lastName: String?)

    private fun getJdbcTemplateByKey(key: String): NamedParameterJdbcTemplate {
        return applicationContext.getBean(key.getJdbcTemplateBeanName(), NamedParameterJdbcTemplate::class.java)
    }
}