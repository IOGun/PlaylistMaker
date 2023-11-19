package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class FindActivity : AppCompatActivity() {
    companion object{
        const val SEARCH_TEXT_KEY = "TEXT_KEY"
        const val VALUE_ET_DEF = ""
    }
    private var valueFromET = VALUE_ET_DEF
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, valueFromET)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueFromET = savedInstanceState.getString(SEARCH_TEXT_KEY, VALUE_ET_DEF)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)


        val clearButton = findViewById<ImageView>(R.id.clear_button)
        val editText = findViewById<EditText>(R.id.find_edit_text)
        val backButton = findViewById<ImageView>(R.id.back_image)

        editText.setText(valueFromET)

        clearButton.setOnClickListener {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.setText("")
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                    valueFromET = p0.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                // TODO("Not yet implemented")
            }
        }

        editText.addTextChangedListener(simpleTextWatcher)

        backButton.setOnClickListener {
            finish()
        }
    }
}