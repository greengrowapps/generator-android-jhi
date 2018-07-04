package <%= packageName %>

import android.app.Application
import android.content.Context
import com.greengrowapps.ggarest.GgaRest
import com.greengrowapps.jhiusers.JhiUsers
import com.greengrowapps.jhiusers.JhiUsersImpl
import <%= packageName %>.core.CustomSerializer
import <%= packageName %>.core.Core
import <%= packageName %>.core.config.CoreConfiguration
<% if(websockets) { %>
import <%= packageName %>.core.messaging.Messager
import <%= packageName %>.core.messaging.MessagerGetListener
import <%= packageName %>.core.messaging.MessagerFactory
import <%= packageName %>.core.messaging.StrompMessagerFactory
<% } %>
class MyApplication : Application() {

    private lateinit var jhiUsers: JhiUsers

    private lateinit var core: Core

    private lateinit var config: CoreConfiguration

<% if(websockets) { %>
    private var messagerFactory: MessagerFactory? = null
<% } %>

    override fun onCreate() {
        super.onCreate()

        GgaRest.init(this)
        GgaRest.setSerializer(CustomSerializer())

        if(BuildConfig.DEBUG){
            config = CoreConfiguration("<%= developmentUrl %>")
        }
        else{
            config = CoreConfiguration("<%= productionUrl %>")
        }

        jhiUsers = JhiUsersImpl.with(this,config.serverUrl,true,getSharedPreferences("JhiUsers", Context.MODE_PRIVATE))
        core = Core(jhiUsers,config,getSharedPreferences("Core", Context.MODE_PRIVATE),CustomSerializer())
<% if(websockets) { %>
        messagerFactory = StrompMessagerFactory(config.serverUrl) { val token = jhiUsers.authToken; if (token.isNullOrEmpty()) { null } else {token}  }
<% } %>
    }

    fun getJhiUsers() : JhiUsers{
        return jhiUsers
    }

    fun getCore() : Core{
        return core
    }
<% if(websockets) { %>
    fun getMessager(listener: MessagerGetListener){
      messagerFactory?.instance(listener)
    }
<% } %>
}
