package ru.alfabank.testing.domain

data class ProcedureRequest(
        val name: String, val requestParameters: MutableMap<String, Any>
)
