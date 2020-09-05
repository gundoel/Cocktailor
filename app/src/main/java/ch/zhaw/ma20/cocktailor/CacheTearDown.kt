package ch.zhaw.ma20.cocktailor

import android.content.Context
import ch.zhaw.ma20.cocktailor.model.IngredientListItem
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache

class CacheTearDown {

    fun persistList(list: MutableList<IngredientListItem>?, listName: String) {
        val settings = Cocktailor.applicationContext().getSharedPreferences(listName, Context.MODE_PRIVATE)
        val editor = settings.edit()
        val jsonString : String = RemoteDataCache.gson.toJson(list)
        editor.putString(listName, jsonString)
        editor.commit()
    }
}