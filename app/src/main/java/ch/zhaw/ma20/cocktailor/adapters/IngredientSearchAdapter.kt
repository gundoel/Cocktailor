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
import com.example.cocktailor.R
import kotlinx.android.synthetic.main.ingredient_selectable_item.view.*

class IngredientsSearchAdapter(
    var ingredients: MutableList<IngredientListItem>,
    val context: Context

) : BaseAdapter() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(context)
    var selectedItems = RemoteDataCache.selectedItems

    override fun getCount(): Int {
        return ingredients.size
    }

    override fun getItem(index: Int): IngredientListItem {
        return ingredients.get(index)
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
            view = layoutInflater.inflate(R.layout.ingredient_selectable_item, null)
        } else {
            view = oldView
        }
        view.ingredientNameCb.setOnClickListener {
            val isChecked: Boolean = view.ingredientNameCb.isChecked
            val itemText: String = view.ingredientNameCb.text.toString()
            if (!isChecked && selectedItems.contains(itemText)) {
                selectedItems.remove(itemText)
            } else if (isChecked) {
                selectedItems.add(itemText)
            }
            //Log.i("CHECKBOX",view.ingredientNameCb.text.toString() + " checked changed to " + isChecked.toString())
        }
        val entry = getItem(index)
        view.ingredientNameCb.text = entry.strIngredient1
        view.ingredientNameCb.isChecked = selectedItems.contains(view.ingredientNameCb.text.toString())
        return view
    }
}