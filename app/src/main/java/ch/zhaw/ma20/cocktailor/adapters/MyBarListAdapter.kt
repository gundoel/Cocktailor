package ch.zhaw.ma20.cocktailor.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.DataCache
import kotlinx.android.synthetic.main.ingredient_item.view.*

class MyBarListAdapter(
    private var myBarList: MutableList<Ingredient>
) : BaseAdapter() {

    private var layoutInflater: LayoutInflater

    init {
        myBarList.sortBy { ingredientListItem -> ingredientListItem.strIngredient1 }
        layoutInflater = LayoutInflater.from(Cocktailor.applicationContext())
    }

    override fun getCount(): Int {
        return myBarList.size
    }

    override fun getItem(index: Int): Ingredient {
        return myBarList[index]
    }

    override fun getItemId(index: Int): Long {
        return 0
    }

    @SuppressLint("InflateParams")
    override fun getView(
        index: Int, oldView: View?,
        viewGroup: ViewGroup?
    ): View {
        val view: View = oldView ?: layoutInflater.inflate(R.layout.ingredient_item, null)
        val entry = getItem(index)
        view.deleteItemButton.setOnClickListener {
            myBarList.remove(entry)
            DataCache.removeIngredientFromMyBar(entry)
            notifyDataSetChanged()
        }
        view.ingredientName.text = entry.strIngredient1
        return view
    }
}