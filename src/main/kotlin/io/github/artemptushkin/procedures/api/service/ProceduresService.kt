package io.github.artemptushkin.procedures.api.service

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import io.github.artemptushkin.procedures.api.configuration.ProcedureProperty
import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import io.github.artemptushkin.procedures.api.validation.ProcedureRequestConstraint
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
class ProceduresService(
        private val procedureProperties: ProcedureProperties, private val procedureNameToJdbcCall: Map<String, SimpleJdbcCall>
) {

    fun execute(@ProcedureRequestConstraint("required params don't exist at the properties") procedureRequest: ProcedureRequest) {
          val procedure: ProcedureProperty = procedureProperties.properties.getValue(procedureRequest.name)

        val jdbcCallParameters = procedure.parameters
                .entries
                .associate {
                    val parameterKey = it.key
                    val procedureParameter = it.value
                    val parameterValue = procedureRequest.requestParameters.getOrDefault(parameterKey, procedureParameter.default)
                    procedureParameter.procedureKey to parameterValue
                }

        procedureNameToJdbcCall
                .getValue(procedure.name)
                .execute(jdbcCallParameters)
    }

}
