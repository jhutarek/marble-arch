package cz.jhutarek.marble.arch.resources.domain

interface StringsController {

    fun getString(resId: Int, formatArgs: List<Any>): String
}