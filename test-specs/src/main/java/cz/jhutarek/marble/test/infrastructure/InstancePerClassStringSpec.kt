package cz.jhutarek.marble.test.infrastructure

import io.kotlintest.extensions.TestListener
import io.kotlintest.specs.AbstractStringSpec
import io.kotlintest.specs.StringSpec

abstract class InstancePerClassStringSpec(
    spec: AbstractStringSpec.() -> Unit = {},
    private vararg val listeners: TestListener
) : StringSpec(spec) {
    final override fun isInstancePerTest() = true
    final override fun listeners() = listeners.toList()
}