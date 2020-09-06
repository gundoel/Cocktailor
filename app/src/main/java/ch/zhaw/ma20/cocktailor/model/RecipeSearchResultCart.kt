package ch.zhaw.ma20.cocktailor.model

import java.util.concurrent.atomic.AtomicInteger

class RecipeSearchResultCart(val pendingRequests: AtomicInteger, val hasRequestFailed: Boolean) {
    val allRequestResults = mutableListOf<Recipe>()

    fun addQueryResult(recipe: Recipe) {
        allRequestResults.add(recipe)
    }
}