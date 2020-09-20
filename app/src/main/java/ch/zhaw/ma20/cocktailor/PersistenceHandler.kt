package ch.zhaw.ma20.cocktailor

import android.content.Context
import ch.zhaw.ma20.cocktailor.appconst.PersistentData
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.Ingredient
import com.google.gson.Gson

class PersistenceHandler {
    companion object {
        private val gson = Gson()

        fun persistIngredientList(list: MutableList<Ingredient>) {
            val settings = Cocktailor.applicationContext().getSharedPreferences(PersistentData.MY_BAR_LIST, Context.MODE_PRIVATE)
            val editor = settings.edit()
            val jsonString : String = gson.toJson(list)
            editor.putString(PersistentData.MY_BAR_LIST, jsonString)
            editor.apply()
            editor.commit()
        }

        fun persistCocktailList(list: MutableList<Cocktail>?) {
            val settings = Cocktailor.applicationContext().getSharedPreferences(PersistentData.MY_FAVORITES_LIST, Context.MODE_PRIVATE)
            val editor = settings.edit()
            val jsonString : String = gson.toJson(list)
            editor.putString(PersistentData.MY_FAVORITES_LIST, jsonString)
            editor.apply()
            editor.commit()
        }
    }
}