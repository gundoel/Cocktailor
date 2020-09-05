package ch.zhaw.ma20.cocktailor.model

import ch.zhaw.ma20.cocktailor.model.RemoteDataCache.gson
import com.google.gson.Gson

object RemoteDataCache {
    var ingredientsList = mutableListOf<IngredientListItem>()
    val selectedItemsSet = mutableSetOf<String>()
    var lastCocktailSearchResultList = mutableListOf<Cocktail>()
    var emptyBarString = "The bar is empty!"
    var myBarList: MutableList<IngredientListItem>? = mutableListOf(IngredientListItem(emptyBarString))
    var nameMyBarList = "MyBarList"
    val gson = Gson()
    val favoriteCocktailsSet = mutableSetOf<String>()

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

    fun addRecipeToFavorites(recipe : Recipe) {
        favoriteCocktailsSet.add(recipe.idDrink)
    }

    fun removeRecipeFromFavorites(recipe : Recipe) {
        favoriteCocktailsSet.remove(recipe.idDrink)
    }

    fun isRecipeInFavorites(recipe: Recipe) : Boolean {
        return favoriteCocktailsSet.contains(recipe.idDrink)
    }
/*
    fun isIngredientInMyBar(ingredientName : String) : Boolean {
        return myBarList.any{ingredientListItem -> ingredientListItem.strIngredient1  == ingredientName}
    }

    fun getNumberOfGivenIngredientsInMyBar(ingredientsList : MutableList<String>) : Int {
        val filteredList= myBarList.filter {ingredientListItem ->   ingredientsList.contains(ingredientListItem.strIngredient1)}
        return filteredList.size
    }*/
}