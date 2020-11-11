package io.github.artemptushkin.procedures.api.domain

import io.github.artemptushkin.procedures.api.validation.ProcedureNameConstraint
import org.springframework.validation.annotation.Validated
import javax.validation.Valid

@Validated
data class ProcedureRequest(
        @Valid
        @ProcedureNameConstraint(message = "invalid procedure name")
        val name: String,
        val requestParameters: Map<String, String>
)
