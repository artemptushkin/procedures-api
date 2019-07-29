package ru.alfabank.testing.service

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import ru.alfabank.testing.configuration.ProcedureProperties
import ru.alfabank.testing.configuration.ProcedureProperty
import ru.alfabank.testing.domain.ProcedureRequest

@Component
class ProcedureRequestValidator(private val procedureProperties: ProcedureProperties) : Validator {

    override fun validate(target: Any, errors: Errors) {
        val procedureRequest = target as ProcedureRequest
        if (!procedureProperties.properties.containsKey(procedureRequest.name)) {
            errors.rejectValue("procedure_name", REQUIRED_FROM_PROPERTIES)
            val requestParameters = procedureRequest.requestParameters
            val procedureProperty : ProcedureProperty = procedureProperties.properties.getValue(procedureRequest.name)
            procedureProperty
                    .parameters
                    .filter { it.value.required && !requestParameters.contains(it.key) }
                    .forEach {
                        errors.rejectValue(it.key, REQUIRED_NOT_EXISTS)
                    }
        }
    }

    override fun supports(clazz: Class<*>): Boolean = ProcedureRequest::class.java.isAssignableFrom(clazz)

    companion object {
        const val REQUIRED_FROM_PROPERTIES = "required.not.from.properties"
        const val REQUIRED_NOT_EXISTS = "required.not.exists"
        const val NOT_KNOWN = "required"
    }
}
