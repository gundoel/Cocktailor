package ch.zhaw.ma20.cocktailor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.zhaw.ma20.cocktailor.appconst.PersistentData
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.DataCache
import ch.zhaw.ma20.cocktailor.model.Ingredient
import ch.zhaw.ma20.cocktailor.model.IngredientSearchResult
import ch.zhaw.ma20.cocktailor.remote.APIController
import ch.zhaw.ma20.cocktailor.remote.ServiceVolley
import com.beust.klaxon.Klaxon
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class CacheInitializer : AppCompatActivity() {
    private val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val service = ServiceVolley()
        val apiController = APIController(service)
        val path = "list.php?i=list"

        // read persistent data from storage
        readIngredientList(PersistentData.MY_BAR_LIST)?.let {
            DataCache.addMyBarList(it)
        }
        readCocktailList(PersistentData.MY_FAVORITES_LIST)?.let {
            DataCache.addFavoriteCocktailList(it)
        }
        // cache ingredients
        apiController.get(path) { response ->
            if (response != null) {
                val ingredients = Klaxon().parse<IngredientSearchResult>(response)
                DataCache.addIngredientsList(ingredients!!.drinks)
                startMainActivity()
            }
            else {
                Toast.makeText(
                    Cocktailor.applicationContext(),
                    R.string.unknown_error_caching_ingredients,
                    Toast.LENGTH_LONG
                ).show()
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        finish()
                    }
                }, 2000)

            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun readIngredientList(listName: String) : MutableList<Ingredient>? {
        val settings = Cocktailor.applicationContext().getSharedPreferences(
            listName,
            Context.MODE_PRIVATE
        )
        val jsonString : String? = settings.getString(listName, "")
        val ingredientListType = object : TypeToken<MutableList<Ingredient>>() {}.type
        return gson.fromJson(jsonString, ingredientListType)
    }

    private fun readCocktailList(listName: String) : MutableList<Cocktail>? {
        val settings = Cocktailor.applicationContext().getSharedPreferences(
            listName,
            Context.MODE_PRIVATE
        )
        val jsonString : String? = settings.getString(listName, "")
        val cocktailListType = object : TypeToken<MutableList<Cocktail>>() {}.type
        return gson.fromJson(jsonString, cocktailListType)
    }
}