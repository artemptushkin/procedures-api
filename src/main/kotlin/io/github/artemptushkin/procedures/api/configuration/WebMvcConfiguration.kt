package io.github.artemptushkin.procedures.api.configuration

import com.zaxxer.hikari.HikariDataSource
import io.github.artemptushkin.procedures.api.service.ApplicationProceduresService
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(value = [ProcedureProperties::class])
class WebMvcConfiguration : WebMvcConfigurer {

    @Deprecated(message = "todo update for multiple datasources")
    fun procedureNameToJdbcCall(dataSourceProperties: DataSourceProperties,
                                jdbcTemplate: JdbcTemplate,
                                procedureProperties: ProcedureProperties): Map<String, SimpleJdbcCall> {
        return procedureProperties.procedures
                .entries
                .associate {
                    val procedure = it.value
                    val jdbcCall = SimpleJdbcCall(jdbcTemplate)
                            .withProcedureName(procedure.name)
                            .withCatalogName(dataSourceProperties.username)
                            .withoutProcedureColumnMetaDataAccess()
                    procedure.parameters
                            .forEach { (_, parameter) ->
                                jdbcCall.addDeclaredParameter(SqlParameter(parameter.key, parameter.type.vendorTypeNumber))
                            }
                    procedure.name to jdbcCall
                }
    }

    @Bean
    fun datasourceServices(procedureProperties: ProcedureProperties): Map<String, ApplicationProceduresService> {
        return procedureProperties.dataSource
                .entries
                .associate {
                    val dataSourceProperties = it.value
                    val hikariDataSource = createHikariDataSource(dataSourceProperties)
                    val jdbcTemplate = NamedParameterJdbcTemplate(hikariDataSource)
                    it.key to ApplicationProceduresService(jdbcTemplate, procedureProperties)
                }
    }

    private fun createHikariDataSource(dataSourceProperties: DataSourceProperties) =
            dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
}
