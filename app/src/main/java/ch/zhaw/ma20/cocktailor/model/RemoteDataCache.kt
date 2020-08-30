package ch.zhaw.ma20.cocktailor.model

object RemoteDataCache {
    var ingredientsList = mutableListOf<IngredientListItem>()
    var cocktailList = mutableListOf<Cocktail>()
    val selectedItems = mutableSetOf<String>()

    fun addIngredientsList(list : MutableList<IngredientListItem>) {
        // sort alphabetically
        list.sortBy { it.strIngredient1.toString() }
        ingredientsList = list;
    }

    fun addCocktailList(list : MutableList<Cocktail>) {
        cocktailList = list;
    }
}