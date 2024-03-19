package io.github.artemptushkin.procedures.api.validation

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ProcedureNameValidator(private val procedureProperties: ProcedureProperties) :
    ConstraintValidator<ProcedureNameConstraint, String> {

    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: ProcedureNameConstraint) {
        acceptNull = constraintAnnotation.acceptNull
    }

    override fun isValid(procedureName: String?, context: ConstraintValidatorContext): Boolean {
        if (procedureName == null) return acceptNull
        return procedureProperties.procedures.containsKey(procedureName)
    }
}
