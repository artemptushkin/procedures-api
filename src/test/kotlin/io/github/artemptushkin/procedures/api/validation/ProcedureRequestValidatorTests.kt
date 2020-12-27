package io.github.artemptushkin.procedures.api.validation

import io.github.artemptushkin.procedures.api.configuration.ParameterProperty
import io.github.artemptushkin.procedures.api.configuration.ProcedureProperties
import io.github.artemptushkin.procedures.api.configuration.ProcedureProperty
import io.github.artemptushkin.procedures.api.domain.ProcedureRequest
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.sql.JDBCType
import javax.validation.ConstraintValidatorContext

internal class ProcedureRequestValidatorTests {

    lateinit var procedureProperties: ProcedureProperties
    lateinit var victim: ProcedureRequestValidator

    @Test
    fun itReturnsFalseOnMissingRequiredProperty() {
        val request = prepareRequestWithoutRequired()
        val context = mock(ConstraintValidatorContext::class.java)

        procedureProperties = prepareProperties()
        victim = ProcedureRequestValidator(procedureProperties)

        assertThat(victim.isValid(request, context)).isFalse
    }

    @Test
    fun itReturnsTrueOnAllParametersExist() {
        val request = prepareRequest()
        val context = mock(ConstraintValidatorContext::class.java)

        procedureProperties = prepareProperties()
        victim = ProcedureRequestValidator(procedureProperties)

        assertThat(victim.isValid(request, context)).isTrue
    }

    @Test
    fun itReturnsTrueOnEmptyPropertiesParameters() {
        val request = prepareRequestWithoutParameter()
        val context = mock(ConstraintValidatorContext::class.java)

        procedureProperties = preparePropertiesWithEmptyParameters()
        victim = ProcedureRequestValidator(procedureProperties)

        assertThat(victim.isValid(request, context)).isTrue
    }

    @Test
    fun itAcceptsNullBasedOnAnnotation() {
        val context = mock(ConstraintValidatorContext::class.java)
        val expected = true
        val procedureRequestConstraint = prepareAnnotation(acceptNull = expected)

        procedureProperties = preparePropertiesWithEmptyParameters()
        victim = ProcedureRequestValidator(procedureProperties)
        victim.initialize(procedureRequestConstraint)

        assertThat(victim.isValid(null, context)).isEqualTo(expected)
    }

    private fun prepareAnnotation(acceptNull: Boolean): ProcedureRequestConstraint {
        val constraint = mock(ProcedureRequestConstraint::class.java)
        Mockito.`when`(constraint.acceptNull).thenReturn(acceptNull)
        return constraint
    }

    private fun prepareRequestWithoutParameter(): ProcedureRequest {
        return ProcedureRequest(
                name = "create-key",
                parameters = emptyMap()
        )
    }

    private fun prepareRequest(): ProcedureRequest {
        return ProcedureRequest(
                name = "create-key",
                parameters = mapOf(
                        "name" to "John",
                        "lastName" to "Taylor"
                )
        )
    }

    private fun prepareRequestWithoutRequired(): ProcedureRequest {
        return ProcedureRequest(
                name = "create-key",
                parameters = mapOf(
                        "name" to "John"
                )
        )
    }

    private fun preparePropertiesWithEmptyParameters(): ProcedureProperties {
        val procedures = mapOf(
                "create-key" to ProcedureProperty.from(
                        name = "create",
                        sql = "SELECT *",
                        parameters = emptyMap()
                )
        )
        val procedureProperties = ProcedureProperties()
        procedureProperties.procedures = procedures
        return procedureProperties
    }

    private fun prepareProperties(): ProcedureProperties {
        val procedures = mapOf(
                "create-key" to ProcedureProperty.from(
                        name = "create",
                        sql = "SELECT *",
                        parameters = mapOf(
                                "name" to ParameterProperty.from(
                                        required = false,
                                        key = "name",
                                        type = JDBCType.VARCHAR
                                ),
                                "lastName" to ParameterProperty.from(
                                        required = true,
                                        key = "name",
                                        type = JDBCType.VARCHAR
                                )
                        )
                )
        )
        val procedureProperties = ProcedureProperties()
        procedureProperties.procedures = procedures
        return procedureProperties
    }
}