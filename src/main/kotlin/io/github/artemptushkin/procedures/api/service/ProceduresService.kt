package io.github.artemptushkin.procedures.api.service

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import io.github.artemptushkin.procedures.api.configuration.ProcedureProperty
import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import io.github.artemptushkin.procedures.api.validation.ProcedureRequestConstraint
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.validation.annotation.Validated

@Deprecated(message = "Class for stored procedures, todo to update for multiple datasource and the latest config")
@Validated
class ProceduresService(
        private val procedureProperties: ProcedureProperties, private val procedureNameToJdbcCall: Map<String, SimpleJdbcCall>
) {

    fun execute(@ProcedureRequestConstraint("required params don't exist at the properties") procedureRequest: ProcedureRequest) {
        val procedure: ProcedureProperty = procedureProperties.procedures.getValue(procedureRequest.name)

        val jdbcCallParameters = procedure.parameters
                .entries
                .associate {
                    val parameterKey = it.key
                    val procedureParameter = it.value
                    val parameterValue = procedureRequest.parameters.getOrDefault(parameterKey, procedureParameter.default)
                    procedureParameter.key to parameterValue
                }

        procedureNameToJdbcCall
                .getValue(procedure.name)
                .execute(jdbcCallParameters)
    }

}