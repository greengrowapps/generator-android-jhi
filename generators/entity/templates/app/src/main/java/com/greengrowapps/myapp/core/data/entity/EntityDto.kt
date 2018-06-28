package <%= packageName %>.core.data.<%= entityNameLower %>

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class <%= entityName %>Dto : Serializable {

    var id: Long? = null

    var name: String? = null

    var description: String? = null

    var number: String? = null

    var modelId: Long? = null

    var modelName: String? = null
}
