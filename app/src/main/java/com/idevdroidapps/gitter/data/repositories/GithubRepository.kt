package com.idevdroidapps.gitter.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.idevdroidapps.gitter.data.models.Repo
import com.idevdroidapps.gitter.data.network.GithubPagingSource
import com.idevdroidapps.gitter.data.network.GithubService
import kotlinx.coroutines.flow.Flow

class GithubRepository(private val service: GithubService) {

    /**
     * Search repositories whose names match the query, exposed as a stream of data that will emit
     * every time we get more data from the network.
     */
    fun getSearchResultStream(query: String): Flow<PagingData<Repo>> {
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = { GithubPagingSource(service, query) }
        ).flow
    }

    companion object {

        private const val NETWORK_PAGE_SIZE = 50

        // For Singleton instantiation
        @Volatile
        private var instance: GithubRepository? = null

        fun getInstance(service: GithubService) =
            instance ?: synchronized(this) {
                instance ?: GithubRepository(service).also { instance = it }
            }
    }
}