package fr.youness.ebook.presentation.activity

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import fr.youness.ebook.R
import fr.youness.ebook.data.model.Item
import fr.youness.ebook.presentation.adapter.BookAdapter
import fr.youness.ebook.presentation.viewmodel.BookViewModel
//import fr.youness.ebook.utils.AUTHOR_BOOK_REQUEST
import fr.youness.ebook.utils.TITLE_BOOK_REQUEST
import fr.youness.ebook.utils.Utils
import kotlinx.android.synthetic.main.activity_list_book.*
import kotlinx.android.synthetic.main.content_list_book.*
import android.util.Log


class ListBookActivity : AppCompatActivity() {

    var listFree: List<Item> = listOf()
    var listPaid: List<Item> = listOf()
    var listMix: MutableList<Item> = mutableListOf()

    lateinit var bookViewModel: BookViewModel
    lateinit private var adapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_book)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        swipe_refresh_list_book.isRefreshing = true
        val book_title = intent.getStringExtra(TITLE_BOOK_REQUEST)

        bookViewModel = BookViewModel(applicationContext)
        if (Utils.isNetworkConnected(this)) {
            bookViewModel.getBooksFromApi(book_title, "paid-ebooks").observe(this,
                Observer {
                    listPaid = it.items

                    bookViewModel.getBooksFromApi(book_title, "free-ebooks").observe(this,
                        Observer {
                            listFree = it.items

                            for(i: Int in 0..listPaid.size-1) {
                                listMix.add(listPaid[i])
                                listMix.add(listFree[i])
                            }

                            setUpBookRecyclerView(listMix)
                        })
                })
        } else {
            Toast.makeText(
                this,
                getString(R.string.no_internet_error),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bookViewModel.cancelJobs()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun setUpBookRecyclerView(books: List<Item>) {
        if (books.isEmpty()) list_empty.visibility = View.VISIBLE
        else {
            recycler_view_list_book.apply {
                swipe_refresh_list_book.isRefreshing = false

                adapter = books.let {
                    BookAdapter(
                        it,
                        this.context)
                }
            }
        }
    }
}
