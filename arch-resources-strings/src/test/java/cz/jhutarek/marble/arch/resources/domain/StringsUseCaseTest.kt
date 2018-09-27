package cz.jhutarek.marble.arch.resources.domain

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class StringsUseCaseTest {

    @Nested
    inner class GetStringUseCase {
        private val anyString = "any string"
        private val anyResId = 123
        private val anyController = mock<StringsController> {
            on { getString(any()) } doReturn anyString
        }
        private val getString = StringsUseCase.GetString(anyController)

        @Test
        fun `should invoke controller`() {
            getString(anyResId)

            verify(anyController).getString(anyResId)
        }

        @Test
        fun `should return string from controller`() {
            assertThat(getString(anyResId)).isEqualTo(anyString)
        }
    }
}