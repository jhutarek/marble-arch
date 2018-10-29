package cz.jhutarek.marble.example.settings.model

data class Settings(
    val units: Units
) {
    enum class Units {
        METRIC,
        IMPERIAL
    }
}