package <%= packageName %>

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.greengrowapps.jhiusers.JhiUsers
import <%= packageName %>.core.MyCountersCore

open class BaseActivity : AppCompatActivity(){

    fun getJhiUsers() : JhiUsers{
        return ( application as MyCountersApplication).getJhiUsers()
    }
    fun getCore() : MyCountersCore{
        return ( application as MyCountersApplication).getCore()
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId==android.R.id.home){
            onBackPressed()
        }
        return super.onContextItemSelected(item)
    }
}
