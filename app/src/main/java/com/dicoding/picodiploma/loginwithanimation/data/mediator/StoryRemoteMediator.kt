package com.dicoding.picodiploma.loginwithanimation.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.picodiploma.loginwithanimation.data.db.RemoteKeys
import com.dicoding.picodiploma.loginwithanimation.data.db.StoriesDatabase
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesItem
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val storiesDatabase: StoriesDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, StoriesItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoriesItem>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH ->{
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            Log.d("StoryRemoteMediator", "Loading page: $page with pageSize: ${state.config.pageSize}")
            val responseData = apiService.getAllStories(page, state.config.pageSize).listStory

            val endOfPaginationReached = responseData?.isEmpty()

            Log.d("StoryRemoteMediator", "Received response data: $responseData")

            storiesDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    storiesDatabase.remoteKeysDao().deleteRemoteKeys()
                    storiesDatabase.storyDao().clearAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached == true) null else page + 1
                val keys = responseData?.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                keys?.let { storiesDatabase.remoteKeysDao().insertAll(it) }
                responseData?.let { storiesDatabase.storyDao().insertAll(it) }
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached == true)
        } catch (exception: Exception) {
            Log.e("StoryRemoteMediator", "Error loading data", exception)
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoriesItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            storiesDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoriesItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            storiesDatabase.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoriesItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                storiesDatabase.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}