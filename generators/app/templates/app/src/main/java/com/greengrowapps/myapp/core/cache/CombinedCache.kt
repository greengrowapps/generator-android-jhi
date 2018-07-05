package <%= packageName %>.core.cache

import android.content.SharedPreferences
import com.greengrowapps.ggarest.serialization.Serializer

class CombinedCache(private val preferences: SharedPreferences, private val serializer: Serializer) {

    fun <T> save(key: String, item: T){

        val json = serializer.fromObject(item)

        preferences.edit().putString(key,json).apply()
    }
    fun <T> load(key: String, clazz: Class<T>) : T? {
        if(preferences.contains(key)){
            try {
                val json = preferences.getString(key,"")
                return serializer.fromString(json,clazz)
            }
            catch (e:Exception){
                //Ignore
            }
        }
        return null
    }
    fun clear(key: String){
      preferences.edit().remove(key).apply()
    }
    fun clearAll(){

    }
}
