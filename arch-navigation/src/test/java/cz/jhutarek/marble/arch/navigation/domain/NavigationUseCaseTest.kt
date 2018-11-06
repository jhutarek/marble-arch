package cz.jhutarek.marble.arch.navigation.domain

import cz.jhutarek.marble.arch.navigation.domain.NavigationUseCase.Observe
import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable

internal class ObserveUseCaseTest : InstancePerClassStringSpec({
    val controller = mockk<NavigationController>()
    val destination = 123

    val observe = Observe(controller)

    "use case should execute observe on controller" {
        every { controller.observe() } returns Observable.never()

        observe(Unit)

        verify { controller.observe() }
    }

    "use case should return observable from controller" {
        every { controller.observe() } returns Observable.just(destination)

        observe(Unit)
            .test()
            .assertValue(destination)
    }
})

internal class NavigateToUseCaseTest : InstancePerClassStringSpec({
    val controller = mockk<NavigationController>(relaxUnitFun = true)
    val destination = 123

    val navigateTo = NavigationUseCase.NavigateTo(controller)

    "use case should execute navigate to on controller" {
        navigateTo(destination)

        verify { controller.navigateTo(destination) }
    }
})
