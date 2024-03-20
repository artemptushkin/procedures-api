package io.github.artemptushkin.procedures.api.configuration

import org.springframework.boot.autoconfigure.sql.init.SqlDataSourceScriptDatabaseInitializer
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import javax.sql.DataSource

fun String.getDataSourceBeanName(): String = "${this}${DataSource::class.java.simpleName}"

fun String.getDataSourceInitializerBeanName(): String = "${this}${DataSourceInitializer::class.java.simpleName}"

fun String.getJdbcTemplateBeanName(): String = "${this}${NamedParameterJdbcTemplate::class.java.simpleName}"

fun String.getSqlInitializerBeanName(): String = "${this}${SqlDataSourceScriptDatabaseInitializer::class.java.simpleName}"