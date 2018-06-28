package <%= packageName %>.core.counters

import com.greengrowapps.ggarest.Webservice
import com.greengrowapps.ggarest.listeners.OnListResponseListener
import com.greengrowapps.ggarest.listeners.OnObjResponseListener
import com.greengrowapps.ggarest.listeners.OnResponseListener
import com.greengrowapps.jhiusers.JhiUsers

class CounterRestResource(private val url:String,private val webservice: Webservice,private val users: JhiUsers){

    companion object {
        const val resourceUrl = "api/counters"
    }

    fun readList(success: (List<CounterDto>) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.get("$url/$resourceUrl")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .onSuccess(CounterDto::class.java, OnListResponseListener{ code, items, fullResponse -> success(items) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun readOne(id:Int, success: (CounterDto) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.get("$url/$resourceUrl/$id")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .onSuccess(CounterDto::class.java, OnObjResponseListener{ code, item, fullResponse -> success(item) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun save(toSave: CounterDto, success: (CounterDto) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.post("$url/$resourceUrl")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .withBody(toSave)
                .onSuccess(CounterDto::class.java, OnObjResponseListener{ code, item, fullResponse -> success(item) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun update(toUpdate: CounterDto, success: (CounterDto) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.put("$url/$resourceUrl")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .withBody(toUpdate)
                .onSuccess(CounterDto::class.java, OnObjResponseListener{ code, item, fullResponse -> success(item) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
    fun delete(id: Int, success: (CounterDto) -> Unit, error: (statusCode:Int, response:String) -> Unit){

        webservice.delete("$url/$resourceUrl/$id")
                .addHeader("Content-Type","application/json;charset=UTF-8")
                .addHeader("Authorization", "Bearer ${users.authToken}")
                .onSuccess(CounterDto::class.java, OnObjResponseListener{ code, item, fullResponse -> success(item) })
                .onOther(OnResponseListener{code, fullResponse, e -> error(code, fullResponse.toString()) })
                .execute()
    }
}
