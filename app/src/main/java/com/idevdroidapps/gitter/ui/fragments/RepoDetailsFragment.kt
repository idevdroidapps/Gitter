package com.idevdroidapps.gitter.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.idevdroidapps.gitter.R
import com.idevdroidapps.gitter.databinding.FragmentRepoDetailsBinding
import com.idevdroidapps.gitter.ui.viewmodels.SharedViewModel

class RepoDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRepoDetailsBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_repo_details, container, false)

        binding.closeButton.setOnClickListener {
            findNavController().navigate(RepoDetailsFragmentDirections.actionGlobalSearchResultsFragment())
        }

        binding.browserButton.setOnClickListener {
            viewModel.currentRepo.value?.let { repo ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(repo.url))
                context?.startActivity(intent)
            }
        }

        binding.viewModel = viewModel
        return binding.root
    }

}