package io.github.artemptushkin.procedures.api.validation

import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DatasourceNameValidator(private val procedureProperties: ProcedureProperties) :
    ConstraintValidator<DatasourceNameConstraint, String> {
    private var acceptNull: Boolean = false

    override fun initialize(constraintAnnotation: DatasourceNameConstraint) {
        this.acceptNull = constraintAnnotation.acceptNull
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return acceptNull
        return procedureProperties.dataSource.containsKey(value)
    }
}
