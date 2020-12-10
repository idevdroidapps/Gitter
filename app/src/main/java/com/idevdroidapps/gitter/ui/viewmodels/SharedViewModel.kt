package com.idevdroidapps.gitter.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val currentQuery: LiveData<String> get() = mCurrentQuery
    val currentRepo: LiveData<Repo> get() = mCurrentRepo
    private var mPreviousQuery: String? = null
    private var mCurrentRepo = MutableLiveData<Repo>()
    private var mCurrentQuery = MutableLiveData<String>()
    private var mCurrentSearchResult: Flow<PagingData<Repo>>? = null

    /**
     * Queries the [GithubRepository] for matching search results
     *
     * @param   queryString The query term to search, as a [String]
     */
    fun searchRepo(queryString: String): Flow<PagingData<Repo>> {
        val lastResult = mCurrentSearchResult
        if (queryString == mPreviousQuery && lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Repo>> = githubRepository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        mPreviousQuery = queryString
        mCurrentSearchResult = newResult
        return newResult
    }

    /**
     * Sets the value for current repo LiveData
     *
     * @param   repo The currently selected [Repo]
     */
    fun setCurrentRepo(repo: Repo) {
        mCurrentRepo.value = repo
    }

    /**
     * Sets the value for current query LiveData
     *
     * @param   queryString The query term to search, as a [String]
     */
    fun setCurrentQuery(queryString: String) {
        mCurrentQuery.value = queryString
    }

}