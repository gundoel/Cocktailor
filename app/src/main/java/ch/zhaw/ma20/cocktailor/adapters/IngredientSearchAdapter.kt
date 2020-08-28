package ch.zhaw.ma20.cocktailor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ch.zhaw.ma20.cocktailor.model.IngredientListItem
import com.example.cocktailor.R
import kotlinx.android.synthetic.main.ingredient_selectable_item.view.*

class IngredientsSearchAdapter(var stationboardEntries: MutableList<IngredientListItem>, val context: Context) : BaseAdapter() {
    var layoutInflater : LayoutInflater

    init {
        // sort alphabetically
        stationboardEntries.sortBy { it.strIngredient1.toString()}
        layoutInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int { //number of elements to display
        return stationboardEntries.size
    }

    override fun getItem(index: Int): IngredientListItem { //item at index
        return stationboardEntries.get(index)
    }

    override fun getItemId(index: Int): Long { //itemId for
        return 0
    }

    override fun getView(index: Int, oldView: View?,
                         viewGroup: ViewGroup?): View {
        var view : View
        if (oldView == null) { //check if we get a view to recycle
            view = layoutInflater.inflate(R.layout.ingredient_selectable_item, null)
        }
        else { //if yes, use the oldview
            view = oldView
        }

        val entry = getItem(index) //get the data for this index
        view.ingredientName.text = entry.strIngredient1 //and fill the layout
        return view
    }
}