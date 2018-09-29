package cz.jhutarek.marble.arch.mvvm.system

import android.widget.EditText

var EditText.textString: String?
    get() = text?.toString()
    set(value) {
        val nullableSelectionStart = selectionStart.takeIf { it >= 0 }
        setText(value)
        nullableSelectionStart?.let { setSelection(it) }
    }