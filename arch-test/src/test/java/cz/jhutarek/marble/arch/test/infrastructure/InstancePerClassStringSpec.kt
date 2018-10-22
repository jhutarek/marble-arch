package cz.jhutarek.marble.arch.test.infrastructure

import io.kotlintest.specs.AbstractStringSpec
import io.kotlintest.specs.StringSpec

abstract class InstancePerClassStringSpec(body: AbstractStringSpec.() -> Unit = {}) : StringSpec(body) {
    final override fun isInstancePerTest() = true
}