package cz.jhutarek.marble.arch.keyboard.system

import android.app.Activity
import android.app.Activity.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import cz.jhutarek.marble.arch.keyboard.device.AndroidKeyboardController
import cz.jhutarek.marble.arch.log.infrastructure.logD
import io.reactivex.disposables.Disposable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KeyboardActivityDelegate @Inject constructor(
    private val androidKeyboardController: AndroidKeyboardController,
    private val rootViewResId: Int
) {
    private var keyboardShowsDisposable: Disposable? = null
    private var keyboardHidesDisposable: Disposable? = null

    fun onStart(activity: Activity) {
        logD("Bind keyboard activity delegate")

        keyboardShowsDisposable = androidKeyboardController.observeKeyboardShows().subscribe {
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        keyboardHidesDisposable = androidKeyboardController.observeKeyboardHides().subscribe {
            val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.findViewById<View>(rootViewResId).windowToken, 0)
        }
    }

    fun onStop() {
        logD("Unbind keyboard activity delegate")

        keyboardShowsDisposable?.dispose()
        keyboardShowsDisposable = null

        keyboardHidesDisposable?.dispose()
        keyboardHidesDisposable = null
    }
}