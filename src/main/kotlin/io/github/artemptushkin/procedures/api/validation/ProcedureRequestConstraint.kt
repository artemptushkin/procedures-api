package io.github.artemptushkin.procedures.api.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ProcedureRequestValidator::class])
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.CLASS,
    AnnotationTarget.FILE
)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProcedureRequestConstraint(
    val message: String = "The following parameters are invalid: {parameters}",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val acceptNull: Boolean = true
)
