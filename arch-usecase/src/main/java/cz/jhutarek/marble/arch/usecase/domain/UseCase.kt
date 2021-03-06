package cz.jhutarek.marble.arch.usecase.domain

interface UseCase<in I, out O> {
    operator fun invoke(input: I): O
}

