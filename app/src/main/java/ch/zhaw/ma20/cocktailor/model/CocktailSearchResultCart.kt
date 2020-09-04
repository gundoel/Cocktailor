package ch.zhaw.ma20.cocktailor.model

import java.util.concurrent.atomic.AtomicInteger

class CocktailSearchResultCart(val pendingRequests: AtomicInteger, val hasRequestFailed: Boolean) {
    private val cocktailSearchResultList = mutableListOf<MutableMap<String, Cocktail>>()
    private val cocktailSearResultMap = mutableMapOf<String, Cocktail>()

    fun addCocktailMap(cocktailMap: MutableMap<String, Cocktail>) {
        cocktailSearchResultList.add(cocktailMap)
    }

    fun getCocktailsAND(): MutableList<Cocktail> {
        // put all cocktails in map for easy filtering
        for (item in cocktailSearchResultList) {
            for (item in item.values) {
                cocktailSearResultMap.put(item.idDrink, item)
            }
        }
        //initialize with keys of first map
        var tempResultList : Set<String> = cocktailSearchResultList[0].keys
        val resultList = mutableListOf<Cocktail>()
        for (item in cocktailSearchResultList) {
            tempResultList = tempResultList.intersect(item.keys)
        }
        for (item in tempResultList) {
            cocktailSearResultMap[item]?.let { resultList.add(it) }
        }
        return resultList
    }

    fun getCocktailsOR(): MutableList<Cocktail> {
        val resultList = mutableListOf<Cocktail>()
        for (item in cocktailSearchResultList) {
            resultList.addAll(item.values)
        }
        return resultList
    }

}