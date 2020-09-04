package ch.zhaw.ma20.cocktailor.model

class Cocktail(val strDrink : String, val strDrinkThumb : String, val idDrink : String, var missingIngredients : Int = 0, var availableIngredients : Int = 0) {
}