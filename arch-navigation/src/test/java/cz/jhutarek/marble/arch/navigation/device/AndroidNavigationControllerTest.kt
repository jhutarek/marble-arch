package cz.jhutarek.marble.arch.navigation.device

import cz.jhutarek.marble.arch.navigation.model.Destination
import cz.jhutarek.marble.arch.navigation.model.Destination.Type.POP_TO_PREVIOUS_INSTANCE
import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec

internal class AndroidNavigationControllerTest : InstancePerClassStringSpec({
    val destinationId = 123
    val destination = Destination(456, POP_TO_PREVIOUS_INSTANCE)

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

    "observe observable should have no default value after construction" {
        controller.observe().test()
            .assertNoValues()
            .assertNotTerminated()
    }

    "observe observable should emit value passed to notify navigation executed" {
        val testObservable = controller.observe().test()

        controller.notifyNavigationExecuted(destinationId)

        testObservable.assertValue(destinationId)
    }
})