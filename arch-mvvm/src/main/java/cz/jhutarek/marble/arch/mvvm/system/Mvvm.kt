package cz.jhutarek.marble.arch.mvvm.system

import android.os.Looper.getMainLooper
import cz.jhutarek.marble.arch.log.infrastructure.logD
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers

object Mvvm {

    fun initialize() {
        // TODO confirm that this is working: https://medium.com/@sweers/rxandroids-new-async-api-4ab5b3ad3e93
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { AndroidSchedulers.from(getMainLooper(), true) }

        logD("Initialized MVVM")
    }
}