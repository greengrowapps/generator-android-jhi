package <%= packageName %>.viewadapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.text.format.DateFormat
import <%= packageName %>.R
import <%= packageName %>.core.data.<%= entityNameLower %>.<%= entityName %>Dto

class <%= entityName %>ViewAdapter(private val myDataset: List<<%= entityName %>Dto>) :
        RecyclerView.Adapter<<%= entityName %>ViewAdapter.ViewHolder>() {

    class ViewHolder(val parent: View
                      <% fields.forEach(function(field){ %>
                      ,val <%=field.fieldName%>TextView: TextView
                      <% }); %>
    ) : RecyclerView.ViewHolder(parent)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): <%= entityName %>ViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_<%= entityNameLower %>_item, parent, false) as View

        return ViewHolder(view
          <% fields.forEach(function(field){ %>
          , view.findViewById(R.id.tv_<%=field.fieldName%>)
          <% }); %>
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myDataset[position]
        <% fields.forEach(function(field){ %>
        <%switch (field.fieldType) {
          case 'String':%>
          holder.<%=field.fieldName%>TextView.text = item.<%=field.fieldName%>?:""
          <%break;
          case 'Date':%>
          item.<%=field.fieldName%>?.let { holder.<%=field.fieldName%>TextView.text = "${DateFormat.getDateFormat(holder.parent.context).format(item.<%=field.fieldName%>)} ${DateFormat.getTimeFormat(holder.parent.context).format(item.<%=field.fieldName%>)}" }
          <%break;
          default:%>
          holder.<%=field.fieldName%>TextView.text = item.<%=field.fieldName%>?.toString()?:""
          <%break;
        }%>
        <% }); %>
    }

    override fun getItemCount() = myDataset.size
}
