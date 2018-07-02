package <%= packageName %>.core.data.<%= entityNameLower %>

import <%= packageName %>.core.cache.CombinedCache

class <%= entityName %>Service(private val resource: <%= entityName %>RestResource, private val cache: CombinedCache) {

    companion object {
        private const val CACHE_KEY = "get:<%= entityNameLower %>:list"
    }

    fun readList(useCache: Boolean, success: (List<<%= entityName %>Dto>) -> Unit, error: (statusCode: Int, response: String) -> Unit) : List<<%= entityName %>Dto>{

        var items : Array<<%= entityName %>Dto>? = null
        if(useCache) {
            items = cache.load(CACHE_KEY, java.lang.reflect.Array.newInstance(<%= entityName %>Dto::class.java, 0).javaClass) as? Array<<%= entityName %>Dto>?
        }
        resource.readList({list -> cache.save(CACHE_KEY,list); success(list) },error)

        if(items!=null){
            return items.toList()
        }
        return ArrayList()
    }
    fun create(item: <%= entityName %>Dto, success: (<%= entityName %>Dto) -> Unit, error: (statusCode: Int, response: String) -> Unit){
      resource.save(item,success,error)
    }
    fun update(item: <%= entityName %>Dto, success: (<%= entityName %>Dto) -> Unit, error: (statusCode: Int, response: String) -> Unit){
      resource.update(item,success,error)
    }
    fun delete(id: Long?, success: () -> Unit, error: (statusCode: Int, response: String) -> Unit) {
      resource.delete(id?:0, success, error)
    }
}
