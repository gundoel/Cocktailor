package ch.zhaw.ma20.cocktailor

import APIController
import ServiceVolley
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.zhaw.ma20.cocktailor.appconst.PersistentData
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.Ingredients
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.beust.klaxon.Klaxon
import com.google.gson.reflect.TypeToken

class CacheInitializer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = ServiceVolley()
        val apiController = APIController(service)
        val path = "list.php?i=list"

        // read persistent data from storage
        readIngredientList(PersistentData.MY_BAR_LIST)?.let { RemoteDataCache.addMyBarList(it) }
        // cache ingredients
        apiController.get(path) { response ->
            val ingredients = response?.let { Klaxon().parse<Ingredients>(it) }
            RemoteDataCache.addIngredientsList(ingredients!!.drinks)
            startMainActivity()
        }
    }

    fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun readIngredientList(listName : String) : MutableList<Ingredient>? {
        val settings = Cocktailor.applicationContext().getSharedPreferences(listName, Context.MODE_PRIVATE)
        val jsonString : String? = settings.getString(listName, "")
        val ingredientListType = object : TypeToken<MutableList<Ingredient>>() {}.type
        return RemoteDataCache.gson.fromJson(jsonString, ingredientListType)
    }
}