package <%= packageName %>

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.greengrowapps.jhiusers.JhiUsers
import <%= packageName %>.core.Core
<% if(websockets) { %>
import <%= packageName %>.core.messaging.Messager
import <%= packageName %>.core.messaging.MessagerGetListener
<% } %>
open class BaseActivity : AppCompatActivity()<% if(websockets) { %>, MessagerGetListener<% } %>{

<% if(websockets) { %>
    private var messager: Messager? = null
<% } %>
    fun getJhiUsers() : JhiUsers{
        return ( application as MyApplication).getJhiUsers()
    }
    fun getCore() : Core{
        return ( application as MyApplication).getCore()
    }
<% if(websockets) { %>
    fun getMessager() {
      ( application as MyApplication).getMessager(this)
    }
<% } %>
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item?.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onContextItemSelected(item)
    }

<% if(websockets) { %>
    override fun onResume() {
      super.onResume()
      messager?.let { it.send("/topic/activity",getPageMessage()) }?: run { getMessager() }
    }

    private fun getPageMessage(): String {
      return "{\"page\":\"${this::class.java.simpleName}\"}"
    }

    override fun onMessagerGet(messager: Messager) {
      this.messager = messager
      messager.send("/topic/activity",getPageMessage())
    }

    override fun onMessagerGetError(error: String) {
      //Ignore
    }
<% } %>


}
