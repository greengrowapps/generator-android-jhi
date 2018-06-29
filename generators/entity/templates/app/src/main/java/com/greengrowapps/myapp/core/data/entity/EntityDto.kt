package <%= packageName %>.core.data.<%= entityNameLower %>

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable
import java.util.Date
import <%= packageName %>.core.data.enum.*

@JsonIgnoreProperties(ignoreUnknown = true)
class <%= entityName %>Dto : Serializable {

    var id: Long? = null

    <% fields.forEach(function(field){ %>
    var <%=field.fieldName%>: <%=field.fieldType%>? = null
    <% }); %>
}
