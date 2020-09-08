package ch.zhaw.ma20.cocktailor

import APIController
import ServiceVolley
import ch.zhaw.ma20.cocktailor.model.Cocktail
import java.io.File

class ThumbHandler {
    val service = ServiceVolley()
    val apiController = APIController(service)

    fun storeSingleThumb(cocktail: Cocktail) {
        var thumbName = cocktail.idDrink + ".jpg"
        if (!(File(thumbName).exists())) {
            var picturePath = cocktail.strDrinkThumb + "/preview"
            apiController.get(picturePath) {
                File(Cocktailor.applicationContext().filesDir, thumbName)
            }
        }
    }

    fun storeMultipleThumbs(cocktailList: MutableList<Cocktail>) {
        for (cocktail in cocktailList) {
            storeSingleThumb(cocktail)
        }
    }
}
