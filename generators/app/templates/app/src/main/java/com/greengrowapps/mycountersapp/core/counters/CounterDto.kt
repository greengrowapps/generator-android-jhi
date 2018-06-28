package <%= packageName %>.core.counters

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class CounterDto : Serializable {

    var id: Long? = null

    var name: String? = null

    var description: String? = null

    var number: String? = null

    var modelId: Long? = null

    var modelName: String? = null
}
