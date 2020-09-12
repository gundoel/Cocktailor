package ch.zhaw.ma20.cocktailor.model

/**
 * Represents a CocktailSearchResult. This is just a list of Cocktails as result of parsing e.g. https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=Gin
 */
class CocktailSearchResult(val drinks: MutableList<Cocktail>)