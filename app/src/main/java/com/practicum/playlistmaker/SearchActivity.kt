package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    companion object{
        const val SEARCH_TEXT_KEY = "TEXT_KEY"
        const val EMPTY = ""
    }
    private var valueFromET = EMPTY
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, valueFromET)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueFromET = savedInstanceState.getString(SEARCH_TEXT_KEY, EMPTY)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)


        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val editText = findViewById<EditText>(R.id.findEditText)
        val backButton = findViewById<ImageView>(R.id.backImage)

        editText.setText(valueFromET)

        clearButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.setText(EMPTY)
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int): Unit {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                    valueFromET = p0.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?): Unit {}
        }

        editText.addTextChangedListener(watcher)

        backButton.setOnClickListener {
            finish()
        }
    }
}