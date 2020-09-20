package ch.zhaw.ma20.cocktailor.model

import ch.zhaw.ma20.cocktailor.PersistenceHandler

/**
 * Centralized cache for all data, thats need to be cached. Singleton, that can be accessed from anywhere in app.
 * Provides some useful functions (with simple business logic) to access cache data.
 **/
object DataCache {
    var ingredientsList = mutableListOf<Ingredient>()
    val selectedItemsSet = mutableSetOf<String>()
    var lastCocktailSearchResultList = mutableListOf<Cocktail>()
    var lastRecipeSearchResultMap = mutableMapOf<String, Recipe>()
    // access only allowed with provided methods, since updating SharedPreferences is needed
    private var myBarList = mutableListOf<Ingredient>()
    private var favoriteCocktailsList = mutableListOf<Cocktail>()

    fun addIngredientsList(list : MutableList<Ingredient>) {
        // sort alphabetically
        list.sortBy { it.strIngredient1}
        ingredientsList = list
    }

    fun addLastCocktailSearchResult(list : MutableList<Cocktail>) {
        lastCocktailSearchResultList = list
    }

    fun addLastRecipeSearchResult(list : MutableList<Recipe>) {
        for(item in list) {
            lastRecipeSearchResultMap[item.idDrink] = item
        }
    }

    fun addMyBarList(list : MutableList<Ingredient>) {
        myBarList = list
        // Store changes in case app crashes or is re-installed in emulator
        PersistenceHandler.persistIngredientList(myBarList)
    }

    fun addIngredientToMyBar(ingredient : Ingredient) {
        myBarList.add(ingredient)
        // Store changes in case app crashes or is re-installed in emulator
        PersistenceHandler.persistIngredientList(myBarList)
    }

    fun removeIngredientFromMyBar(ingredient : Ingredient) {
        myBarList.remove(ingredient)
        // Store changes in case app crashes or is re-installed in emulator
        PersistenceHandler.persistIngredientList(myBarList)
    }

    fun getMyBarList() : MutableList<Ingredient> {
        return myBarList
    }

    fun isIngredientInMyBar(ingredientName : String) : Boolean {
        return myBarList.any { ingredientListItem -> ingredientListItem.strIngredient1  == ingredientName}
    }

    fun getNumberOfGivenIngredientsInMyBar(ingredientsList: MutableList<String>) : Int {
        val filteredList= myBarList.filter {ingredientListItem ->   ingredientsList.contains(ingredientListItem.strIngredient1)}
        return filteredList.size
    }

    fun addFavoriteCocktailList(list : MutableList<Cocktail>) {
        favoriteCocktailsList = list
        // Store changes in case app crashes or is re-installed in emulator
        PersistenceHandler.persistCocktailList(favoriteCocktailsList)
    }

    fun addRecipeToFavorites(recipe : Recipe) {
        favoriteCocktailsList.add(Cocktail(recipe.strDrink, recipe.strDrinkThumb, recipe.idDrink))
        // Store changes in case app crashes or is re-installed in emulator
        PersistenceHandler.persistCocktailList(favoriteCocktailsList)
    }

    fun removeRecipeFromFavorites(recipe : Recipe) {
        favoriteCocktailsList.remove(Cocktail(recipe.strDrink, recipe.strDrinkThumb, recipe.idDrink))
        // Store changes in case app crashes or is re-installed in emulator
        PersistenceHandler.persistCocktailList(favoriteCocktailsList)
    }

    fun isRecipeInFavorites(recipe: Recipe) : Boolean {
        return favoriteCocktailsList.contains(Cocktail(recipe.strDrink, recipe.strDrinkThumb, recipe.idDrink))
    }

    fun getFavorites() : MutableList<Cocktail> {
        return favoriteCocktailsList
    }

}