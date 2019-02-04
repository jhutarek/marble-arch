package cz.jhutarek.marble.arch.keyboard.device

import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec

internal class AndroidKeyboardControllerTest : InstancePerClassStringSpec({

    val controller = AndroidKeyboardController()

    "observe hides should not have default value" {
        controller.observeKeyboardHides()
            .test()
            .assertNoValues()
            .assertNotComplete()
    }

    "observe hides should emit value after hide keyboard" {
        val testObserver = controller.observeKeyboardHides().test()

        controller.hideKeyboard()

        testObserver.assertValue(Unit)
    }
})