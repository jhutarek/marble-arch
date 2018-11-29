package cz.jhutarek.marble.arch.navigation.model

data class Destination(val id: Int, val type: Type = Type.ADD_TO_TOP) {
    enum class Type { ADD_TO_TOP, POP_TO_PREVIOUS }
}