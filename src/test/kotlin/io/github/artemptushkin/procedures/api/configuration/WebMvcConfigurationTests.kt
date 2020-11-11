package io.github.artemptushkin.procedures.api.configuration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.JDBCType

@ExtendWith(SpringExtension::class)
@ActiveProfiles("test-properties")
@ContextConfiguration(classes = [WebMvcConfiguration::class], initializers = [ConfigFileApplicationContextInitializer::class])
internal class WebMvcConfigurationTests {

    @Autowired
    lateinit var procedureProperties: ProcedureProperties

    @Test
    fun itInitializesContextWithProperties() {
        assertThat(procedureProperties.properties).isNotEmpty
        
        assertThat(procedureProperties.properties["createCats"]).isNotNull
        assertThat(procedureProperties.properties["createCats"]?.type).isSameAs(Database.MySQL)

        assertThat(procedureProperties.properties["createCats"]?.parameters).isNotEmpty
        assertThat(procedureProperties.properties["createCats"]?.parameters!!["name"]?.type).isSameAs(JDBCType.VARCHAR)
        assertThat(procedureProperties.properties["createCats"]?.parameters!!["name"]?.procedureKey).isEqualTo("name")
        assertThat(procedureProperties.properties["createCats"]?.parameters!!["name"]?.default).isNull()
        assertThat(procedureProperties.properties["createCats"]?.parameters!!["name"]?.required).isFalse

        assertThat(procedureProperties.properties["createDogs"]).isNotNull
        assertThat(procedureProperties.properties["createDogs"]?.type).isSameAs(Database.PostgreSQL)
        
        assertThat(procedureProperties.properties["createDogs"]?.parameters).isNotEmpty
        assertThat(procedureProperties.properties["createDogs"]?.parameters!!["name"]?.type).isSameAs(JDBCType.VARCHAR)
        assertThat(procedureProperties.properties["createDogs"]?.parameters!!["name"]?.procedureKey).isEqualTo("name")
        assertThat(procedureProperties.properties["createDogs"]?.parameters!!["name"]?.default).isNull()
        assertThat(procedureProperties.properties["createDogs"]?.parameters!!["name"]?.required).isFalse
    }
}