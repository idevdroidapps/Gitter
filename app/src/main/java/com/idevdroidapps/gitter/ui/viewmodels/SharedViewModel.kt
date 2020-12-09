package com.idevdroidapps.gitter.ui.viewmodels

import android.util.Log
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
    private var mCurrentQuery = MutableLiveData<String>()
    private var mCurrentSearchResult: Flow<PagingData<Repo>>? = null

    /**
     * Queries the [GithubRepository] for matching search results
     *
     * @param   queryString The query term to search, as a [String]
     */
    fun searchRepo(queryString: String): Flow<PagingData<Repo>> {
        Log.d("GitHub", "SharedViewModel; Starting GitHub Query for: $queryString")
        val lastResult = mCurrentSearchResult
        if (queryString == mCurrentQuery.value && lastResult != null) {
            return lastResult
        }
        val newResult: Flow<PagingData<Repo>> = githubRepository.getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        mCurrentSearchResult = newResult
        return newResult
    }

    /**
     * Sets the value for current query LiveData
     *
     * @param   queryString The query term to search, as a [String]
     */
    fun setCurrentQuery(queryString: String) {
        Log.d("GitHub", "SharedViewModel; New Query Set to: $queryString")
        mCurrentQuery.value = queryString
    }

}