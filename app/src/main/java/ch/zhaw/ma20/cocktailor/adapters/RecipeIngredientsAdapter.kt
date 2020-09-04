package ch.zhaw.ma20.cocktailor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.Recipe
import ch.zhaw.ma20.cocktailor.model.RecipeIngredientItem
import kotlinx.android.synthetic.main.cocktail_selectable_item.view.*
import kotlinx.android.synthetic.main.recipe_ingredient_item.view.*

class RecipeIngredientsAdapter(
    var recipe: MutableList<RecipeIngredientItem>

) : BaseAdapter() {
    var layoutInflater: LayoutInflater = LayoutInflater.from(Cocktailor.applicationContext())

    override fun getCount(): Int {
        return recipe.size
    }

    override fun getItem(index: Int): RecipeIngredientItem {
        return recipe.get(index)
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
            view = layoutInflater.inflate(R.layout.recipe_ingredient_item, null)
        } else {
            view = oldView
        }
        val entry = getItem(index)
        view.recipeMeasurement.text = entry.measure
        view.recipeIngredient.text = entry.ingredient
        return view
    }
}