package com.idevdroidapps.gitter.data.network

import android.util.Log
import androidx.paging.PagingSource
import com.idevdroidapps.gitter.data.models.Repo
import retrofit2.HttpException
import java.io.IOException

// GitHub page API is 1 based
private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubPagingSource(
    private val service: GithubService,
    private val query: String
) : PagingSource<Int, Repo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val position = params.key ?: GITHUB_STARTING_PAGE_INDEX
        val apiQuery = query + IN_QUALIFIER
        Log.d("GitHub", "GithubPagingSource; Making query: $apiQuery")
        return try {
            val response = service.searchRepos(apiQuery, position, params.loadSize)
            val repos = response.items
            LoadResult.Page(
                data = repos,
                prevKey = if (position == GITHUB_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (repos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}