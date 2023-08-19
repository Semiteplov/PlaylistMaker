package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
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

        searchEditText?.doOnTextChanged { _, _, _, count ->
            clearButton?.visibility = if (count == 0) View.GONE else View.VISIBLE
        }

        clearButton?.setOnClickListener {
            searchEditText?.text?.clear()
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
}
