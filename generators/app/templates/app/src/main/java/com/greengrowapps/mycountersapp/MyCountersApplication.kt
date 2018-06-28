package <%= packageName %>

import android.app.Application
import android.content.Context
import com.greengrowapps.ggarest.GgaRest
import com.greengrowapps.jhiusers.JhiUsers
import com.greengrowapps.jhiusers.JhiUsersImpl
import <%= packageName %>.core.CustomSerializer
import <%= packageName %>.core.MyCountersCore
import <%= packageName %>.core.config.CoreConfiguration

class MyCountersApplication : Application() {

    private lateinit var jhiUsers: JhiUsers

    private lateinit var core: MyCountersCore

    private lateinit var config: CoreConfiguration

    override fun onCreate() {
        super.onCreate()

        GgaRest.init(this)
        GgaRest.setSerializer(CustomSerializer())

        if(BuildConfig.DEBUG){
            config = CoreConfiguration("http://192.168.1.6:8080")
        }
        else{
            config = CoreConfiguration("https://mycounters.greengrowapps.com")
        }

        jhiUsers = JhiUsersImpl.with(this,config.serverUrl,true,getSharedPreferences("JhiUsers", Context.MODE_PRIVATE))
        core = MyCountersCore(jhiUsers,config,getSharedPreferences("MyCountersCore", Context.MODE_PRIVATE),CustomSerializer())
    }

    fun getJhiUsers() : JhiUsers{
        return jhiUsers
    }

    fun getCore() : MyCountersCore{
        return core
    }
}
