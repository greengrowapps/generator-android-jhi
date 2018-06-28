package <%= packageName %>.core

import com.greengrowapps.ggarest.serialization.Serializer
import org.codehaus.jackson.map.ObjectMapper
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CustomSerializer : Serializer {

    private val mapper : ObjectMapper = ObjectMapper()

    init{
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        mapper.setDateFormat(sdf)
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
