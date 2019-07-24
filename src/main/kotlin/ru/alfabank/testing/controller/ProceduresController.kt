package ru.alfabank.testing.controller

import org.omg.CORBA.Object
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.alfabank.testing.domain.ProcedureRequest
import ru.alfabank.testing.service.ProceduresService

@RestController
@RequestMapping("/procedures")
class ProceduresController(val proceduresService : ProceduresService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = ["/{procedureRequest}/execute"])
    fun execute(@PathVariable procedureRequest: ProcedureRequest, @RequestParam parameters: Map<String, Object>) {
        procedureRequest.requestParameters.putAll(parameters)
        proceduresService.execute(procedureRequest)
    }
}
