package ch.zhaw.ma20.cocktailor.model

class RecipeIngredient(val ingredient : String, val measure : String?) {
    val isIngredientInMyBar : Boolean = RemoteDataCache.isIngredientInMyBar(ingredient)
}