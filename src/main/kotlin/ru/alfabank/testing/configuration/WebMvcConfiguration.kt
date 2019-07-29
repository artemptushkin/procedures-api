package ru.alfabank.testing.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableConfigurationProperties(value = [ProcedureProperties::class])
class WebMvcConfiguration : WebMvcConfigurer {

    @Bean
    fun procedureNameToJdbcCall(dataSourceProperties: DataSourceProperties,
                                jdbcTemplate: JdbcTemplate,
                                procedureProperties: ProcedureProperties): Map<String, SimpleJdbcCall> {
        return procedureProperties.properties
                .entries
                .associate {
                    val procedure = it.value
                    val jdbcCall = SimpleJdbcCall(jdbcTemplate)
                            .withProcedureName(procedure.name)
                            .withCatalogName(dataSourceProperties.username)
                            .withoutProcedureColumnMetaDataAccess()
                    procedure.parameters
                            .forEach { (_, parameter) ->
                                jdbcCall.addDeclaredParameter(SqlParameter(parameter.procedureKey, parameter.type.oracleType)) }

                    procedure.name to jdbcCall
                }
    }
}
