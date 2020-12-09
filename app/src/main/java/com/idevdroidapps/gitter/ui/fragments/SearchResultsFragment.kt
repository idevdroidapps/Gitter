package com.idevdroidapps.gitter.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.idevdroidapps.gitter.R
import com.idevdroidapps.gitter.databinding.FragmentSearchResultsBinding
import com.idevdroidapps.gitter.ui.adapters.ReposAdapter
import com.idevdroidapps.gitter.ui.adapters.ReposLoadStateAdapter
import com.idevdroidapps.gitter.ui.viewmodels.SharedViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Simple Fragment class for displaying Github Repo search results as a list
 */
class SearchResultsFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultsBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private val adapter = ReposAdapter()
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_results, container, false)

        initAdapter()

        viewModel.currentQuery.observe(viewLifecycleOwner, { query ->
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                viewModel.searchRepo(query).collectLatest {
                    adapter.submitData(it)
                }
            }

        })

        binding.viewModel = viewModel
        return binding.root
    }

    /**
     * Initializes the RecyclerView Adapter and LoadStateListener
     */
    private fun initAdapter() {
        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerViewRepos.addItemDecoration(decoration)

        binding.recyclerViewRepos.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.recyclerViewRepos.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}