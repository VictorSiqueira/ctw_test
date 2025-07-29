import kotlinx.coroutines.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

interface NewsRepository {
    suspend fun getNews(): NewsResponse
}

data class NewsResponse(val articles: List<String>)

data class NewsState(
    val isLoading: Boolean = false,
    val newsResponse: NewsResponse? = null,
    val errorMessage: String? = null
)

class NewsViewModel(private val repository: NewsRepository) {
    var state = NewsState()
        private set

    fun loadNews() {
        state = state.copy(isLoading = true)

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val news = repository.getNews()
                state = state.copy(isLoading = false, newsResponse = news, errorMessage = null)
            } catch (e: Exception) {
                state = state.copy(isLoading = false, newsResponse = null, errorMessage = e.message)
            }
        }
    }
}

class MockNewsRepository : NewsRepository {
    var shouldThrow = false
    var newsResponse = NewsResponse(emptyList())
    var exceptionMessage = "Network error"

    override suspend fun getNews(): NewsResponse {
        delay(10)
        if (shouldThrow) throw RuntimeException(exceptionMessage)
        return newsResponse
    }
}

class NewsViewModelTest {
    private lateinit var repository: MockNewsRepository
    private lateinit var viewModel: NewsViewModel

    @Before
    fun setup() {
        repository = MockNewsRepository()
        viewModel = NewsViewModel(repository)
    }

    @Test
    fun `initial state is correct`() {
        assertFalse(viewModel.state.isLoading)
        assertNull(viewModel.state.newsResponse)
        assertNull(viewModel.state.errorMessage)
    }

    @Test
    fun `loadNews sets newsResponse on success`() = runBlocking {
        val expectedResponse = NewsResponse(listOf("article1", "article2"))
        repository.newsResponse = expectedResponse
        repository.shouldThrow = false

        viewModel.loadNews()
        delay(50)

        assertFalse(viewModel.state.isLoading)
        assertEquals(expectedResponse, viewModel.state.newsResponse)
        assertNull(viewModel.state.errorMessage)
    }

    @Test
    fun `loadNews sets errorMessage on failure`() = runBlocking {
        repository.shouldThrow = true
        repository.exceptionMessage = "Network error"

        viewModel.loadNews()
        delay(50)

        assertFalse(viewModel.state.isLoading)
        assertNull(viewModel.state.newsResponse)
        assertEquals("Network error", viewModel.state.errorMessage)
    }
}