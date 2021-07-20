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
import fr.youness.ebook.utils.AUTHOR_BOOK_REQUEST
import fr.youness.ebook.utils.TITLE_BOOK_REQUEST
import fr.youness.ebook.utils.Utils
import kotlinx.android.synthetic.main.activity_list_book.*
import kotlinx.android.synthetic.main.content_list_book.*
import android.util.Log

import fr.youness.ebook.help.Help


class ListBookActivity : AppCompatActivity() {

    var listFree: List<Item> = listOf()
    var listPaid: List<Item> = listOf()

    var cFree = 0
    var cPaid = 0

    var listMix: MutableList<Item> = mutableListOf()

    lateinit var bookViewModel: BookViewModel

    lateinit private var adapter: BookAdapter

    var helper = Help()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_book)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        swipe_refresh_list_book.isRefreshing = true
        val book_title = intent.getStringExtra(TITLE_BOOK_REQUEST)

        bookViewModel = BookViewModel(applicationContext)
        if (Utils.isNetworkConnected(this)) {

//            adapter = BookAdapter(list1, this@ListBookActivity)
//            recycler_view_list_book.adapter = adapter

            bookViewModel.getBooksFromApi(book_title, "paid-ebooks").observe(this,
                Observer {
                    listPaid = it.items
                    for (item in it.items) {
                        if (item.saleInfo?.saleability.equals("FREE")) {
                            cFree += 1
                        } else {
                            cPaid += 1
                        }
                    }
                    Log.d("FREE / PAID", "${cFree}  ${cPaid}")
//                    setUpBookRecyclerView(it.items)
//                    list1 = it.items
//                    getToList(it.items)
//                    helpFunctionAdd(it.items.toMutableList())
//                    adapter.notifyDataSetChanged()
//                    Log.d("LOG", it.items.toString())

                    bookViewModel.getBooksFromApi(book_title, "free-ebooks").observe(this,
                        Observer {
                            listFree = it.items
                            for (item in it.items) {
                                Log.d("NESTED OBSERVER FREE", item.toString())
                            }

                            for(item in listPaid) {
                                Log.d("NESTED OBSERVER PAID", item.toString())
                            }

                            for(i: Int in 0..listPaid.size-1) {
                                listMix.add(listPaid[i])
                                listMix.add(listFree[i])
                            }

                            Log.d("MIXED LISTED NESTED", listMix.toString())

                            setUpBookRecyclerView(listMix)
                        })
                })
//            Log.d("LOG", list1.toString())
//            Log.d("LOG", helpFunctionGet().toString())


//            bookViewModel.getBooksFromApi(book_title, "paid-ebooks").observe(this,
//                Observer {
//                    setUpBookRecyclerView2(it.items)
////                    list2 = it.items
//                })
//            Log.d("COCK", list2.toString())

//            for(i: Int in 0..(list1.size-1)) {
//                list3.add(list1[i])
//                list3.add(list2[i])
//            }

//            Log.d("BROCK", list3.toString())


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

//    private fun getToList(books: List<Item>) {
////        list1 = books
//        books.let {
//            list1 = it
//        }
//        Log.d("LOG FROM FUNC", books.toString())
//        Log.d("LOG FROM FUNC2", list1.toString())
//    }

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

//    private fun setUpBookRecyclerView2(books: List<Item>) {
//        if (books.isEmpty()) list_empty.visibility = View.VISIBLE
//        else {
//            recycler_view_list_book2.apply {
//                swipe_refresh_list_book.isRefreshing = false
//
//                adapter = books.let {
//                    BookAdapter(
//                        it,
//                        this.context)
//                }
//            }
//        }
//    }

//    private fun helpFunctionAdd(books: List<Item>) {
//        Log.d("INSIDE HELP FUNC", books.toString())
//        helper.addToList(books)
//    }
//
//    private fun helpFunctionGet(): List<Item> {
//        return helper.getFinalList()
//    }
}
