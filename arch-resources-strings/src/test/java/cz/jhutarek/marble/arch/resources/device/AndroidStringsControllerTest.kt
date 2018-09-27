package cz.jhutarek.marble.arch.resources.device

import android.content.Context
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AndroidStringsControllerTest {

    private val anyString = "any string"
    private val anyResId = 123
    private val anyFormatArgs = listOf("any arg", 999)
    private val anyContext = mock<Context>()

    private val controller = AndroidStringsController(anyContext)

    @Test
    fun `should invoke string method on context with empty args`() {
        whenever(anyContext.getString(any())).doReturn(anyString)

        controller.getString(anyResId, listOf())

        verify(anyContext).getString(anyResId)
    }

    @Test
    fun `should return string from context with empty args`() {
        whenever(anyContext.getString(anyResId)).thenReturn(anyString)

        assertThat(controller.getString(anyResId, listOf())).isEqualTo(anyString)
    }

    @Test
    fun `should invoke string method on context with some args`() {
        whenever(anyContext.getString(any(), any())).thenReturn(anyString)

        controller.getString(anyResId, anyFormatArgs)

        verify(anyContext).getString(anyResId, *anyFormatArgs.toTypedArray())
    }

    @Test
    fun `should return string from context with some args`() {
        whenever(anyContext.getString(anyResId, *anyFormatArgs.toTypedArray())).thenReturn(anyString)

        assertThat(controller.getString(anyResId, anyFormatArgs)).isEqualTo(anyString)
    }
}