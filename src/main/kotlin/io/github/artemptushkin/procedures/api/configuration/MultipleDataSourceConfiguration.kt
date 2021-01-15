package io.github.artemptushkin.procedures.api.configuration

import com.zaxxer.hikari.HikariDataSource
import io.github.artemptushkin.procedures.api.service.ApplicationProceduresService
import org.springframework.beans.factory.support.DefaultListableBeanFactory
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

@Configuration
@EnableConfigurationProperties(value = [ProcedureProperties::class])
class MultipleDataSourceConfiguration {

    @Bean
    fun datasourceServices(
        procedureProperties: ProcedureProperties,
        applicationContext: ApplicationContext
    ): Map<String, ApplicationProceduresService> {
        val beanFactory = applicationContext.autowireCapableBeanFactory as DefaultListableBeanFactory
        procedureProperties.dataSource
            .entries
            .forEach {
                val dataSource = createHikariDataSource(it.value)
                val dataSourceInitializer = DataSourceInitializer()
                val resourceDatabasePopulator = prepareDatabasePopulator(applicationContext, it.value)
                val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
                val applicationProceduresService = ApplicationProceduresService(jdbcTemplate, procedureProperties)

                dataSourceInitializer.setDataSource(dataSource)
                dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator)

                initAndRegisterBean(dataSource, it.key.getDataSourceBeanName(), beanFactory)
                initAndRegisterBean(dataSourceInitializer, it.key.getDataSourceInitializerBeanName(), beanFactory)
                initAndRegisterBean(jdbcTemplate, it.key.getJdbcTemplateBeanName(), beanFactory)
                initAndRegisterBean(applicationProceduresService, it.key, beanFactory)

                it.key to applicationProceduresService
            }
        /* In order to get beans and not these objects of type ApplicationProceduresService we have to access it from the application context*/
        return applicationContext.getBeansOfType(ApplicationProceduresService::class.java)
    }

    private fun initAndRegisterBean(bean: Any, name: String, beanFactory: DefaultListableBeanFactory) {
        val initializedBean = beanFactory.initializeBean(bean, name)
        beanFactory.registerSingleton(name, initializedBean)
    }

    private fun prepareDatabasePopulator(
        applicationContext: ApplicationContext,
        dataSourceProperties: DataSourceProperties
    ): ResourceDatabasePopulator {
        val resourceDatabasePopulator = ResourceDatabasePopulator()
        dataSourceProperties.schema?.forEach { location ->
            doGetResources(applicationContext, location).forEach { resource ->
                resourceDatabasePopulator.addScript(
                    resource
                )
            }
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
