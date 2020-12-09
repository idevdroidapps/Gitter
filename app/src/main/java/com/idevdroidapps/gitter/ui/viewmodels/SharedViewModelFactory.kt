package com.idevdroidapps.gitter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.idevdroidapps.gitter.data.repositories.GithubRepository

class SharedViewModelFactory(
    private val githubRepository: GithubRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
            return SharedViewModel(githubRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}