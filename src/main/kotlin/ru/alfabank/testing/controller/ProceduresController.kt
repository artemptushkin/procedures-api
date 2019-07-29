package ru.alfabank.testing.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import ru.alfabank.testing.configuration.ProcedureProperties
import ru.alfabank.testing.domain.ProcedureRequest
import ru.alfabank.testing.service.ProcedureRequestValidator
import ru.alfabank.testing.service.ProceduresService

@RestController
@RequestMapping("/procedures")
class ProceduresController(val proceduresService : ProceduresService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = ["/{procedureRequest}/execute"])
    fun execute(@PathVariable procedureRequest: ProcedureRequest, @RequestParam parameters: Map<String, String>) {
        procedureRequest.requestParameters.putAll(parameters)
        proceduresService.execute(procedureRequest)
    }

    @InitBinder
    fun initBinder(webDataBinder: WebDataBinder, procedureProperties: ProcedureProperties) {
        webDataBinder.addValidators(
                ProcedureRequestValidator(procedureProperties)
        )
    }
}
