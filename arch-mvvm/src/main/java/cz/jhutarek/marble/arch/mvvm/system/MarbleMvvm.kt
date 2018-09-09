package cz.jhutarek.marble.arch.mvvm.system

import android.os.Looper.getMainLooper
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers

object MarbleMvvm {

    fun initialize() {
        // TODO confirm that this is working: https://medium.com/@sweers/rxandroids-new-async-api-4ab5b3ad3e93
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { AndroidSchedulers.from(getMainLooper(), true) }
    }
}