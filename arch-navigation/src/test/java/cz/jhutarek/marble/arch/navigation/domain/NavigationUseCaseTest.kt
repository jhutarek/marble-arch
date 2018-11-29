package cz.jhutarek.marble.arch.navigation.domain

import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import io.mockk.mockk
import io.mockk.verify

internal class NavigateUseCaseTest : InstancePerClassStringSpec({
    val controller = mockk<NavigationController>(relaxUnitFun = true)
    val destination = 123

    val navigateTo = NavigationUseCase.Navigate(controller)

    "use case should execute navigate to on controller" {
        navigateTo(destination)

        verify { controller.navigate(destination) }
    }
})
