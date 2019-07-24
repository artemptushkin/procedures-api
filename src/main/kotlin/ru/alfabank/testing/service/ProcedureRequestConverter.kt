package ru.alfabank.testing.service

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import ru.alfabank.testing.domain.ProcedureRequest

@Component
class ProcedureRequestConverter : Converter<String, ProcedureRequest> {

    override fun convert(source: String): ProcedureRequest {
        return ProcedureRequest(source, mutableMapOf())
    }
}
