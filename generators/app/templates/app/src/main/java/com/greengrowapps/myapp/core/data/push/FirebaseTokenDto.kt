package <%= packageName %>.core.data.firebase_token

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class FirebaseTokenDto : Serializable{
    var id: Long? = null

    var token: String? = null

    var userId: Long? = null
}
