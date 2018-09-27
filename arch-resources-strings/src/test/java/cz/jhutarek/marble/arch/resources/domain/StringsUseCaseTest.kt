package cz.jhutarek.marble.arch.resources.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import cz.jhutarek.marble.arch.resources.domain.StringsUseCase.GetString.Params
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StringsUseCaseTest {

    @Nested
    inner class GetStringUseCase {
        private val anyString = "any string"
        private val anyResId = 123
        private val anyFormatArgs = listOf("any arg", 999)
        private val anyController = mock<StringsController> {
            on { getString(any(), any()) } doReturn anyString
        }
        private val getString = StringsUseCase.GetString(anyController)

        @Test
        fun `should invoke controller with default method`() {
            getString(Params(anyResId, anyFormatArgs))

            verify(anyController).getString(anyResId, anyFormatArgs)
        }

        @Test
        fun `should return string from controller with default method`() {
            assertThat(getString(Params(anyResId, anyFormatArgs))).isEqualTo(anyString)
        }

        @Test
        fun `should invoke controller with simplified method`() {
            getString(anyResId)

            verify(anyController).getString(anyResId, listOf())
        }

        @Test
        fun `should return string from controller with simplified method`() {
            assertThat(getString(anyResId)).isEqualTo(anyString)
        }

        @Test
        fun `should invoke controller with varargs method`() {
            getString(anyResId, *anyFormatArgs.toTypedArray())

            verify(anyController).getString(anyResId, anyFormatArgs)
        }

        @Test
        fun `should return string from controller with varargs method`() {
            assertThat(getString(anyResId, *anyFormatArgs.toTypedArray())).isEqualTo(anyString)
        }
    }
}