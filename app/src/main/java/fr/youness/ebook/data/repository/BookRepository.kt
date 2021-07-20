package fr.youness.ebook.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import fr.youness.ebook.data.model.ApiResponse
import fr.youness.ebook.data.remote.IBookApi
import fr.youness.ebook.data.remote.ServiceBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class BookRepository(context: Context) {
    private var context: Context = context
    private var getBooksFromApiJob: CompletableJob? = null
    private var insertBookInDbJob: CompletableJob? = null


    fun loadListBook(
        book_title: String,
        price: String
    ): MutableLiveData<ApiResponse> {
        getBooksFromApiJob = Job()
        return object : MutableLiveData<ApiResponse>() {
            override fun onActive() {
                super.onActive()
                getBooksFromApiJob?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        try {
                            val books = ServiceBuilder.buildService(IBookApi::class.java)
                                .getBooksFromApi(book_title, price)
                            withContext(Main) {
                                value = books
                                theJob.complete()
                            }
                        } catch (e: Exception) {
                            Log.d("Error getting books API", e.toString())
                        }
                    }
                }
            }
        }
    }

    fun cancelJobs() {
        getBooksFromApiJob?.cancel()
    }
}