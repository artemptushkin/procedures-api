package ru.alfabank.testing.domain

import org.springframework.validation.annotation.Validated
import ru.alfabank.testing.validation.ProcedureNameConstraint
import javax.validation.Valid

@Validated
data class ProcedureRequest(
        @Valid
        @ProcedureNameConstraint(message = "invalid procedure name")
        val name: String,
        val requestParameters: Map<String, String>
)
