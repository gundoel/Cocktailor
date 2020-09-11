package ch.zhaw.ma20.cocktailor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.model.RecipeIngredient
import kotlinx.android.synthetic.main.recipe_ingredient_item.view.*

class RecipeIngredientsAdapter(
    private var recipe: MutableList<RecipeIngredient>

) : BaseAdapter() {
    private var layoutInflater: LayoutInflater = LayoutInflater.from(Cocktailor.applicationContext())

    override fun getCount(): Int {
        return recipe.size
    }

    override fun getItem(index: Int): RecipeIngredient {
        return recipe[index]
    }

    override fun getItemId(index: Int): Long {
        return 0
    }

    override fun getView(
        index: Int, oldView: View?,
        viewGroup: ViewGroup?
    ): View {
        //check if we get a view to recycle
        val view: View = oldView ?: layoutInflater.inflate(R.layout.recipe_ingredient_item, null)
        val entry = getItem(index)
        view.recipeMeasurement.text = entry.measure
        view.recipeIngredient.text = entry.ingredient
        if (entry.isIngredientInMyBar) {
            view.recipeIngredientInMyBar.setBackgroundResource(R.drawable.ic_baseline_done_24)
            //view.recipeIngredientInMyBar.setPadding(0,0,0,0);
        }
        return view
    }
}