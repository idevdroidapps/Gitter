package com.idevdroidapps.gitter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.idevdroidapps.gitter.data.network.GithubService
import com.idevdroidapps.gitter.data.repositories.GithubRepository
import com.idevdroidapps.gitter.ui.viewmodels.SharedViewModelFactory

object Injection {

    /**
     * Creates an instance of [GithubRepository]
     */
    private fun provideGithubRepo(): GithubRepository {
        return GithubRepository.getInstance(GithubService.create())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideMainActViewModelFactory(): ViewModelProvider.Factory {
        return SharedViewModelFactory(provideGithubRepo())
    }

}