package com.ranjeet.peko.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager


fun Activity.hideKeyboard() {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (currentFocus != null) {
        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
    }
}