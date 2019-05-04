package cz.jhutarek.marble.arch.keyboard.device

import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec

internal class AndroidKeyboardControllerTest : InstancePerClassStringSpec({

    val controller = AndroidKeyboardController()

    "observe shows should not have default value" {
        controller.observeKeyboardShows()
            .test()
            .assertNoValues()
            .assertNotComplete()
    }

    "observe hides should not have default value" {
        controller.observeKeyboardHides()
            .test()
            .assertNoValues()
            .assertNotComplete()
    }

    "observe shows should emit value after show keyboard" {
        val testObserver = controller.observeKeyboardShows().test()

        controller.showKeyboard()

        testObserver.assertValue(Unit)
    }

    "observe hides should emit value after hide keyboard" {
        val testObserver = controller.observeKeyboardHides().test()

        controller.hideKeyboard()

        testObserver.assertValue(Unit)
    }
})