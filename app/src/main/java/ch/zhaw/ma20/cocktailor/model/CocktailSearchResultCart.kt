package ch.zhaw.ma20.cocktailor.model

import java.util.concurrent.atomic.AtomicInteger

class CocktailSearchResultCart(val pendingRequests: AtomicInteger, val hasRequestFailed : Boolean) {
    val cocktailSearchResultList = mutableListOf<MutableMap<String, Cocktail>>()

    fun addCocktailMap(cocktailMap : MutableMap<String, Cocktail>) {
        cocktailSearchResultList.add(cocktailMap)
    }

    fun getCocktailsAND() : MutableList<Cocktail>{
        //TODO return AND not OR
        val resultList = mutableListOf<Cocktail>()
        for(item in cocktailSearchResultList) {
            resultList.addAll(item.values)
        }
        return resultList
    }

    fun getCocktailsOR() : MutableList<Cocktail>{
        val resultList = mutableListOf<Cocktail>()
        for(item in cocktailSearchResultList) {
            resultList.addAll(item.values)
        }
        return resultList
    }

}