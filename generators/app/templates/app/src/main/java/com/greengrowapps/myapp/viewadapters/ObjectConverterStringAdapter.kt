package <%= packageName %>.viewadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView


class ObjectConverterStringAdapter<T>(context: Context?, objects: MutableList<T>?, private val convert: (T) -> String) : ArrayAdapter<T>(context, android.R.layout.simple_list_item_1, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = (convertView ?: inflater.inflate(android.R.layout.simple_list_item_1, parent, false) ) as TextView

        row.text = convert( getItem(position) )

        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
      val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      val row = (convertView ?: inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false) ) as TextView

      row.text = convert( getItem(position) )

      return row
    }

}
