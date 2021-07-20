package fr.youness.ebook.presentation.adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.youness.ebook.R
import fr.youness.ebook.data.model.Item
import fr.youness.ebook.data.repository.BookRepository
import fr.youness.ebook.presentation.activity.DetailBookActivity
import fr.youness.ebook.utils.EXTRA_BOOK
import kotlinx.android.synthetic.main.book_item.view.*
import kotlinx.coroutines.coroutineScope
import java.util.*
//import java.util.*
//import java.util.*
import kotlin.collections.*

internal class BookAdapter(private val listBook: List<Item>, private val context: Context) :
    BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    lateinit var bookRepository: BookRepository

    override fun getCount(): Int {
        return listBook.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var convertView = convertView
        if (layoutInflater == null) {
            layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.book_item, null)
        }
        Picasso
            .with(convertView!!.context)
            .load(listBook[position].volumeInfo?.imageLinks?.smallThumbnail)
            .into(convertView.bookImage)
        convertView.bookTitle.text = listBook[position].volumeInfo?.title
        listBook[position].volumeInfo?.authors?.forEach { author ->
            convertView.bookAuthor.text = author
//            return convertView
        }

        convertView.setOnClickListener {
            val intent = Intent(context, DetailBookActivity::class.java)
            intent.putExtra(EXTRA_BOOK, listBook[position])
            convertView.context.startActivity(intent)
        }

        return convertView
    }
}