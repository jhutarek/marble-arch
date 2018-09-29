package cz.jhutarek.marble.example.current.model

import org.threeten.bp.ZonedDateTime

data class CurrentWeather(
        val timestamp: ZonedDateTime?,
        val location: String?,
        val temperatureCelsius: Double?,
        val pressureMilliBar: Double?,
        val descriptionText: String?,
        val windSpeedKmph: Double?,
        val windDirectionDegrees: Double?,
        val sunriseTimestamp: ZonedDateTime?,
        val sunsetTimestamp: ZonedDateTime?
)