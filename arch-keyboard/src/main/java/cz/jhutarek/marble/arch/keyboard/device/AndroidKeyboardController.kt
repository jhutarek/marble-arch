package cz.jhutarek.marble.arch.keyboard.device

import com.jakewharton.rxrelay2.PublishRelay
import cz.jhutarek.marble.arch.keyboard.domain.KeyboardController
import cz.jhutarek.marble.arch.log.infrastructure.logI
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidKeyboardController @Inject constructor() : KeyboardController {

    private val keyboardShowsRelay = PublishRelay.create<Unit>()
    private val keyboardHidesRelay = PublishRelay.create<Unit>()

    fun observeKeyboardShows(): Observable<Unit> = keyboardShowsRelay.hide()
    fun observeKeyboardHides(): Observable<Unit> = keyboardHidesRelay.hide()

    override fun showKeyboard() {
        logI("Show keyboard")

        keyboardShowsRelay.accept(Unit)
    }

    override fun hideKeyboard() {
        logI("Hide keyboard")

        keyboardHidesRelay.accept(Unit)
    }
}