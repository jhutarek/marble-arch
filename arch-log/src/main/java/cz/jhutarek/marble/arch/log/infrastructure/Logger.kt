package cz.jhutarek.marble.arch.log.infrastructure

import cz.jhutarek.marble.arch.log.BuildConfig
import timber.log.Timber

object Logger {

    private class TagPrefixDebugTree(private val tagPrefix: String) : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) =
            super.log(priority, tagPrefix + tag, message, throwable)
    }

    fun initialize(tagPrefix: String) {
        if (BuildConfig.DEBUG) {
            Timber.plant(TagPrefixDebugTree(tagPrefix))
        }
    }
}

fun <T : Any> T.logD(message: String) = log(message) { Timber.d(it) }

fun <T : Any> T.logI(message: String) = log(message) { Timber.i(it) }

fun <T : Any> T.logW(message: String) = log(message) { Timber.w(it) }

fun <T : Any> T.logE(message: String) = log(message) { Timber.e(it) }

private fun <T : Any> T.log(message: String, block: (String) -> Unit) {
    Timber.tag(this::class.java.simpleName)
    block(message)
}