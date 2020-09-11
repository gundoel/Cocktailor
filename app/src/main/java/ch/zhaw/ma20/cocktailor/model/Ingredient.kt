package ch.zhaw.ma20.cocktailor.model

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