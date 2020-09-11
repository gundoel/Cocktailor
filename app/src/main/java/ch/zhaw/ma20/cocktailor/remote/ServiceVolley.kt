package ch.zhaw.ma20.cocktailor.remote

import android.util.Log
import ch.zhaw.ma20.cocktailor.Cocktailor
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.StringRequest

/**
 * https://www.varvet.com/blog/kotlin-with-volley/ adapted to work with GET and StringRequest
 */

class ServiceVolley : ServiceInterface {
    val TAG = ServiceVolley::class.java.simpleName
    val basePath = "https://www.thecocktaildb.com/api/json/v1/1/"

    override fun get(path: String, completionHandler: (response: String?) -> Unit) {
        val request = StringRequest(
            Request.Method.GET, basePath + path,
            Response.Listener<String> { response ->
                Log.d(TAG, "/get request OK! Response: $response")
                completionHandler(response)
            },
            Response.ErrorListener { error ->
                VolleyLog.e(TAG, "/get request fail! Error: ${error.message}")
                completionHandler(null)
            })
        Cocktailor.instance?.addToRequestQueue(request, TAG)
    }
}