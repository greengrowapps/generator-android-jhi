package <%= packageName %>.core.counters

import <%= packageName %>.core.cache.CombinedCache

class CounterService(private val resource: CounterRestResource, private val cache: CombinedCache) {

    companion object {
        private const val CACHE_KEY = "get:counter:list"
    }

    fun readList(useCache: Boolean, success: (List<CounterDto>) -> Unit, error: (statusCode: Int, response: String) -> Unit) : List<CounterDto>{

        var items : Array<CounterDto>? = null
        if(useCache) {
            items = cache.load(CACHE_KEY, java.lang.reflect.Array.newInstance(CounterDto::class.java, 0).javaClass) as? Array<CounterDto>?
        }
        resource.readList({list -> cache.save(CACHE_KEY,list); success(list) },error)

        if(items!=null){
            return items.toList()
        }
        return ArrayList()
    }
}
