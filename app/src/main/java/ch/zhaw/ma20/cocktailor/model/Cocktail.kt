package ch.zhaw.ma20.cocktailor.model

class Cocktail(val strDrink : String, val strDrinkThumb : String, val idDrink : String, var missingIngredients : Int = 0, var availableIngredients : Int = 0) {

    fun setIngredientNumbers(ingredientsList: MutableList<String>) {
        val availableIngredientsMyBar =
            RemoteDataCache.getNumberOfGivenIngredientsInMyBar(
                ingredientsList
            )
        availableIngredients = availableIngredientsMyBar
        missingIngredients = ingredientsList.size - availableIngredientsMyBar
    }

    override fun hashCode(): Int {
        var result = 17
        result = 37 * result + idDrink.hashCode()
        return 37 * result
    }

    override fun equals(other: Any?) : Boolean {
        if (this === other) {
            return true
        }
        if (other !is Cocktail) {
            return false
        }
        val other: Cocktail = other
        return idDrink == other.idDrink
    }
}