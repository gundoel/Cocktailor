package ch.zhaw.ma20.cocktailor

import APIController
import ServiceVolley
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.zhaw.ma20.cocktailor.model.Ingredients
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.beust.klaxon.Klaxon

class CacheInitializer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val service = ServiceVolley()
        val apiController = APIController(service)
        val path = "list.php?i=list"

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

}