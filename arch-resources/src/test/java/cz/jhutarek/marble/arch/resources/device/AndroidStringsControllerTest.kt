package cz.jhutarek.marble.arch.resources.device

import android.content.Context
import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class AndroidStringsControllerTest : InstancePerClassStringSpec({
    val string = "any string"
    val resId = 123
    val context = mockk<Context>()

    val controller = AndroidStringsController(context)

    "controller should invoke string method on context" {
        every { context.getString(any()) } returns string

        controller.getString(resId)

        verify { context.getString(resId) }
    }

    "controller should return string from context" {
        every { context.getString(any()) } returns string

        controller.getString(resId) shouldBe string
    }
})