package cz.jhutarek.marble.arch.navigation.model

data class Destination(val id: Int, val type: Type = Type.PUSH_ON_TOP) {
    enum class Type {
        PUSH_ON_TOP,
        POP_TO_PREVIOUS_INSTANCE,
        POP_ALL_THEN_PUSH
    }
}