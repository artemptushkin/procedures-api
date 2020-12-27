package io.github.artemptushkin.procedures.api.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import java.sql.JDBCType

@ConfigurationProperties("operations")
class ProcedureProperties {
    lateinit var tables: Map<String, ProcedureProperty>
    lateinit var procedures: Map<String, ProcedureProperty>
    lateinit var dataSource: Map<String, DataSourceProperties>
}

class ProcedureProperty {
    lateinit var name: String
    lateinit var sql: String
    lateinit var parameters: Map<String, ParameterProperty>
}

class ParameterProperty {
    var required: Boolean = false
    lateinit var key: String
    lateinit var type: JDBCType
    var default: String? = null
}
