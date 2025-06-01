package com.example.myapplication

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class DateInputMask(private val editText: EditText) : TextWatcher {

    private var isUpdating = false
    private val mask = "##/##/####"
    private val regex = Regex("[^\\d]") // Remove tudo que não for número

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable?) {
        if (isUpdating) return

        isUpdating = true

        val unmasked = s.toString().replace(regex, "")
        var masked = ""

        var i = 0
        for (char in mask) {
            if (char != '#' && unmasked.length > i) {
                masked += char
            } else {
                try {
                    masked += unmasked[i]
                    i++
                } catch (_: Exception) {
                    break
                }
            }
        }

        editText.setText(masked)
        editText.setSelection(masked.length.coerceAtMost(mask.length))

        isUpdating = false
    }
}
