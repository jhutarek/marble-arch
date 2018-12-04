package cz.jhutarek.marble.arch.intents.domain

import cz.jhutarek.marble.test.infrastructure.InstancePerClassStringSpec
import io.mockk.mockk
import io.mockk.verify

internal class IntentUseCaseBrowseTest : InstancePerClassStringSpec({
    val url = "url"
    val intentController = mockk<IntentController>(relaxUnitFun = true)

    val browse = IntentUseCase.Browse(intentController)

    "use case should delegate browse action to controller" {
        browse(url)

        verify { intentController.browse(url) }
    }
})