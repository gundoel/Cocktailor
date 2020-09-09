package ch.zhaw.ma20.cocktailor.model

import com.google.gson.Gson

object RemoteDataCache {
    var ingredientsList = mutableListOf<Ingredient>()
    val selectedItemsSet = mutableSetOf<String>()
    var lastCocktailSearchResultList = mutableListOf<Cocktail>()
    var lastRecipeSearchResultMap = mutableMapOf<String, Recipe>()
    var myBarList: MutableList<Ingredient> = mutableListOf()
    val gson = Gson()
    val favoriteCocktailsList = mutableListOf<Cocktail>()

    fun addIngredientsList(list : MutableList<Ingredient>) {
        // sort alphabetically
        list.sortBy { it.strIngredient1.toString() }
        ingredientsList = list;
    }

    fun addLastCocktailSearchResult(list : MutableList<Cocktail>) {
        lastCocktailSearchResultList = list;
    }

    fun addLastRecipeSearchResult(list : MutableList<Recipe>) {
        for(item in list) {
            lastRecipeSearchResultMap.put(item.idDrink, item)
        }
    }

    fun addMyBarList(list : MutableList<Ingredient>) {
        myBarList = list
    }

    fun addRecipeToFavorites(recipe : Recipe) {
        favoriteCocktailsList.add(Cocktail(recipe.strDrink, recipe.strDrinkThumb, recipe.idDrink))
    }

    fun removeRecipeFromFavorites(recipe : Recipe) {
        favoriteCocktailsList.remove(Cocktail(recipe.strDrink, recipe.strDrinkThumb, recipe.idDrink))
    }

    fun isRecipeInFavorites(recipe: Recipe) : Boolean {
        return favoriteCocktailsList.contains(Cocktail(recipe.strDrink, recipe.strDrinkThumb, recipe.idDrink))
    }

    fun isIngredientInMyBar(ingredientName : String) : Boolean {
        return myBarList.any{ingredientListItem -> ingredientListItem.strIngredient1  == ingredientName}
    }

    fun getNumberOfGivenIngredientsInMyBar(ingredientsList: MutableList<String?>) : Int {
        val filteredList= myBarList.filter {ingredientListItem ->   ingredientsList.contains(ingredientListItem.strIngredient1)}
        return filteredList.size
    }
}