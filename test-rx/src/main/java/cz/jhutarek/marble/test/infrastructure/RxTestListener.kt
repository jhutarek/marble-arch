package cz.jhutarek.marble.test.infrastructure

import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.extensions.TestListener
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

object RxTestListener : TestListener {

    override fun beforeSpec(description: Description, spec: Spec) {
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setSingleSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    override fun afterSpec(description: Description, spec: Spec) {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}