package ch.zhaw.ma20.cocktailor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.zhaw.ma20.cocktailor.model.Ingredients
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon


class CacheInitializer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val INGREDIENTSURL = "https://www.thecocktaildb.com/api/json/v1/1/list.php?i=list"
        // Ingredients-Request gleich beim Start absetzen
        val requestQueue = Volley.newRequestQueue(this)
        val request = StringRequest(
            Request.Method.GET, INGREDIENTSURL,
            Response.Listener<String> { response ->
                val ingredients = Klaxon().parse<Ingredients>(response)
                RemoteDataCache.addIngredientsList(ingredients!!.drinks)
                startMainActivity()
            },
            Response.ErrorListener {
                Log.e("VOLLEY ERROR:", it.toString())
            })
        requestQueue.add(request)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}