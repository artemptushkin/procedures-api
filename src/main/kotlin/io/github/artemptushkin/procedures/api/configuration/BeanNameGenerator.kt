package io.github.artemptushkin.procedures.api.configuration

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.init.DataSourceInitializer
import javax.sql.DataSource

fun String.getDataSourceBeanName(): String = "${this}${DataSource::class.java.simpleName}"

fun String.getDataSourceInitializerBeanName(): String = "${this}${DataSourceInitializer::class.java.simpleName}"

fun String.getJdbcTemplateBeanName(): String = "${this}${NamedParameterJdbcTemplate::class.java.simpleName}"