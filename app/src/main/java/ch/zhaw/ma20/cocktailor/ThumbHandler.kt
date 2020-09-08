package ch.zhaw.ma20.cocktailor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import ch.zhaw.ma20.cocktailor.model.Cocktail
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

object ThumbHandler {

    private fun storeSingleThumb(cocktail: Cocktail) {
        val thumbURL = cocktail.strDrinkThumb
        var bmp: Bitmap? = null
        var fileName = cocktail.idDrink
        try {
            val `in`: InputStream = URL(thumbURL).openStream()
            bmp = BitmapFactory.decodeStream(`in`)
        } catch (e: Exception) {
            e.message?.let { Log.e("Error", it) }
            e.printStackTrace()
        }
        if (bmp != null) {
            saveImage(Cocktailor.applicationContext(), bmp, fileName)
        }
    }

    fun storeMultipleThumbs(cocktailList: MutableList<Cocktail>) {
        for (cocktail in cocktailList) {
            storeSingleThumb(cocktail)
        }
    }

    private fun saveImage(context: Context, bmp: Bitmap, imageName: String?) {
        val foStream: FileOutputStream
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE)
            bmp.compress(Bitmap.CompressFormat.PNG, 100, foStream)
            foStream.close()
        } catch (e: java.lang.Exception) {
            Log.d("saveImage", "Exception 2, Something went wrong!")
            e.printStackTrace()
        }
    }

    fun loadImageBitmap(context: Context, imageName: String?): Bitmap? {
        var bitmap: Bitmap? = null
        val fiStream: FileInputStream
        try {
            fiStream = context.openFileInput(imageName)
            bitmap = BitmapFactory.decodeStream(fiStream)
            fiStream.close()
        } catch (e: java.lang.Exception) {
            Log.d("saveImage", "Exception 3, Something went wrong!")
            e.printStackTrace()
        }
        return bitmap
    }
}





