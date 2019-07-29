package ru.alfabank.testing.validation

import ru.alfabank.testing.configuration.ProcedureProperties
import ru.alfabank.testing.configuration.ProcedureProperty
import ru.alfabank.testing.domain.ProcedureRequest
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Validator

class ProcedureRequestValidator(
        private val procedureProperties: ProcedureProperties
) : ConstraintValidator<ProcedureRequestConstraint, ProcedureRequest> {

    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: ProcedureRequestConstraint) {
        acceptNull = constraintAnnotation.acceptNull
    }

    override fun isValid(procedureRequest: ProcedureRequest, context: ConstraintValidatorContext): Boolean {
        val requestParameters = procedureRequest.requestParameters
        val procedureProperty: ProcedureProperty = procedureProperties.properties.getValue(procedureRequest.name)
        return procedureProperty
                .parameters
                .filter { it.value.required && requestParameters.contains(it.key) }
                .any()
    }
}
