package io.github.artemptushkin.procedures.api.helper

import org.json.JSONObject

fun <K, V> Map<K, V>.json(): String {
    return JSONObject(this).toString()
}