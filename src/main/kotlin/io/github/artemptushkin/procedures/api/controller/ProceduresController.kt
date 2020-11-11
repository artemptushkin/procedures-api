package io.github.artemptushkin.procedures.api.controller

import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import io.github.artemptushkin.procedures.api.service.ProceduresService
import io.github.artemptushkin.procedures.api.validation.ProcedureNameConstraint
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

@Validated
@RestController
@RequestMapping("/procedures")
class ProceduresController(val proceduresService : ProceduresService) {

    @ResponseStatus(HttpStatus.CREATED)

    @PostMapping(value = ["/{procedureName}:execute"])
    fun execute(@ProcedureNameConstraint("procedureName doesn't exist at the properties") @PathVariable procedureName: String,
                @RequestParam parameters: Map<String, String>) {
        val procedureRequest = ProcedureRequest(procedureName, parameters)
        proceduresService.execute(procedureRequest)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleInvalidRequest(exception: ConstraintViolationException) : ResponseEntity<String> {
        return ResponseEntity(exception.message, HttpStatus.BAD_REQUEST)
    }
}
