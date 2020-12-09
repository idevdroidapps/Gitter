package com.idevdroidapps.gitter.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.idevdroidapps.gitter.data.models.Repo
import com.idevdroidapps.gitter.data.repositories.GithubRepository
import com.idevdroidapps.gitter.ui.activities.MainActivity
import kotlinx.coroutines.flow.Flow

/**
 * ViewModel for the [MainActivity] screen.
 * The ViewModel works with the [GithubRepository] to get the data.
 */
class SharedViewModel(private val githubRepository: GithubRepository) :
    ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<Repo>>? = null

    /**
     * Queries the [GithubRepository] for matching search results
     *
     * @param   queryString The query term to search, as a [String]
     */
    fun searchRepo(queryString: String): Flow<PagingData<Repo>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Repo>> = githubRepository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}