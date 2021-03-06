package ch.zhaw.ma20.cocktailor

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

/**
 * Provides access to application Context from anywhere and has centralized volley queue for all volley requests of Cocktailor
 * https://www.varvet.com/blog/kotlin-with-volley/ adapted to work with GET and StringRequest
 */

class Cocktailor : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        val context: Context = applicationContext()
    }

    private val requestQueue: RequestQueue? = null
        get() {
            if (field == null) {
                return Volley.newRequestQueue(applicationContext)
            }
            return field
        }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    companion object {
        private val TAG = Cocktailor::class.java.simpleName

        @get:Synchronized
        var instance: Cocktailor? = null
            private set

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}