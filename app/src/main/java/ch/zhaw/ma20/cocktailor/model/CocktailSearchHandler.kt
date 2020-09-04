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
            val resultObjectMap = mutableMapOf<String,Cocktail>()
            var cocktailsWithCommonIngredients = mutableSetOf<String>()
            val requestQueue = Volley.newRequestQueue(context)
            for((index, item) in searchKeySet.withIndex()) {
                var ingredient = item
                val cocktailsUrl = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?i=$ingredient"
                val request = StringRequest(
                    Request.Method.GET, cocktailsUrl,
                    Response.Listener<String> { response ->
                        val cocktails = Klaxon().parse<CocktailSearchResult>(response)
                        val currentCocktailResults = mutableSetOf<String>()
                        // OR
                        if(connector == Connector.OR) {
                            for(item in cocktails!!.drinks) {
                                resultObjectMap[item.idDrink] = item
                                Log.i("COCKTAILRESULT",item.idDrink +" " + item.strDrink + " " + item.strDrinkThumb)
                            }
                        }
                        // AND
                        else {
                            for(item in cocktails!!.drinks) {
                                currentCocktailResults.add(item.idDrink)
                                resultObjectMap[item.idDrink] = item
                                Log.i(
                                    "COCKTAILRESULT",
                                    item.idDrink + " " + item.strDrink + " " + item.strDrinkThumb
                                )
                            }
                            if(index == 0) cocktailsWithCommonIngredients.addAll(currentCocktailResults)
                            val tempResultList = cocktailsWithCommonIngredients.intersect(currentCocktailResults)
                            resultObjectMap.keys.removeAll{ !tempResultList.contains(it) }
                        }
                        for(item in resultObjectMap) {
                            Log.i("COCKTAILRESULTLIST",item.key)
                        }
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