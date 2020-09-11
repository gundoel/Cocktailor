package ch.zhaw.ma20.cocktailor.remote

/**
 * https://www.varvet.com/blog/kotlin-with-volley/ adapted to work with GET and StringRequest
 */

interface ServiceInterface {
    fun get(path: String, completionHandler: (response: String?) -> Unit)
}