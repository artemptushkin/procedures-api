package io.github.artemptushkin.procedures.api.validation

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import io.github.artemptushkin.procedures.api.configuration.ProcedureProperty
import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ProcedureRequestValidator(
        private val procedureProperties: ProcedureProperties
) : ConstraintValidator<ProcedureRequestConstraint, ProcedureRequest> {

    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: ProcedureRequestConstraint) {
        acceptNull = constraintAnnotation.acceptNull
    }

    override fun isValid(procedureRequest: ProcedureRequest, context: ConstraintValidatorContext): Boolean {
        val requestParameters = procedureRequest.parameters
        val procedureProperty: ProcedureProperty = procedureProperties.procedures.getValue(procedureRequest.name)
        return procedureProperty
                .parameters
                .filter { it.value.required && requestParameters.contains(it.key) }
                .any()
    }
}
