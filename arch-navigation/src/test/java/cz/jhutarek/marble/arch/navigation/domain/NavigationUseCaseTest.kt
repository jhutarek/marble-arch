package cz.jhutarek.marble.arch.navigation.domain

import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable

internal class NavigateUseCaseTest : InstancePerClassStringSpec({
    val controller = mockk<NavigationController>(relaxUnitFun = true)
    val destination = 123

    val navigate = NavigationUseCase.Navigate(controller)

    "use case should execute navigate on controller" {
        navigate(destination)

        verify { controller.navigate(destination) }
    }
})

internal class ObserveUseCaseTest : InstancePerClassStringSpec({
    val controller = mockk<NavigationController>()
    val destination = 456

    val observe = NavigationUseCase.Observe(controller)

    "use case should execute observe on controller" {
        every { controller.observe() } returns Observable.never()

        observe(Unit)

        verify { controller.observe() }
    }

    "use case should destination from controller" {
        every { controller.observe() } returns Observable.just(destination)

        observe(Unit)
            .test()
            .assertValue(destination)
    }
})