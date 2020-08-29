package ch.zhaw.ma20.cocktailor.model

object RemoteDataCache {
    var ingredientsList = mutableListOf<IngredientListItem>()
    var cocktailList = mutableListOf<Cocktail>()

    fun addIngredientsList(list : MutableList<IngredientListItem>) {
        ingredientsList = list;
    }

    fun addCocktailList(list : MutableList<Cocktail>) {
        cocktailList = list;
    }
}