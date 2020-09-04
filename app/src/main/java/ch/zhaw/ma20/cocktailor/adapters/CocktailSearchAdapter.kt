package ch.zhaw.ma20.cocktailor.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import ch.zhaw.ma20.cocktailor.model.IngredientListItem
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.model.Cocktail
import kotlinx.android.synthetic.main.cocktail_selectable_item.view.*
import kotlinx.android.synthetic.main.ingredient_selectable_item.view.*

class CocktailSearchAdapter(
    var cocktails: MutableList<Cocktail>,
    val context: Context

) : BaseAdapter() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return cocktails.size
    }

    override fun getItem(index: Int): Cocktail {
        return cocktails.get(index)
    }

    override fun getItemId(index: Int): Long {
        return 0
    }

    override fun getView(
        index: Int, oldView: View?,
        viewGroup: ViewGroup?
    ): View {
        var view: View
        //check if we get a view to recycle
        if (oldView == null) {
            view = layoutInflater.inflate(R.layout.cocktail_selectable_item, null)
        } else {
            view = oldView
        }
        val entry = getItem(index)
        view.cocktailThumb.tooltipText = entry.strDrinkThumb
        view.cocktailName.text = entry.strDrink
        return view
    }
}