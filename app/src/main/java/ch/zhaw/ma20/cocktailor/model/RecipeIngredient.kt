package ch.zhaw.ma20.cocktailor.model

/**
 * Represents an ingredient of a recipe and inforrmation if ingredient is available in my bar.
 * Therefore state in MyBar is requested from my bar when creating instance.
 */
class RecipeIngredient(val ingredient : String, val measure : String?) {
    val isIngredientInMyBar : Boolean = RemoteDataCache.isIngredientInMyBar(ingredient)
}