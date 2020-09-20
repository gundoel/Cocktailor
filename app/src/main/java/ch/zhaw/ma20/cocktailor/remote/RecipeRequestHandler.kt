package ch.zhaw.ma20.cocktailor.remote

import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.Recipe
import ch.zhaw.ma20.cocktailor.model.RecipeSearchResult
import ch.zhaw.ma20.cocktailor.model.DataCache
import com.beust.klaxon.Klaxon
import java.util.concurrent.atomic.AtomicInteger

class RecipeRequestHandler {
    companion object {
        private val service = ServiceVolley()
        private val apiController = APIController(service)
        private lateinit var allRequestResults: MutableList<Recipe>

        /**
         * Returns and caches Recipes for given set of cocktails. Result are delivered, when all requests finished.
         * All requests are collected and callback (completion handler) is executed, after all results
         * have been delivered. Results are being cached. Caller does not neet to handle caching and can access data from Cache.
         */
        fun getAndCacheRecipesForCocktails(
            cocktailList: MutableList<Cocktail>,
            completionHandler: (response: MutableList<Recipe>?) -> Unit
        ) {
            val pendingRequests = AtomicInteger(cocktailList.size)
            allRequestResults = mutableListOf<Recipe>()
            for (item in cocktailList) {
                val cocktailId = item.idDrink
                val recipePath = "lookup.php?i=$cocktailId"
                // get recipes
                apiController.get(recipePath) { response ->
                    if (response != null) {
                        val recipes = Klaxon().parse<RecipeSearchResult>(response)
                        // recipe is always only 1 (id is unique)
                        val recipe = recipes?.drinks?.get(0)
                        if (recipe != null) {
                            allRequestResults.add(recipe)
                        }
                        // got all recipes -> get missing ingredients and cache recipe
                        val ingredientsList = (recipe?.getIngredientsList()
                            ?.map { it.ingredient })!!.toMutableList()
                        item.setIngredientNumbers(ingredientsList)
                        if (pendingRequests.decrementAndGet() == 0) {
                            DataCache.addLastRecipeSearchResult(allRequestResults)
                            completionHandler(allRequestResults)
                        }
                    } else {
                        // at least one request failed. return null to handle in calling fragment.
                        completionHandler(null)
                    }
                }
            }
        }

    }
}