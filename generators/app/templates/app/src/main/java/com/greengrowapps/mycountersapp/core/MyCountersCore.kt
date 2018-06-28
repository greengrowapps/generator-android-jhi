package <%= packageName %>.core

import android.content.SharedPreferences
import com.greengrowapps.ggarest.GgaRest
import com.greengrowapps.ggarest.listeners.OnListResponseListener
import com.greengrowapps.ggarest.listeners.OnObjResponseListener
import com.greengrowapps.ggarest.serialization.Serializer
import com.greengrowapps.jhiusers.JhiUsers
import <%= packageName %>.core.cache.CombinedCache
import <%= packageName %>.core.config.CoreConfiguration
import <%= packageName %>.core.counters.CounterDto
import <%= packageName %>.core.counters.CounterRestResource
import <%= packageName %>.core.counters.CounterService
import java.util.concurrent.atomic.AtomicInteger


class MyCountersCore(private val jhiUsers: JhiUsers, private val configuration: CoreConfiguration, private val preferences: SharedPreferences, private val serializer: Serializer) {

    fun CounterService(): CounterService {
        val resource = CounterRestResource(configuration.serverUrl,GgaRest.ws(),jhiUsers)

        return CounterService(resource, CombinedCache(preferences,serializer))
    }
}
