package <%= packageName %>.core.data.enum

enum class <%= enumName %>{
    <% enumValues.forEach(function(enumValue){ %>
    <%= enumValue %>,
    <%});%>
}
