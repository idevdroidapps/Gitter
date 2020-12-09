package com.idevdroidapps.gitter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.idevdroidapps.gitter.R
import com.idevdroidapps.gitter.databinding.FragmentSearchResultsBinding
import com.idevdroidapps.gitter.ui.viewmodels.SharedViewModel

class SearchResultsFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultsBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_results, container, false)

        binding.viewModel = viewModel
        return binding.root
    }
}