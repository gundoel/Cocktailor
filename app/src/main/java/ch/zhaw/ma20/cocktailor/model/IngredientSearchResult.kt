package ch.zhaw.ma20.cocktailor.model

/**
 * Represents a IngredientSearchResult. This is just a list of Cocktails as result of parsing e.g. https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list
 */
class IngredientSearchResult(val drinks: MutableList<Ingredient>)