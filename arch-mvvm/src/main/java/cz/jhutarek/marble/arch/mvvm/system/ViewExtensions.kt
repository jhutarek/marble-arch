package cz.jhutarek.marble.arch.mvvm.system

import android.widget.EditText

// TODO test
var EditText.textString: String?
    get() = text?.toString()
    set(value) {
        if (value != text?.toString()) {
            val nullableSelectionStart = selectionStart.takeIf { it >= 0 && it < value?.length ?: 0 }
            setText(value)
            nullableSelectionStart?.let { setSelection(it) }
        }
    }