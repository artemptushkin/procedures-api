package io.github.artemptushkin.procedures.api.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import java.sql.JDBCType

@ConfigurationProperties("procedures")
class ProcedureProperties {
    lateinit var properties: Map<String, ProcedureProperty>
}

class ProcedureProperty {
    lateinit var name: String
    lateinit var type: Database
    lateinit var parameters: Map<String, ParameterProperty>
}

class ParameterProperty {
    var required: Boolean = false
    lateinit var procedureKey: String
    lateinit var type: JDBCType
    var default: String? = null
}
