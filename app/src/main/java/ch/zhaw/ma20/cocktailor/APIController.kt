import ch.zhaw.ma20.cocktailor.ServiceInterface

/**
 * https://www.varvet.com/blog/kotlin-with-volley/ adapted to work with GET and StringRequest
 */

class APIController constructor(serviceInjection: ServiceInterface): ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun get(path: String, completionHandler: (response: String?) -> Unit) {
        service.get(path, completionHandler)
    }
}