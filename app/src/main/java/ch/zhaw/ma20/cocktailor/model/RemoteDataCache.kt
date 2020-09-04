package ch.zhaw.ma20.cocktailor.model

object RemoteDataCache {
    var ingredientsList = mutableListOf<IngredientListItem>()
    val selectedItemsSet = mutableSetOf<String>()
    var lastCocktailSearchResultList = mutableListOf<Cocktail>()

    fun addIngredientsList(list : MutableList<IngredientListItem>) {
        // sort alphabetically
        list.sortBy { it.strIngredient1.toString() }
        ingredientsList = list;
    }

    fun addLastCocktailSearchResultList(list : MutableList<Cocktail>) {
        lastCocktailSearchResultList = list;
    }
}