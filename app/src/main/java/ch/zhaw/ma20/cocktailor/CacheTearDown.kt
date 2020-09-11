package ch.zhaw.ma20.cocktailor

import android.content.Context
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache

class CacheTearDown {

    fun persistIngredientList(list: MutableList<Ingredient>?, listName: String) {
        val settings = Cocktailor.applicationContext().getSharedPreferences(listName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        val jsonString : String = RemoteDataCache.gson.toJson(list)
        editor.putString(listName, jsonString)
        editor.apply()
    }

    fun persistCocktailList(list: MutableList<Cocktail>?, listName: String) {
        val settings = Cocktailor.applicationContext().getSharedPreferences(listName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        val jsonString : String = RemoteDataCache.gson.toJson(list)
        editor.putString(listName, jsonString)
        editor.apply()
    }
}