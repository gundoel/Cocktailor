package ch.zhaw.ma20.cocktailor.remote

import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.Recipe
import ch.zhaw.ma20.cocktailor.model.RecipeSearchResult
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.beust.klaxon.Klaxon
import java.util.concurrent.atomic.AtomicInteger

class RecipeRequestHandler {
    companion object {
        private val service = ServiceVolley()
        private val apiController = APIController(service)
        val allRequestResults = mutableListOf<Recipe>()
        var hasRequestFailed: Boolean = false

        fun getRecipesForCocktails(
            cocktailList: MutableList<Cocktail>,
            completionHandler: (response: MutableList<Recipe>?) -> Unit
        ) {
            val pendingRequests : AtomicInteger = AtomicInteger(cocktailList.size)
            for (item in cocktailList) {
                val cocktailId = item.idDrink
                val recipePath = "lookup.php?i=$cocktailId"
                // get recipes
                apiController.get(recipePath) { response ->
                    if (response != null) {
                        val recipes = Klaxon().parse<RecipeSearchResult>(response)
                        // recipe is always only 1 (unique id)
                        val recipe = recipes?.drinks?.get(0)
                        if (recipe != null) {
                            allRequestResults.add(recipe)
                        }
                        // got all recipes -> get missing ingredients and cache recipe
                        var ingredientsList = (recipe?.getIngredientsList()
                            ?.map { it.ingredient })!!.toMutableList()
                        item.setIngredientNumbers(ingredientsList)
                        if (pendingRequests.decrementAndGet() == 0) {
                            RemoteDataCache.addLastRecipeSearchResult(allRequestResults)
                            completionHandler(allRequestResults)
                        }
                    } else {
                        // TODO what happens if single request fails?
                        completionHandler(null)
                    }
                }
            }
        }

    }
}