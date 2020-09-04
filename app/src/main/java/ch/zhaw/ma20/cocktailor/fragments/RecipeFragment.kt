package ch.zhaw.ma20.cocktailor.fragments

import APIController
import ServiceVolley
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.RecipeIngredientsAdapter
import ch.zhaw.ma20.cocktailor.model.RecipeSearchResult
import com.beust.klaxon.Klaxon
import kotlinx.android.synthetic.main.fragment_recipe.view.*

class RecipeFragment(val cocktailId: String) : Fragment() {
    val service = ServiceVolley()
    val apiController = APIController(service)
    var adapter: RecipeIngredientsAdapter? = null;
    val path = "lookup.php?i=$cocktailId"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var layout = inflater.inflate(R.layout.fragment_recipe, container, false)
        apiController.get(path) { response ->
            val recipe = response?.let {
                Klaxon().parse<RecipeSearchResult>(it)
            }
            if (recipe != null) {
                layout.recipeCocktailName.text = recipe.drinks[0].strDrink
                layout.recipeInstructions.text = recipe.drinks[0].strInstructions
                adapter = RecipeIngredientsAdapter(recipe.drinks[0].getIngredientsList())
                layout.recipeIngredients.adapter = adapter
            }
        }
        return layout
    }
}