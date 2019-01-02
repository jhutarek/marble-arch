package cz.jhutarek.marble.example.current.domain

import cz.jhutarek.marble.arch.repository.model.LegacyData
import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository.Query
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase.Load
import cz.jhutarek.marble.example.current.domain.CurrentWeatherUseCase.Observe
import cz.jhutarek.marble.example.current.model.CurrentWeather
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable

internal class ObserveUseCaseTest : InstancePerClassStringSpec({
    val repository = mockk<CurrentWeatherRepository>()
    val city = "any city"
    val repositoryData: LegacyData<CurrentWeatherRepository.Query, CurrentWeather> = LegacyData.Loading(Query(city))
    val expectedMappedData = LegacyData.Loading(Unit)

    val observe = Observe(repository)

    "use case should execute observe on repository" {
        every { repository.observe() } returns Observable.never()

        observe(Unit)

        verify { repository.observe() }
    }

    "use case should return mapped observable from repository" {
        every { repository.observe() } returns Observable.just(repositoryData)

        observe(Unit)
            .test()
            .assertValue(expectedMappedData)
    }
})

internal class LoadUseCaseTest : InstancePerClassStringSpec({
    val repository = mockk<CurrentWeatherRepository>(relaxUnitFun = true)
    val byCity = Load.ByCity("any city")

    val load = Load(repository)

    "use case should execute load on repository" {
        load(byCity)

        verify { repository.load(Query(byCity.city)) }
    }
})