package ch.zhaw.ma20.cocktailor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import ch.zhaw.ma20.cocktailor.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.ingredient_selectable_item.view.*

class IngredientsSearchAdapter(
    var ingredients: MutableList<Ingredient>

) : BaseAdapter() {
    private var layoutInflater: LayoutInflater = LayoutInflater.from(Cocktailor.applicationContext())
    private var selectedItems = RemoteDataCache.selectedItemsSet

    override fun getCount(): Int {
        return ingredients.size
    }

    override fun getItem(index: Int): Ingredient {
        return ingredients[index]
    }

    override fun getItemId(index: Int): Long {
        return 0
    }

    override fun getView(
        index: Int, oldView: View?,
        viewGroup: ViewGroup?
    ): View {
        //check if we get a view to recycle
        val view: View = oldView ?: layoutInflater.inflate(R.layout.ingredient_selectable_item, null)
        view.ingredientNameCb.setOnClickListener {
            val isChecked: Boolean = view.ingredientNameCb.isChecked
            val itemText: String = view.ingredientNameCb.text.toString()
            if (!isChecked && selectedItems.contains(itemText)) {
                selectedItems.remove(itemText)
            } else if (isChecked) {
                selectedItems.add(itemText)
                if(!RemoteDataCache.isIngredientInMyBar(itemText)) {
                    Snackbar.make(view,(itemText + Cocktailor.applicationContext().getString(R.string.ingredient_not_in_bar)), Snackbar.LENGTH_LONG)
                        .setAction(R.string.add_to_bar) {
                            RemoteDataCache.addIngredientToMyBar(Ingredient(itemText))
                            Toast.makeText(Cocktailor.applicationContext(), (itemText + Cocktailor.applicationContext().getString(R.string.added_to_bar)), Toast.LENGTH_LONG).show()
                        }.show()
                }
            }
            //Log.i("CHECKBOX",view.ingredientNameCb.text.toString() + " checked changed to " + isChecked.toString())
        }
        val entry = getItem(index)
        view.ingredientNameCb.text = entry.strIngredient1
        view.ingredientNameCb.isChecked = selectedItems.contains(view.ingredientNameCb.text.toString())
        return view
    }
}