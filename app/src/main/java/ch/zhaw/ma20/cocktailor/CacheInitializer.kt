package ch.zhaw.ma20.cocktailor

import ch.zhaw.ma20.cocktailor.remote.APIController
import ch.zhaw.ma20.cocktailor.remote.ServiceVolley
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.zhaw.ma20.cocktailor.appconst.PersistentData
import ch.zhaw.ma20.cocktailor.model.Cocktail
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
        readCocktailList(PersistentData.MY_FAVORITES_LIST)?.let { RemoteDataCache.addFavoriteCocktailList(it)}
        // cache ingredients
        apiController.get(path) { response ->
            val ingredients = response?.let { Klaxon().parse<Ingredients>(it) }
            RemoteDataCache.addIngredientsList(ingredients!!.drinks)
            startMainActivity()
        }
    }

    private fun startMainActivity() {
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

    private fun readCocktailList(listName : String) : MutableList<Cocktail>? {
        val settings = Cocktailor.applicationContext().getSharedPreferences(listName, Context.MODE_PRIVATE)
        val jsonString : String? = settings.getString(listName, "")
        val cocktailListType = object : TypeToken<MutableList<Cocktail>>() {}.type
        return RemoteDataCache.gson.fromJson(jsonString, cocktailListType)
    }
}