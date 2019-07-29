package ru.alfabank.testing.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.alfabank.testing.domain.ProcedureRequest
import ru.alfabank.testing.service.ProceduresService
import ru.alfabank.testing.validation.ProcedureNameConstraint
import javax.validation.ConstraintViolation
import javax.validation.ConstraintViolationException

@Validated
@RestController
@RequestMapping("/procedures")
class ProceduresController(val proceduresService : ProceduresService) {

    @ResponseStatus(HttpStatus.CREATED)

    @PostMapping(value = ["/{procedureName}/execute"])
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
