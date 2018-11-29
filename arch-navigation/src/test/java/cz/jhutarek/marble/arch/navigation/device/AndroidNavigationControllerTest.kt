package cz.jhutarek.marble.arch.navigation.device

import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec

internal class AndroidNavigationControllerTest : InstancePerClassStringSpec({
    val destination = 123

    val controller = AndroidNavigationController()

    "destination requests observable should have no default value after construction" {
        controller.observeDestinationRequests().test()
            .assertNoValues()
            .assertNotTerminated()
    }

    "destination requests observable should emit value passed to navigate" {
        val testObservable = controller.observeDestinationRequests().test()

        controller.navigate(destination)

        testObservable.assertValue(destination)
    }
})