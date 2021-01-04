package io.github.artemptushkin.procedures.api.configuration

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import java.sql.JDBCType

@ConfigurationProperties("operations")
class ProcedureProperties {
    lateinit var procedures: Map<String, ProcedureProperty>
    lateinit var dataSource: Map<String, DataSourceProperties>
}

class ProcedureProperty {
    lateinit var name: String
    lateinit var sql: String
    lateinit var parameters: Map<String, ParameterProperty>

    companion object {
        fun from(name: String, sql: String, parameters: Map<String, ParameterProperty>) : ProcedureProperty {
            val property = ProcedureProperty()
            property.name = name
            property.sql = sql
            property.parameters = parameters
            return property
        }
    }
}

class ParameterProperty {
    var required: Boolean = false
    lateinit var key: String
    lateinit var type: JDBCType
    var default: String? = null

    companion object {
        fun from(required: Boolean, key: String, type: JDBCType, default: String? = null) : ParameterProperty {
            val property = ParameterProperty()
            property.required = required
            property.key = key
            property.type = type
            property.default = default
            return property
        }
    }
}
