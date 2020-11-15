package io.github.artemptushkin.procedures.api.validation

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class TableNameValidator(private val procedureProperties: ProcedureProperties): ConstraintValidator<TableNameConstraint, String> {
    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: TableNameConstraint) {
        this.acceptNull = constraintAnnotation.acceptNull
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return acceptNull
        return procedureProperties.tables.containsKey(value)
    }
}
