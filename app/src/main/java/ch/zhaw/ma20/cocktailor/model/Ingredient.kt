package ch.zhaw.ma20.cocktailor.model

/**
 * Represents an ingredient. This is just the name as result of parsing e.g. https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list
 */
class Ingredient(val strIngredient1: String) {

    override fun hashCode(): Int {
        var result = 17
        result = 37 * result + strIngredient1.hashCode()
        return 37 * result
    }

    override fun equals(other: Any?) : Boolean {
        if (this === other) {
            return true
        }
        if (other !is Ingredient) {
            return false
        }
        val other: Ingredient = other
        return strIngredient1 == other.strIngredient1
    }

}