package ru.alfabank.testing.service

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator
import ru.alfabank.testing.configuration.ProcedureProperties
import ru.alfabank.testing.domain.ProcedureRequest

@Component
class ProcedureRequestValidator(private val properties: ProcedureProperties) : Validator {

    override fun validate(target: Any, errors: Errors) {
        val procedureRequest = target as ProcedureRequest
        if (!properties.properties.containsKey(procedureRequest.name)) {
            errors.rejectValue("ad", REQUIRED)
        }
        errors.rejectValue("ad", REQUIRED)
    }

    override fun supports(clazz: Class<*>): Boolean = ProcedureRequest::class.java.isAssignableFrom(clazz)

    companion object {
        const val REQUIRED = "required"
        const val NOT_KNOWN = "required"
    }
}
