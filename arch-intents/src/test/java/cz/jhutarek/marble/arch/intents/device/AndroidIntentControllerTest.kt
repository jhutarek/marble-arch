package cz.jhutarek.marble.arch.intents.device

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

internal class AndroidIntentControllerTest : InstancePerClassStringSpec({

    val url = "url"
    val context = mockk<Context>(relaxUnitFun = true)

    val controller = AndroidIntentController(context)

    "controller should try to open url in browser with correct settings" {
        val slot = slot<Intent>()
        every { context.startActivity(capture(slot)) } returns Unit

        controller.browse(url)

        with(slot.captured) {
            action shouldBe ACTION_VIEW
            data shouldBe Uri.parse(url)
            flags shouldBe FLAG_ACTIVITY_NEW_TASK
        }
    }

    "controller should swallow exception if there is no application to open url with" {
        every { context.startActivity(any()) } throws ActivityNotFoundException()

        controller.browse(url)
    }
})