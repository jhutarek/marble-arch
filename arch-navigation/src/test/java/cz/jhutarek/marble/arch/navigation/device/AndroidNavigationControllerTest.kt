package cz.jhutarek.marble.arch.navigation.device

import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec

internal class AndroidNavigationControllerTest : InstancePerClassStringSpec({
    val destination = 123

    val controller = AndroidNavigationController()

    "observable should have no default value after construction" {
        controller.observe().test()
            .assertNoValues()
            .assertNotTerminated()
    }

    "should emit value passed to navigate to" {
        val testObservable = controller.observe().test()

        controller.navigateTo(destination)

        testObservable.assertValue(destination)
    }
})