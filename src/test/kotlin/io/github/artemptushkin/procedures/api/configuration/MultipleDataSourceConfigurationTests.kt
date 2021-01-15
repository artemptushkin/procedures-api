package io.github.artemptushkin.procedures.api.configuration

import io.github.artemptushkin.procedures.api.helper.TestConfig
import io.github.artemptushkin.procedures.api.service.ApplicationProceduresService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.context.ApplicationContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.sql.JDBCType
import javax.sql.DataSource

@ExtendWith(SpringExtension::class)
@ActiveProfiles("config")
@ContextConfiguration(classes = [MultipleDataSourceConfiguration::class, TestConfig::class], initializers = [ConfigDataApplicationContextInitializer::class])
internal class MultipleDataSourceConfigurationTests {

    @Autowired
    lateinit var procedureProperties: ProcedureProperties

    @Autowired
    lateinit var applicationContext: ApplicationContext

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
        assertThat(procedureProperties.procedures["create-dog"]?.parameters!!["name"]?.required).isTrue()
    }

    @Test
    fun itHasBeansWithNameEqualToPropertiesKey() {
        assertThat(applicationContext.getBean("cat-h2", ApplicationProceduresService::class.java)).isNotNull
        assertThat(applicationContext.getBean("dog-h2", ApplicationProceduresService::class.java)).isNotNull
    }

    @Test
    fun itHasDataSourceBeansWithNameBasedOnPropertiesKey() {
        assertThat(applicationContext.getBean("cat-h2".getDataSourceBeanName(), DataSource::class.java)).isNotNull
        assertThat(applicationContext.getBean("dog-h2".getDataSourceBeanName(), DataSource::class.java)).isNotNull

        assertThat(applicationContext.getBean("cat-h2".getJdbcTemplateBeanName(), NamedParameterJdbcTemplate::class.java)).isNotNull
        assertThat(applicationContext.getBean("dog-h2".getJdbcTemplateBeanName(), NamedParameterJdbcTemplate::class.java)).isNotNull

        assertThat(applicationContext.getBean("cat-h2".getDataSourceInitializerBeanName(), DataSourceInitializer::class.java)).isNotNull
        assertThat(applicationContext.getBean("dog-h2".getDataSourceInitializerBeanName(), DataSourceInitializer::class.java)).isNotNull
    }
}