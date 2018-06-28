package <%= packageName %>.viewadapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import <%= packageName %>.R
import <%= packageName %>.core.data.<%= entityNameLower %>.<%= entityName %>Dto

class <%= entityName %>ViewAdapter(private val myDataset: List<<%= entityName %>Dto>) :
        RecyclerView.Adapter<<%= entityName %>ViewAdapter.ViewHolder>() {

    class ViewHolder(parent: View, val nameTextView: TextView, val descriptionTextView: TextView, val numberTextView: TextView, val modelTextView: TextView) : RecyclerView.ViewHolder(parent)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): <%= entityName %>ViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_<%= entityNameLower %>_item, parent, false) as View

        return ViewHolder(view,view.findViewById(R.id.tv_name),view.findViewById(R.id.tv_description),view.findViewById(R.id.tv_number),view.findViewById(R.id.tv_model))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = myDataset[position]
        holder.nameTextView.text = item.name
        holder.descriptionTextView.text = item.description
        holder.numberTextView.text = item.number
        holder.modelTextView.text = item.modelName
    }

    override fun getItemCount() = myDataset.size
}
