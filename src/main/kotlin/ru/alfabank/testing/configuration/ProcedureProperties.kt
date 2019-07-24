package ru.alfabank.testing.configuration

import oracle.jdbc.OracleTypes
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("procedures")
class ProcedureProperties {
    lateinit var properties: Map<String, ProcedureProperty>
}

class ProcedureProperty {
    lateinit var name: String
    lateinit var parameters: Map<String, ParameterProperty>
}

class ParameterProperty {
    var required: Boolean = false
    lateinit var procedureKey: String
    lateinit var type: Type
    lateinit var default: String
}

enum class Type(val oracleType: Int) {
    VARCHAR(OracleTypes.VARCHAR), NUMBER(OracleTypes.NUMBER), INTEGER(OracleTypes.INTEGER), CHAR(OracleTypes.CHAR)
}
