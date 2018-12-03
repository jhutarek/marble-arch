package cz.jhutarek.marble.arch.intents.domain

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import cz.jhutarek.marble.arch.test.infrastructure.InstancePerClassStringSpec
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

internal class IntentUseCaseBrowseTest : InstancePerClassStringSpec({
    val context = mockk<Context>(relaxUnitFun = true)
    val uri = Uri.parse("http://example.com")
    val error = IllegalStateException()

    val browse = IntentUseCase.Browse(context)

    "use case should try to start the activity with correct settings" {
        val slot = slot<Intent>()
        every { context.startActivity(capture(slot)) } returns Unit

        browse(uri)
            .test()

        with(slot.captured) {
            action shouldBe ACTION_VIEW
            data shouldBe uri
        }
    }

    "use case should return complete completable if activity is started" {
        browse(uri)
            .test()
            .assertComplete()
    }

    "use case should return error completable if start activity throws an exception" {
        every { context.startActivity(any()) } throws error

        browse(uri)
            .test()
            .assertError(error)
    }
})