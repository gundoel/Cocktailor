package ch.zhaw.ma20.cocktailor.model

import com.google.gson.Gson

object RemoteDataCache {
    var ingredientsList = mutableListOf<IngredientListItem>()
    val selectedItemsSet = mutableSetOf<String>()
    var lastCocktailSearchResultList = mutableListOf<Cocktail>()
    var emptyBarString = "The bar is empty!"
    var myBarList: MutableList<IngredientListItem>? = mutableListOf(IngredientListItem(emptyBarString))
    var nameMyBarList = "MyBarList"
    val gson = Gson()

    fun addIngredientsList(list : MutableList<IngredientListItem>) {
        // sort alphabetically
        list.sortBy { it.strIngredient1.toString() }
        ingredientsList = list;
    }

    fun addLastCocktailSearchResultList(list : MutableList<Cocktail>) {
        lastCocktailSearchResultList = list;
    }

    fun addMyBarList(list : MutableList<IngredientListItem>) {
        if (list == null) myBarList?.add(IngredientListItem(emptyBarString))
        else myBarList = list
    }
}