package cz.jhutarek.marble.arch.resources.device

import android.content.Context
import cz.jhutarek.marble.arch.resources.domain.StringsController
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidStringsController @Inject constructor(
        private val context: Context
) : StringsController {
    override fun getString(resId: Int): String = context.getString(resId)
}