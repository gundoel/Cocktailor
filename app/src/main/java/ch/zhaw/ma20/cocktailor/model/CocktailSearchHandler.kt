package ch.zhaw.ma20.cocktailor.model

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.beust.klaxon.Klaxon

class CocktailSearchHandler() {

    companion object {
        fun executeCocktailSearch(context: Context, searchKeySet : MutableSet<String>, connector : Connector = Connector.OR) {
            val requestQueue = Volley.newRequestQueue(context)
            for(item in searchKeySet) {
                var ingredient = item.toString()
                val cocktailsUrl = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=$ingredient"
                val request = StringRequest(
                    Request.Method.GET, cocktailsUrl,
                    Response.Listener<String> { response ->
                        val ingredients = Klaxon().parse<CocktailSearchResult>(response)
                        //TODO collect results in list and return result list (Callback when last result is processed?)

                    },
                    Response.ErrorListener {
                        Log.e("VOLLEY ERROR:", it.toString())
                    })
                requestQueue.add(request)
            }}
    }



}


/*var ingredient = "df"
*/