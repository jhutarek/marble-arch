package cz.jhutarek.marble.arch.resources.domain

import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

internal class StringsUseCaseTest : InstancePerClassStringSpec({
    val string = "any string"
    val resId = 123
    val controller = mockk<StringsController> {
        every { getString(any()) } returns string
    }

    val getString = StringsUseCase.GetString(controller)

    "use case should invoke strings controller" {
        getString(resId)

        verify { controller.getString(123) }
    }

    "use case should return string from controller" {
        getString(resId) shouldBe string
    }
})