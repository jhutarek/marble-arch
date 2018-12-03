package cz.jhutarek.marble.arch.intents.device

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import cz.jhutarek.marble.arch.intents.domain.IntentController
import cz.jhutarek.marble.arch.log.infrastructure.logE
import cz.jhutarek.marble.arch.log.infrastructure.logI
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidIntentController @Inject constructor(private val context: Context) : IntentController {

    override fun browse(url: String) {
        logI("Browse: $url")

        try {
            Intent(ACTION_VIEW, Uri.parse(url))
                .apply { addFlags(FLAG_ACTIVITY_NEW_TASK) }
                .let { context.startActivity(it) }
        } catch (e: ActivityNotFoundException) {
            logE("Activity not found for browse intent: $e")
        }
    }
}