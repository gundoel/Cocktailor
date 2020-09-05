package ch.zhaw.ma20.cocktailor.model

class Recipe(
    val idDrink: String,
    val strDrink: String,
    val strAlcoholic: String?,
    val strGlass: String?,
    val strInstructions: String?,
    val strDrinkThumb: String?,
    val strIngredient1: String?,
    val strIngredient2: String?,
    val strIngredient3: String?,
    val strIngredient4: String?,
    val strIngredient5: String?,
    val strIngredient6: String?,
    val strIngredient7: String?,
    val strIngredient8: String?,
    val strIngredient9: String?,
    val strIngredient10: String?,
    val strIngredient11: String?,
    val strIngredient12: String?,
    val strIngredient13: String?,
    val strIngredient14: String?,
    val strIngredient15: String?,
    val strMeasure1: String?,
    val strMeasure2: String?,
    val strMeasure3: String?,
    val strMeasure4: String?,
    val strMeasure5: String?,
    val strMeasure6: String?,
    val strMeasure7: String?,
    val strMeasure8: String?,
    val strMeasure9: String?,
    val strMeasure10: String?,
    val strMeasure11: String?,
    val strMeasure12: String?,
    val strMeasure13: String?,
    val strMeasure14: String?,
    val strMeasure15: String?) {

    fun getIngredientsList() : MutableList<RecipeIngredientItem> {
        val ingredientsList = mutableListOf<RecipeIngredientItem>()
        if (strIngredient1 != null) ingredientsList.add(RecipeIngredientItem(strIngredient1,strMeasure1))
        if (strIngredient2 != null) ingredientsList.add(RecipeIngredientItem(strIngredient2,strMeasure2))
        if (strIngredient3 != null) ingredientsList.add(RecipeIngredientItem(strIngredient3,strMeasure3))
        if (strIngredient4 != null) ingredientsList.add(RecipeIngredientItem(strIngredient4,strMeasure4))
        if (strIngredient5 != null) ingredientsList.add(RecipeIngredientItem(strIngredient5,strMeasure5))
        if (strIngredient6 != null) ingredientsList.add(RecipeIngredientItem(strIngredient6,strMeasure6))
        if (strIngredient7 != null) ingredientsList.add(RecipeIngredientItem(strIngredient7,strMeasure7))
        if (strIngredient8 != null) ingredientsList.add(RecipeIngredientItem(strIngredient8,strMeasure8))
        if (strIngredient9 != null) ingredientsList.add(RecipeIngredientItem(strIngredient9,strMeasure9))
        if (strIngredient10 != null) ingredientsList.add(RecipeIngredientItem(strIngredient10,strMeasure10))
        if (strIngredient11 != null) ingredientsList.add(RecipeIngredientItem(strIngredient11,strMeasure11))
        if (strIngredient12 != null) ingredientsList.add(RecipeIngredientItem(strIngredient12,strMeasure12))
        if (strIngredient13 != null) ingredientsList.add(RecipeIngredientItem(strIngredient13,strMeasure13))
        if (strIngredient14 != null) ingredientsList.add(RecipeIngredientItem(strIngredient14,strMeasure14))
        if (strIngredient15 != null) ingredientsList.add(RecipeIngredientItem(strIngredient15,strMeasure15))
        return ingredientsList
    }
}