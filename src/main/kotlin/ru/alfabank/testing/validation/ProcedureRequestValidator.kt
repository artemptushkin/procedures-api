package ru.alfabank.testing.validation

import ru.alfabank.testing.domain.ProcedureRequest
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Validator

class ProcedureRequestValidator(private val validator: Validator) : ConstraintValidator<ProcedureRequestConstraint, ProcedureRequest> {

    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: ProcedureRequestConstraint?) {
        acceptNull = constraintAnnotation!!.acceptNull
    }

    override fun isValid(procedureRequest: ProcedureRequest, context: ConstraintValidatorContext?): Boolean {
        val constraintViolations = validator.validate(procedureRequest)
        return true
    }
}
