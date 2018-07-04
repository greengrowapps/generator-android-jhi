package <%= packageName %>.core.messaging

import android.util.Log
import com.greengrowapps.ggarest.GgaRest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.LifecycleEvent
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.client.StompClient
import java.util.HashMap

class StrompMessager(private val client: StompClient) : Messager {
    override fun send(topic: String, message: String) {
        try{
            client.send(topic,message).subscribe()
        }
        catch (e:Exception){
            Log.w("StrompMessager",e)
        }
    }
}

class StrompMessagerFactory(private val serverUrl: String, private val accessTokenProvider: () -> String?) : MessagerFactory{

    private var instance : StrompMessager? = null

    override fun instance(listener: MessagerGetListener) {

        if (instance != null) {
            listener.onMessagerGet(instance!!)
            return
        }

        val token = accessTokenProvider()

        if (token == null) {
            listener.onMessagerGetError("No access token")
            return
        }

        configureStropClient(token,listener)
    }

    private fun configureStropClient(token: String, listener: MessagerGetListener) {
        val connectionHeaders = HashMap<String, String>()

        val socketUrl = serverUrl.replace("http://","ws://").replace("https://","ws://")

        val mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "${socketUrl}/websocket/tracker/websocket?access_token=$token", connectionHeaders)

        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t -> t.message?.let { listener.onMessagerGetError(it) } }
                .doOnCancel{ listener.onMessagerGetError("Stromp onnection cancelled")}
                .doOnComplete { Log.d("StrompMessager","Completed!") }
                .subscribe { lifecycleEvent ->
                    when (lifecycleEvent.type) {
                        LifecycleEvent.Type.OPENED -> {
                            instance = StrompMessager(mStompClient)
                        }
                        LifecycleEvent.Type.ERROR -> {
                            listener.onMessagerGetError("Stomp connection error")
                        }
                        LifecycleEvent.Type.CLOSED -> {
                            listener.onMessagerGetError("Stomp connection closed")
                        }
                        null -> listener.onMessagerGetError("Unknown")
                    }
                }


        mStompClient.connect()
    }

}
