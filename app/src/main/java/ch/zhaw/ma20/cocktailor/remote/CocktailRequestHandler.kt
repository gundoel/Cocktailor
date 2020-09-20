package ch.zhaw.ma20.cocktailor.remote

import ch.zhaw.ma20.cocktailor.appconst.Connector
import ch.zhaw.ma20.cocktailor.model.Cocktail
import ch.zhaw.ma20.cocktailor.model.CocktailSearchResult
import ch.zhaw.ma20.cocktailor.model.DataCache
import com.beust.klaxon.Klaxon
import java.util.concurrent.atomic.AtomicInteger
import kotlin.collections.mutableMapOf as mutableMapOf

class CocktailRequestHandler {
    companion object {
        private lateinit var allRequestResults: MutableList<MutableMap<String,Cocktail>>
        private lateinit var allRequestResultsUnique : MutableMap<String, Cocktail>
        private val service = ServiceVolley()
        private val apiController = APIController(service)

        /**
         * Returns and caches Cocktails for given set of ingredients, connector (OR = all results, AND = only cocktails with all of given ingedients).
         * Result are delivered, when all requests finished. All requests are collected and callback (completion handler) is executed, after all results
         * have been delivered. Results are being cached. Caller does not neet to handle caching and can access data from Cache.
         */
        fun getAndCacheCocktailsByIngredients(
            ingredientIdSet: MutableSet<String>,
            connector: Connector = Connector.OR,
            completionHandler: (response: MutableList<Cocktail>?) -> Unit
        ) {
            allRequestResults = mutableListOf<MutableMap<String, Cocktail>>()
            allRequestResultsUnique = mutableMapOf<String, Cocktail>()
            val pendingRequests = AtomicInteger(ingredientIdSet.size)
            for (item in ingredientIdSet) {
                val path = "filter.php?i=$item"

                apiController.get(path) { response ->
                    if (response != null) {
                        val cocktails = Klaxon().parse<CocktailSearchResult>(response)
                        if (cocktails != null) {
                            allRequestResults.add(
                                cocktails.drinks.map { it.idDrink to it }.toMap().toMutableMap()
                            )
                        }
                        // all cocktail requests done
                        if (pendingRequests.decrementAndGet() == 0) {
                            val cocktailList = mutableListOf<Cocktail>()
                            if (connector == Connector.OR) {
                                cocktailList.addAll(getCocktailsOR())
                            }
                            // AND
                            else {
                                cocktailList.addAll(getCocktailsAND())
                            }
                            DataCache.addLastCocktailSearchResult(cocktailList)
                            completionHandler(cocktailList)
                        }
                    } else {
                        // at least one request failed. return null to handle in calling fragment.
                        completionHandler(null)
                    }
                }
            }
        }

        /**
         * Gets all Cocktails from allRequestResults, which were contained in all of search results.
         * allRequestResults is a List of maps (one for each searchresult)
         * 1. add all search results to allRequestResultsUnique map (duplicates are removed, since the have same key).
         *    allRequestResultsUnique contains on occurence of each result for easier access.
         * 2. create tempResultList as Set. This Set is initialized with first search result and afterwards reduced by intersection with following search results.
         *    In the end, tempResultList contains all keys, which are common in all search results.
         * 3. Add all items from allRequestResultsUnique, which are in tempResultList, and add them to result list.
         */
        private fun getCocktailsAND(): MutableList<Cocktail> {
            // put all cocktails in map for easy filtering
            for (item in allRequestResults) {
                for (item in item.values) {
                    allRequestResultsUnique[item.idDrink] = item
                }
            }
            //initialize with keys of first map
            var tempResultList: Set<String> = allRequestResults[0].keys
            val resultList = mutableListOf<Cocktail>()
            for (item in allRequestResults) {
                tempResultList = tempResultList.intersect(item.keys)
            }
            for (item in tempResultList) {
                allRequestResultsUnique[item]?.let { resultList.add(it) }
            }
            return resultList
        }

        /**
         * Gets all Cocktails from allRequestResults, which were contained at least in one of search results. OR is simple (compared to AND).
         * Only duplicates need to be removed: this is done with a map, since cocktails with same key are going to be unique in map)
         */
        private fun getCocktailsOR(): MutableList<Cocktail> {
            val resultMap = mutableMapOf<String,Cocktail>()
            for (item in allRequestResults) {
                resultMap.putAll(item)
            }
            return resultMap.values.toMutableList()
        }
    }
}
