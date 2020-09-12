package ch.zhaw.ma20.cocktailor.model

/**
 * Represents a RecipeSearchResult. This is just a list of Recipes as result of parsing e.g. e.g. https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=15300
 */
class RecipeSearchResult(val drinks: MutableList<Recipe>)