package io.github.artemptushkin.procedures.api.controller

import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import io.github.artemptushkin.procedures.api.service.ApplicationProceduresService
import io.github.artemptushkin.procedures.api.validation.DatasourceNameConstraint
import io.github.artemptushkin.procedures.api.validation.ProcedureNameConstraint
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

@Validated
@RestController
@RequestMapping("/procedures")
class ProceduresController(private val datasourceServices: Map<String, ApplicationProceduresService>) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{datasource}/{procedureName}")
    fun create(@DatasourceNameConstraint @PathVariable datasource: String,
               @ProcedureNameConstraint @PathVariable procedureName: String, @RequestBody requestBody: Map<String, Any>) {
        datasourceServices
                .getValue(datasource)
                .update(ProcedureRequest(procedureName, requestBody))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleInvalidRequest(exception: ConstraintViolationException): ResponseEntity<String> {
        return ResponseEntity(exception.message, HttpStatus.BAD_REQUEST)
    }
}