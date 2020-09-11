package ch.zhaw.ma20.cocktailor.remote

import ch.zhaw.ma20.cocktailor.appconst.Connector
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.CocktailSearchResult
import ch.zhaw.ma20.cocktailor.model.RemoteDataCache
import com.beust.klaxon.Klaxon
import java.util.concurrent.atomic.AtomicInteger

class CocktailRequestHandler {
    companion object {
        private val allRequestResults = mutableListOf<MutableMap<String, Cocktail>>()
        private val commonResults = mutableMapOf<String, Cocktail>()
        private var hasRequestFailed: Boolean = false
        private val service = ServiceVolley()
        private val apiController = APIController(service)

        fun getCocktailsByIngredients(
            ingredientIdSet: MutableSet<String>,
            connector: Connector = Connector.OR,
            completionHandler: (response: MutableList<Cocktail>?) -> Unit
        ) {
            val pendingRequests: AtomicInteger = AtomicInteger(ingredientIdSet.size)
            for (item in ingredientIdSet) {
                val ingredient = item
                val path = "filter.php?i=$ingredient"

                apiController.get(path) { response ->
                    if (response != null) {
                        val cocktails = Klaxon().parse<CocktailSearchResult>(response)
                        if (cocktails != null) {
                            allRequestResults.add(
                                cocktails.drinks.map { it.idDrink to it }.toMap().toMutableMap()
                            )
                        }
                        // all cocktail requests done
                        //TODO Error handling if request fails
                        if (pendingRequests.decrementAndGet() == 0) {
                            var cocktailList = mutableListOf<Cocktail>()
                            if (connector == Connector.OR) {
                                cocktailList.addAll(getCocktailsOR())
                            }
                            // AND
                            else {
                                cocktailList.addAll(getCocktailsAND())
                            }
                            RemoteDataCache.addLastCocktailSearchResult(cocktailList)
                            completionHandler(cocktailList)
                        }
                    } else {
                        // TODO what happens if single request fails?
                        completionHandler(null)
                    }
                }
            }
        }

        private fun getCocktailsAND(): MutableList<Cocktail> {
            // put all cocktails in map for easy filtering
            for (item in allRequestResults) {
                for (item in item.values) {
                    commonResults.put(item.idDrink, item)
                }
            }
            //initialize with keys of first map
            var tempResultList: Set<String> = allRequestResults[0].keys
            val resultList = mutableListOf<Cocktail>()
            for (item in allRequestResults) {
                tempResultList = tempResultList.intersect(item.keys)
            }
            for (item in tempResultList) {
                commonResults[item]?.let { resultList.add(it) }
            }
            return resultList
        }

        fun getCocktailsOR(): MutableList<Cocktail> {
            val resultMap = mutableMapOf<String,Cocktail>()
            for (item in allRequestResults) {
                resultMap.putAll(item)
            }
            return resultMap.values.toMutableList()
        }
    }
}
