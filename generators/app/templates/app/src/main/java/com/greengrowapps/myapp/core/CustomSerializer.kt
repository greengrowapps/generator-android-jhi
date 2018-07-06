package <%= packageName %>.core

import com.greengrowapps.ggarest.serialization.Serializer
import org.codehaus.jackson.map.ObjectMapper
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CustomSerializer : Serializer {

    private val mapper : ObjectMapper = ObjectMapper()

    init{
      val formats = arrayOf("yyyy-MM-dd'T'HH:mm:ssX","yyyy-MM-dd'T'HH:mm:ssZZZZZ","yyyy-MM-dd'T'HH:mm:ss")

      for(format in formats){
        if(trySetFormat(format)){
          break
        }
      }
    }

    private fun trySetFormat(format: String): Boolean {
      try {
        val sdf = SimpleDateFormat(format)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        mapper.setDateFormat(sdf)
        return true
      }
      catch (e:Exception){
        return false
      }
    }

    @Throws(IOException::class)
    override fun <T> fromString(string: String, clazz: Class<T>): T {
        return mapper.readValue(string, clazz)
    }

    @Throws(IOException::class)
    override fun fromObject(`object`: Any): String {
        return mapper.writeValueAsString(`object`)
    }

    override fun isJson(): Boolean {
        return true
    }

}
