package io.github.artemptushkin.procedures.api.validation

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class ProcedureNameValidator(private val procedureProperties: ProcedureProperties)
    : ConstraintValidator<ProcedureNameConstraint, String> {

    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: ProcedureNameConstraint) {
        acceptNull = constraintAnnotation.acceptNull
    }

    override fun isValid(procedureName: String?, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        if (procedureName == null) return acceptNull
        return procedureProperties.procedures.containsKey(procedureName)
    }
}
