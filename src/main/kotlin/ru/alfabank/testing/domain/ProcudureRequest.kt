package ru.alfabank.testing.domain

import org.omg.CORBA.Object

data class ProcedureRequest(
        val name: String, val requestParameters: MutableMap<String, Object>
)
