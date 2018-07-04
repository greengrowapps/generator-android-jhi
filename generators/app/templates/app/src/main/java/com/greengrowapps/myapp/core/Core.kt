package <%= packageName %>.core

import android.content.SharedPreferences
import com.greengrowapps.ggarest.GgaRest
import com.greengrowapps.ggarest.serialization.Serializer
import com.greengrowapps.jhiusers.JhiUsers
import <%= packageName %>.core.cache.CombinedCache
import <%= packageName %>.core.config.CoreConfiguration
//import-needle

class Core(private val jhiUsers: JhiUsers, private val configuration: CoreConfiguration, private val preferences: SharedPreferences, private val serializer: Serializer) {

  //services-needle

}
