package io.github.artemptushkin.procedures.api.service

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import io.github.artemptushkin.procedures.api.configuration.ProcedureProperty
import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import io.github.artemptushkin.procedures.api.validation.ProcedureRequestConstraint
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.core.SqlParameterValue
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.validation.annotation.Validated

@Validated
class ApplicationProceduresService(private val jdbcTemplate: NamedParameterJdbcTemplate,
                                   private val procedureProperties: ProcedureProperties) {

    fun update(@ProcedureRequestConstraint procedureRequest: ProcedureRequest) {
        val procedure: ProcedureProperty = procedureProperties.procedures.getValue(procedureRequest.name)

        val jdbcCallParameters = procedure.parameters
                .entries
                .associate {
                    val parameterKey = it.key
                    val procedureParameter = it.value
                    val parameterValue = procedureRequest.parameters.getOrDefault(parameterKey, procedureParameter.default)
                    val sqlParameter = SqlParameter(procedureParameter.key, procedureParameter.type.vendorTypeNumber)
                    procedureParameter.key to SqlParameterValue(sqlParameter, parameterValue)
                }

        jdbcTemplate.update(procedure.sql, MapSqlParameterSource(jdbcCallParameters))
    }
}
