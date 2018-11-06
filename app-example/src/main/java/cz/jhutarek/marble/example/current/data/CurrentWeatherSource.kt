package cz.jhutarek.marble.example.current.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import cz.jhutarek.marble.arch.log.infrastructure.logE
import cz.jhutarek.marble.arch.log.infrastructure.logI
import cz.jhutarek.marble.arch.repository.data.Source
import cz.jhutarek.marble.example.current.domain.CurrentWeatherRepository
import cz.jhutarek.marble.example.current.model.CurrentWeather
import cz.jhutarek.marble.example.current.model.CurrentWeather.DescriptionCode.*
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers.io
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject
import javax.inject.Singleton

// TODO test, move JSON parsing to separate class
@Singleton
class CurrentWeatherSource @Inject constructor() : Source<CurrentWeatherRepository.Query, CurrentWeather> {

    private companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "060babdcb0097cb661c39c2c9e6c4a09"
        const val CURRENT_WEATHER_PATH = "weather"
        const val API_KEY_KEY = "APPID"
        const val CITY_KEY = "q"
        const val LANGUAGE_KEY = "lang"
        const val UNITS_KEY = "units"

        const val METRIC_UNITS = "metric"
    }

    private interface CurrentWeatherInterface {
        @GET(CURRENT_WEATHER_PATH)
        fun currentWeather(
            @Query(API_KEY_KEY) apiKey: String,
            @Query(CITY_KEY) city: String,
            @Query(LANGUAGE_KEY) language: String,
            @Query(UNITS_KEY) units: String
        ): Maybe<CurrentWeatherRemote>
    }

    @JsonClass(generateAdapter = true)
    data class CurrentWeatherRemote(
        @Json(name = "dt") val timestamp: Long? = null,
        @Json(name = "name") val location: String? = null,
        @Json(name = "main") val mainParameters: MainParameters?,
        @Json(name = "weather") val weatherDescriptions: List<WeatherDescription?>?,
        @Json(name = "wind") val wind: Wind?,
        @Json(name = "sys") val sun: Sun?
    ) {
        @JsonClass(generateAdapter = true)
        data class WeatherDescription(
            @Json(name = "id") val code: Int? = null,
            @Json(name = "main") val short: String? = null,
            @Json(name = "description") val long: String? = null
        )

        @JsonClass(generateAdapter = true)
        data class MainParameters(
            @Json(name = "temp") val temperatureCelsius: Double? = null,
            @Json(name = "pressure") val pressureMilliBar: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class Wind(
            @Json(name = "speed") val speedKmph: Double? = null,
            @Json(name = "deg") val directionDegrees: Double? = null
        )

        @JsonClass(generateAdapter = true)
        data class Sun(
            @Json(name = "sunrise") val sunriseTimestamp: Long? = null,
            @Json(name = "sunset") val sunsetTimestamp: Long? = null
        )
    }

    private val client by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CurrentWeatherInterface::class.java)
    }

    override fun request(query: CurrentWeatherRepository.Query): Maybe<CurrentWeather> =
        client.currentWeather(API_KEY, query.city, "en", METRIC_UNITS)
            .map {
                CurrentWeather(
                    timestamp = it.timestamp?.let { ZonedDateTime.ofInstant(Instant.ofEpochSecond(it), ZoneId.systemDefault()) },
                    location = it.location,
                    temperatureCelsius = it.mainParameters?.temperatureCelsius,
                    pressureMilliBar = it.mainParameters?.pressureMilliBar,
                    descriptionText = it.weatherDescriptions?.first()?.long,
                    descriptionCode = it.weatherDescriptions?.first()?.code?.toDescriptionCode() ?: UNKNOWN,
                    windSpeedKmph = it.wind?.speedKmph,
                    windDirectionDegrees = it.wind?.directionDegrees,
                    sunriseTimestamp = it.sun?.sunriseTimestamp?.let {
                        ZonedDateTime.ofInstant(
                            Instant.ofEpochSecond(it),
                            ZoneId.systemDefault()
                        )
                    },
                    sunsetTimestamp = it.sun?.sunsetTimestamp?.let {
                        ZonedDateTime.ofInstant(
                            Instant.ofEpochSecond(it),
                            ZoneId.systemDefault()
                        )
                    }
                )
            }
            .doOnSuccess { logI("Response: $it") }
            .doOnError { logE("Error: $it") }
            .doOnComplete { logI("No response") }
            .subscribeOn(io())

    private fun Int.toDescriptionCode() = when (this) {
        in 200..299 -> THUNDERSTORM
        in 300..399 -> DRIZZLE
        in 500..504 -> LIGHT_RAIN
        in 511..599 -> HEAVY_RAIN
        in 600..699 -> SNOW
        in 700..799 -> FOG
        800 -> CLEAR
        801 -> FEW_CLOUDS
        802 -> SCATTERED_CLOUDS
        in 803..804 -> OVERCAST_CLOUDS
        else -> UNKNOWN
    }
}