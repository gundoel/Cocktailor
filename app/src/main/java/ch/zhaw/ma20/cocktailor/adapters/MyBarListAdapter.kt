package ch.zhaw.ma20.cocktailor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.R
import kotlinx.android.synthetic.main.fragment_my_bar.view.*
import kotlinx.android.synthetic.main.ingredient_item.view.*

class MyBarListAdapter(
    var myBarList: MutableList<Ingredient>,
    val context: Context
) : BaseAdapter() {

    var layoutInflater: LayoutInflater

    init {
        // sort alphabetically
        myBarList.sortBy { ingredientListItem -> ingredientListItem.strIngredient1 }
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int { //number of elements to display
        return myBarList.size
    }

    override fun getItem(index: Int): Ingredient { //item at index
        return myBarList.get(index)
    }

    override fun getItemId(index: Int): Long { //itemId for
        return 0
    }

    override fun getView(
        index: Int, oldView: View?,
        viewGroup: ViewGroup?
    ): View {
        var view: View
        if (oldView == null) { //check if we get a view to recycle
            view = layoutInflater.inflate(R.layout.ingredient_item, null)
        } else { //if yes, use the oldview
            view = oldView
        }
        val entry = getItem(index) //get the data for this index

        // delete item onclick (quickfix)
        // TODO implement button
        // TODO displaying bar empty text not working if all items are deleted
        view.setOnClickListener() {
            myBarList.remove(entry)
            notifyDataSetChanged()
        }
        // display empty Text if bar is empty. if not, use adapter to display items.
        if (myBarList.isEmpty()) {
            val listView: ListView = view.listViewMyBar as ListView
            val emptyText = R.id.emptyBar as TextView
            listView.emptyView = emptyText
        } else {
            view.ingredientName.text = entry.strIngredient1 //and fill the layout}
        }
        return view
    }
}