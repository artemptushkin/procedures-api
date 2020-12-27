package io.github.artemptushkin.procedures.api.configuration

import com.zaxxer.hikari.HikariDataSource
import io.github.artemptushkin.procedures.api.service.ApplicationProceduresService
import org.springframework.beans.factory.support.DefaultListableBeanFactory
import org.springframework.boot.autoconfigure.AutoConfigureAfter
import org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.jdbc.config.SortedResourcesFactoryBean
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator

@Configuration(proxyBeanMethods = false)
@AutoConfigureAfter(value = [ConfigurationPropertiesAutoConfiguration::class])
@EnableConfigurationProperties(value = [ProcedureProperties::class])
class MultipleDataSourceConfiguration {

    @Bean
    fun datasourceServices(procedureProperties: ProcedureProperties, applicationContext: ApplicationContext): Map<String, ApplicationProceduresService> {
        val beanFactory = applicationContext.autowireCapableBeanFactory as DefaultListableBeanFactory
        return procedureProperties.dataSource
                .entries
                .associate {
                    val dataSource = createHikariDataSource(it.value)
                    val dataSourceInitializer = DataSourceInitializer()
                    val resourceDatabasePopulator = prepareDatabasePopulator(applicationContext, it.value)
                    val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
                    var applicationProceduresService = ApplicationProceduresService(jdbcTemplate, procedureProperties = procedureProperties)

                    dataSourceInitializer.setDataSource(dataSource)
                    dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator)

                    beanFactory.initializeBean(dataSource, it.key.getDataSourceBeanName())
                    beanFactory.initializeBean(dataSourceInitializer, it.key.getDataSourceInitializerBeanName())
                    beanFactory.initializeBean(jdbcTemplate, it.key.getJdbcTemplateBeanName())

                    applicationProceduresService = beanFactory.initializeBean(applicationProceduresService, it.key) as ApplicationProceduresService
                    beanFactory.registerSingleton(it.key, applicationProceduresService)
                    it.key to applicationProceduresService
                }
    }

    private fun prepareDatabasePopulator(applicationContext: ApplicationContext, dataSourceProperties: DataSourceProperties): ResourceDatabasePopulator {
        val resourceDatabasePopulator = ResourceDatabasePopulator()
        dataSourceProperties.schema?.forEach { location ->
            doGetResources(applicationContext, location).forEach { resource -> resourceDatabasePopulator.addScript(resource) }
        }
        return resourceDatabasePopulator
    }

    /**
     * Copy-pasted from the @see DataSourceInitializer #doGetResources
     */
    private fun doGetResources(applicationContext: ApplicationContext, location: String): Array<Resource> {
        return try {
            val factory = SortedResourcesFactoryBean(applicationContext, listOf(location))
            factory.afterPropertiesSet()
            factory.getObject()
        } catch (ex: Exception) {
            throw IllegalStateException("Unable to load resources from $location", ex)
        }
    }

    private fun createHikariDataSource(dataSourceProperties: DataSourceProperties) =
            dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
}
