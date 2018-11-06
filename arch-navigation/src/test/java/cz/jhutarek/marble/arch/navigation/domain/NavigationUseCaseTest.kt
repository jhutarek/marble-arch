package cz.jhutarek.marble.arch.navigation.domain

import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import io.mockk.mockk
import io.mockk.verify

internal class NavigateToUseCaseTest : InstancePerClassStringSpec({
    val controller = mockk<NavigationController>(relaxUnitFun = true)
    val destination = 123

    val navigateTo = NavigationUseCase.NavigateTo(controller)

    "use case should execute navigate to on controller" {
        navigateTo(destination)

        verify { controller.navigateTo(destination) }
    }
})
