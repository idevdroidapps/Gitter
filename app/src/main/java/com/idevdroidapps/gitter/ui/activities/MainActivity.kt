package com.idevdroidapps.gitter.ui.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.idevdroidapps.gitter.Injection
import com.idevdroidapps.gitter.R
import com.idevdroidapps.gitter.databinding.ActivityMainBinding
import com.idevdroidapps.gitter.ui.utils.onClickKeyboardDoneButton
import com.idevdroidapps.gitter.ui.viewmodels.SharedViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        actionBar?.hide()

        val viewModel =
            ViewModelProvider(this@MainActivity, Injection.provideMainActViewModelFactory()).get(
                SharedViewModel::class.java
            )

        initEditText(binding)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    /**
     * Initializes the EditText input
     *
     * @param   binding The [ActivityMainBinding] received
     */
    private fun initEditText(binding: ActivityMainBinding) {
        val editText = binding.editTextRepoSearch
        editText.onClickKeyboardDoneButton {
            startSearch(binding)
        }
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.editTextRepoSearch.hint = ""
            } else {
                binding.editTextRepoSearch.hint = getString(R.string.hint_search)
            }
        }
    }

    /**
     * Starts a search of GitHub Repos
     *
     * @param   binding The [ActivityMainBinding] received
     */
    private fun startSearch(binding: ActivityMainBinding) {
        binding.editTextRepoSearch.clearFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        val query = binding.editTextRepoSearch.text.toString()
        Log.d("GitHub Query", query)
    }
}