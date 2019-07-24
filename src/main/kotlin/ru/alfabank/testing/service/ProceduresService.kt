package ru.alfabank.testing.service

import org.springframework.jdbc.core.simple.SimpleJdbcCall
import org.springframework.stereotype.Service
import ru.alfabank.testing.configuration.ProcedureProperties
import ru.alfabank.testing.configuration.ProcedureProperty
import ru.alfabank.testing.domain.ProcedureRequest

@Service
class ProceduresService(
        private val procedureProperties: ProcedureProperties, private val procedureNameToJdbcCall: Map<String, SimpleJdbcCall>
) {

    fun execute(procedureRequest: ProcedureRequest) {
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
