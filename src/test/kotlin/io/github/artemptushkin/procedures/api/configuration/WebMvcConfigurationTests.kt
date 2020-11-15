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
        assertThat(procedureProperties.procedures).isNotEmpty
        
        assertThat(procedureProperties.procedures["create-cat"]).isNotNull

        assertThat(procedureProperties.procedures["create-cat"]?.parameters).isNotEmpty
        assertThat(procedureProperties.procedures["create-cat"]?.parameters!!["name"]?.type).isSameAs(JDBCType.VARCHAR)
        assertThat(procedureProperties.procedures["create-cat"]?.parameters!!["name"]?.default).isNull()
        assertThat(procedureProperties.procedures["create-cat"]?.parameters!!["name"]?.required).isFalse

        assertThat(procedureProperties.procedures["create-dog"]).isNotNull

        assertThat(procedureProperties.procedures["create-dog"]?.parameters).isNotEmpty
        assertThat(procedureProperties.procedures["create-dog"]?.parameters!!["name"]?.type).isSameAs(JDBCType.VARCHAR)
        assertThat(procedureProperties.procedures["create-dog"]?.parameters!!["name"]?.default).isNull()
        assertThat(procedureProperties.procedures["create-dog"]?.parameters!!["name"]?.required).isFalse
    }
}