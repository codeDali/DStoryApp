package com.dicoding.picodiploma.loginwithanimation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.picodiploma.loginwithanimation.adapter.StoriesAdapter
import com.dicoding.picodiploma.loginwithanimation.data.response.StoriesItem
import com.dicoding.picodiploma.loginwithanimation.helper.UserRepository
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: UserRepository

    @Test
    fun `when Get Quote Should Not Null and Return Data`() = runTest {
        val dummyStory = Dummy.generateDummyQuoteResponse()
        val data: PagingData<StoriesItem> = StoryPagingSource.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<StoriesItem>>()
        expectedStory.value = data
        Mockito.`when`(repository.getStories()).thenReturn(expectedStory)

        val mainViewModel = MainViewModel(repository)
        val actualStory: PagingData<StoriesItem> = mainViewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
        Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story is Empty amd Return No Data`() = runTest {
        val data: PagingData<StoriesItem> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<StoriesItem>>()
        expectedStory.value = data
        Mockito.`when`(repository.getStories()).thenReturn(expectedStory)

        val viewModel = MainViewModel(repository)
        val actualStory = viewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        Assert.assertEquals(0, differ.snapshot().size)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}

class StoryPagingSource: PagingSource<Int, LiveData<List<StoriesItem>>>(){
    companion object {
        fun snapshot(item: List<StoriesItem>): PagingData<StoriesItem> {
            return PagingData.from(item)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoriesItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoriesItem>>> {
        return LoadResult.Page(emptyList(),0,1)
    }
}