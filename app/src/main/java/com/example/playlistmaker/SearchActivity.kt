package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged

class SearchActivity : AppCompatActivity() {
    private var searchEditText: EditText? = null
    private var clearButton: ImageButton? = null
    private var savedText: String = ""

    companion object {
        const val SEARCH_TEXT = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById(R.id.search)
        clearButton = findViewById(R.id.clear_button)
        val backButton = findViewById<Toolbar>(R.id.search_back_button)

        backButton.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        savedInstanceState?.let {
            savedText = it.getString(SEARCH_TEXT, "")
            searchEditText?.setText(savedText)
        }

        searchEditText?.doOnTextChanged { text, _, _, _ ->
            savedText = text.toString()
            clearButton?.visibility = if (text?.isEmpty() == true) View.GONE else View.VISIBLE
        }

        clearButton?.setOnClickListener {
            searchEditText?.text?.clear()
            hideKeyboard()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        savedText = searchEditText?.text.toString()
        outState.putString(SEARCH_TEXT, savedText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedText = savedInstanceState.getString(SEARCH_TEXT, "")
        searchEditText?.setText(savedText)
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
