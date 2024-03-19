package io.github.artemptushkin.procedures.api.validation

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import io.github.artemptushkin.procedures.api.configuration.ProcedureProperty
import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl


class ProcedureRequestValidator(
    private val procedureProperties: ProcedureProperties
) : ConstraintValidator<ProcedureRequestConstraint, ProcedureRequest> {

    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: ProcedureRequestConstraint) {
        acceptNull = constraintAnnotation.acceptNull
    }

    override fun isValid(procedureRequest: ProcedureRequest?, context: ConstraintValidatorContext): Boolean {
        if (procedureRequest == null) return acceptNull

        val requestParameters = procedureRequest.parameters
        val procedureProperty: ProcedureProperty = procedureProperties.procedures.getValue(procedureRequest.name)
        val missedRequiredParameters = procedureProperty
            .parameters
            .filter {
                it.value.required && !requestParameters.contains(it.key)
            }

        if (missedRequiredParameters.isNotEmpty()) {
            (context as ConstraintValidatorContextImpl)
                .addMessageParameter("parameters", missedRequiredParameters.map { it.key }.toString())
        }
        return missedRequiredParameters.isEmpty()
    }
}
