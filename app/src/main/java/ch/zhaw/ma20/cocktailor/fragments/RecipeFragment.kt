package ch.zhaw.ma20.cocktailor.fragments

import APIController
import ServiceVolley
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.RecipeIngredientsAdapter
import ch.zhaw.ma20.cocktailor.model.Recipe
import ch.zhaw.ma20.cocktailor.model.RecipeSearchResult
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
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
        // TODO load recipe from cache if available
        apiController.get(path) { response ->
            val recipe = response?.let {
                Klaxon().parse<RecipeSearchResult>(it)
            }
            if (recipe != null) {
                // recipe is always only 1 (id is unique)
                var currentRecipe: Recipe = recipe.drinks[0];
                // set infos
                layout.recipeCocktailName.text = currentRecipe.strDrink
                layout.recipeInstructions.text = currentRecipe.strInstructions
                // ingredients list
                adapter = RecipeIngredientsAdapter(currentRecipe.getIngredientsList())
                layout.recipeIngredients.adapter = adapter
                // initialize like button
                layout.likeButton.isChecked = RemoteDataCache.isRecipeInFavorites(currentRecipe)
                // like button action
                layout.likeButton.setOnClickListener() {
                    if (layout.likeButton.isChecked) {
                        RemoteDataCache.addRecipeToFavorites(currentRecipe)
                    } else {
                        RemoteDataCache.removeRecipeFromFavorites(currentRecipe)
                    }
                }
            }
        }

        return layout
    }
}