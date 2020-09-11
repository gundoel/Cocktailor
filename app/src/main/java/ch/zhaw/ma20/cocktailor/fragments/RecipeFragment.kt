package ch.zhaw.ma20.cocktailor.fragments

import ch.zhaw.ma20.cocktailor.remote.APIController
import ch.zhaw.ma20.cocktailor.remote.ServiceVolley
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ch.zhaw.ma20.cocktailor.Cocktailor
import ch.zhaw.ma20.cocktailor.R
import ch.zhaw.ma20.cocktailor.adapters.RecipeIngredientsAdapter
import ch.zhaw.ma20.cocktailor.model.Recipe
import ch.zhaw.ma20.cocktailor.model.RecipeSearchResult
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.beust.klaxon.Klaxon
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_recipe.view.*

class RecipeFragment(val cocktailId: String) : Fragment() {
    val service = ServiceVolley()
    val apiController = APIController(service)
    var adapter: RecipeIngredientsAdapter? = null;
    val path = "lookup.php?i=$cocktailId"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val manager: FragmentManager = activity!!.supportFragmentManager
                if (manager.backStackEntryCount > 0) {
                    manager.popBackStackImmediate()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var recipe: Recipe? = RemoteDataCache.lastRecipeSearchResultMap[cocktailId]
        /* load recipe from cache (available if recipe was cached during search.
        If recipe is called from favorites, recipe might not be in cache and needs therefore to be reloaded          */
        if (recipe == null) {
            apiController.get(path) { response ->
                val recipeRemote = response?.let {
                    Klaxon().parse<RecipeSearchResult>(it)
                }
                if (recipeRemote != null) {
                    recipe = recipeRemote.drinks[0];
                }
            }
        }
        return getView(container, inflater, recipe)
    }

    fun getView(container: ViewGroup?, inflater: LayoutInflater, recipe: Recipe?): View {
        var layout = inflater.inflate(R.layout.fragment_recipe, container, false)
        if (recipe != null) {
            var imageView : ImageView = layout.cocktailImage
            Picasso.get().load(recipe.strDrinkThumb).into(imageView);
            layout.recipeCocktailName.text = recipe.strDrink
            layout.recipeInstructions.text = recipe.strInstructions
            // ingredients list
            adapter = RecipeIngredientsAdapter(recipe.getIngredientsList())
            layout.recipeIngredients.adapter = adapter
            // initialize like button
            layout.likeButton.isChecked = RemoteDataCache.isRecipeInFavorites(recipe)
            // like button action
            layout.likeButton.setOnClickListener() {
                if (layout.likeButton.isChecked) {
                    RemoteDataCache.addRecipeToFavorites(recipe)
                    Toast.makeText(
                        Cocktailor.applicationContext(),
                        recipe.strDrink + Cocktailor.applicationContext().getString(R.string.added_to_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    RemoteDataCache.removeRecipeFromFavorites(recipe)
                    Toast.makeText(
                        Cocktailor.applicationContext(),
                        recipe.strDrink + Cocktailor.applicationContext().getString(R.string.removed_from_favorites),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return layout

    }
}