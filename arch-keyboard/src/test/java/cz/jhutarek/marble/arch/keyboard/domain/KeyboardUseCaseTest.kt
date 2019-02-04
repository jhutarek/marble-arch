package cz.jhutarek.marble.arch.keyboard.domain

import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec
import io.mockk.mockk
import io.mockk.verify

internal class KeyboardUseCaseHideKeyboardTest : InstancePerClassStringSpec({
    val controller = mockk<KeyboardController>(relaxUnitFun = true)
    val hideKeyboard = KeyboardUseCase.HideKeyboard(controller)

    "use case should delegate hide keyboard action to controller" {
        hideKeyboard(Unit)

        verify { controller.hideKeyboard() }
    }
})