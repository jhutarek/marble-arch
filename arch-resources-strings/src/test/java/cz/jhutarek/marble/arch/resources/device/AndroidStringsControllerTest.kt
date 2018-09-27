package cz.jhutarek.marble.arch.resources.device

import android.content.Context
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AndroidStringsControllerTest {

    private val anyString = "any string"
    private val anyResId = 123
    private val anyContext = mock<Context>()

    private val controller = AndroidStringsController(anyContext)

    @Test
    fun `should invoke string method on context`() {
        whenever(anyContext.getString(any())).doReturn(anyString)

        controller.getString(anyResId)

        verify(anyContext).getString(anyResId)
    }

    @Test
    fun `should return string from context`() {
        whenever(anyContext.getString(anyResId)).thenReturn(anyString)

        assertThat(controller.getString(anyResId)).isEqualTo(anyString)
    }
}