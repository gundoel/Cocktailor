package ch.zhaw.ma20.cocktailor.model

import java.util.concurrent.atomic.AtomicInteger

class CocktailSearchResultCart(val pendingRequests: AtomicInteger, val hasRequestFailed: Boolean) {
    private val allRequestResults = mutableListOf<MutableMap<String, Cocktail>>()
    private val commonResults = mutableMapOf<String, Cocktail>()

    fun addRequestResult(cocktailMap: MutableMap<String, Cocktail>) {
        allRequestResults.add(cocktailMap)
    }

    fun getCocktailsAND(): MutableList<Cocktail> {
        // put all cocktails in map for easy filtering
        for (item in allRequestResults) {
            for (item in item.values) {
                commonResults.put(item.idDrink, item)
            }
        }
        //initialize with keys of first map
        var tempResultList : Set<String> = allRequestResults[0].keys
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
        val resultList = mutableListOf<Cocktail>()
        for (item in allRequestResults) {
            resultList.addAll(item.values)
        }
        return resultList
    }

}