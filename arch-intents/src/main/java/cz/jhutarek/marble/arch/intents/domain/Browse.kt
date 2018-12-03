package cz.jhutarek.marble.arch.intents.domain

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import cz.jhutarek.marble.arch.usecase.domain.UseCase
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

sealed class IntentUseCase<in I, out O> : UseCase<I, O> {

    @Singleton
    class Browse @Inject constructor(private val context: Context) : IntentUseCase<Uri, Completable>() {
        override fun invoke(input: Uri): Completable = Completable.fromAction { context.startActivity(Intent(ACTION_VIEW, input)) }
    }
}

