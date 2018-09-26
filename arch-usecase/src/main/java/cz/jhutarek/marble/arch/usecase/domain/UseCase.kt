package cz.jhutarek.marble.arch.usecase.domain

interface UseCase<in I, out O> {
    fun execute(input: I): O
}

