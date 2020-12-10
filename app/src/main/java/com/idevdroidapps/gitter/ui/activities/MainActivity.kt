package com.idevdroidapps.gitter.ui.activities

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.idevdroidapps.gitter.Injection
import com.idevdroidapps.gitter.R
import com.idevdroidapps.gitter.databinding.ActivityMainBinding
import com.idevdroidapps.gitter.ui.adapters.ReposAdapter
import com.idevdroidapps.gitter.ui.adapters.ReposLoadStateAdapter
import com.idevdroidapps.gitter.ui.fragments.RepoDetailsFragment
import com.idevdroidapps.gitter.ui.utils.onClickKeyboardDoneButton
import com.idevdroidapps.gitter.ui.viewmodels.SharedViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

/**
 * Launcher activity class that handles user interaction with toolbar components
 */
class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ReposAdapter
    private var viewModel: SharedViewModel? = null
    private var searchJob: Job? = null
    private var refreshJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
//        actionBar?.hide()

        viewModel =
            ViewModelProvider(this@MainActivity, Injection.provideMainActViewModelFactory()).get(
                SharedViewModel::class.java
            )

        viewModel?.currentQuery?.observe(this, { query ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                viewModel?.searchRepo(query)?.collectLatest {
                    adapter.submitData(it)
                }
            }
        })
        viewModel?.currentRepo?.observe(this, {
            supportFragmentManager.commit {
                add<RepoDetailsFragment>(R.id.main_fragment_container)
                setReorderingAllowed(false)
                addToBackStack(RepoDetailsFragment::class.java.canonicalName)
            }
        })

        initViews(binding)
        initAdapter(binding)

        if (refreshJob == null) {
            refreshJob = lifecycleScope.launch {
                adapter.loadStateFlow
                    // Only emit when REFRESH LoadState for RemoteMediator changes.
                    .distinctUntilChangedBy { it.refresh }
                    // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { binding.recyclerViewRepos.scrollToPosition(0) }
            }
        }
    }

    /**
     * Initializes the RecyclerView Adapter and LoadStateListener
     *
     * @param   binding The [ActivityMainBinding] received
     */
    private fun initAdapter(binding: ActivityMainBinding?) {
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding?.recyclerViewRepos?.addItemDecoration(decoration)

        adapter = ReposAdapter(viewModel)
        binding?.recyclerViewRepos?.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding?.recyclerViewRepos?.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding?.progressBar?.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding?.retryButton?.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Initializes the EditText input
     *
     * @param   binding The [ActivityMainBinding] received
     */
    private fun initViews(binding: ActivityMainBinding) {
        val editText = binding.editTextRepoSearch
        editText.onClickKeyboardDoneButton {
            setCurrentQuery(binding)
        }
        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.editTextRepoSearch.hint = ""
            } else {
                binding.editTextRepoSearch.hint = getString(R.string.hint_search)
            }
        }
        binding.imageViewSearchButton.setOnClickListener {
            Log.d("GitHub", "Image Touched")
            setCurrentQuery(binding)
        }
        binding.retryButton.setOnClickListener {
            setCurrentQuery(binding)
        }
    }

    /**
     * Saves the current query to [SharedViewModel]
     *
     * @param   binding The [ActivityMainBinding] received
     */
    private fun setCurrentQuery(binding: ActivityMainBinding) {
        binding.editTextRepoSearch.clearFocus()

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        val query = binding.editTextRepoSearch.text.toString()
        viewModel?.setCurrentQuery(query)
    }

}