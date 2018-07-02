package <%= packageName %>

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import <%= packageName %>.core.data.<%= entityNameLower %>.<%= entityName %>Dto
import kotlinx.android.synthetic.main.activity_<%= entityNameLower %>_detail.*
import java.util.*
import <%= packageName %>.core.data.enum.*
import <%= packageName %>.core.l18n.EnumLocalization
import <%= packageName %>.viewadapters.ObjectConverterStringAdapter
<% fields.forEach(function(field){ if(!field.isDependency){return;} %>
import <%= packageName %>.core.data.<%= field.otherEntityNameLower %>.<%= field.otherEntityName %>Dto
  <% }) %>
class <%= entityName %>DetailActivity : BaseActivity() {

    private var isSaving: Boolean = false
    private lateinit var item: <%= entityName %>Dto
    private var isNew: Boolean = false

    private val pendingDependencies = HashSet<String>()
    <% fields.forEach(function(field){ if(!field.isDependency){return;} %>
    private val <%= field.otherEntityNameLower %>List = ArrayList<<%= field.otherEntityName%>Dto>()
    private lateinit var <%= field.otherEntityNameLower %>Adapter : ArrayAdapter<<%= field.otherEntityName%>Dto>

    <% }) %>
    companion object {
        private const val ITEM_EXTRA = "ItemExtra"

        fun newIntent(from: Context): Intent {
            return Intent(from,<%= entityName %>DetailActivity::class.java)
        }
        fun editIntent(from: Context, item: <%= entityName %>Dto): Intent {
            val intent = Intent(from,<%= entityName %>DetailActivity::class.java)
            intent.putExtra(ITEM_EXTRA,item)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_<%= entityNameLower %>_detail)

        if (intent.hasExtra(ITEM_EXTRA)){
            item = intent.extras.getSerializable(ITEM_EXTRA) as <%= entityName %>Dto
            isNew = false
            populate(item)
        }
        else{
            isNew = true
            item = <%= entityName %>Dto()
        }

        save_button.setOnClickListener { attempSave() }
        <% fields.forEach(function(field){ if(!field.isDependency){return;} %>
        <%= field.otherEntityNameLower %>Adapter = ObjectConverterStringAdapter<<%= field.otherEntityName %>Dto>(this,<%= field.otherEntityNameLower %>List,{ item -> item.<%= field.titleProperty %>.toString()})
        spinner_<%= field.fieldName %>.adapter = <%= field.otherEntityNameLower %>Adapter
        <% }) %>

        <% if(hasDependencies){%>
        loadDependencies()
        <% } %>
    }
  <% if(hasDependencies){%>
    private fun loadDependencies() {
      pendingDependencies.clear()
      showProgress(true)

      <% fields.forEach(function(field){ if(!field.isDependency){return;} %>
      pendingDependencies.add("<%= field.otherEntityName %>")
      getCore().<%= field.otherEntityNameLower %>Service().readList(
        false,
        { items -> dependencyLoad(items,<%= field.otherEntityNameLower %>List,"<%= field.otherEntityName %>")},
        {statusCode, response -> dependencyLoadError(response) })
      <% }) %>
    }

    private fun dependencyLoadError(error: String) {
      Toast.makeText(this,R.string.dependency_load_error,Toast.LENGTH_SHORT).show()
      finish()
    }

    private fun <T> dependencyLoad(items: List<T>, target: ArrayList<T>, dependencyName: String) {
      target.clear()
      target.addAll(items)
      pendingDependencies.remove(dependencyName)
      checkDependencies()
    }
    private fun checkDependencies(){
      if(pendingDependencies.isEmpty()){
        save_button.setOnClickListener { attempSave() }
        <% fields.forEach(function(field){ if(!field.isDependency){return;} %>
          <%= field.otherEntityNameLower %>Adapter.notifyDataSetChanged()
          <% }) %>
        showProgress(false)
      }
    }
    <%}%>

    private fun populate(item: <%= entityName %>Dto) {

    <% fields.forEach(function(field){ if(field.isDerived){return;} %>
    <%switch (field.fieldType) {
      case 'String':%>
      input_<%=field.fieldName%>.setText(item.<%=field.fieldName%>?:"")
      <%break;
      case 'Date':%>
      item.<%=field.fieldName%>?.let {  input_<%=field.fieldName%>.setText( "${DateFormat.getDateFormat(this).format(item.<%=field.fieldName%>)} ${DateFormat.getTimeFormat(this).format(item.<%=field.fieldName%>)}" ) }
      <%break;
      default: if(field.isEnum){%>
      input_<%=field.fieldName%>.setText( EnumLocalization.localize<%=field.fieldType.substring(0,1).toUpperCase() %><%=field.fieldType.substring(1) %>(item.<%=field.fieldName%>,this) )
      <% } else if(field.isDependency){ %>
      spinner_<%=field.fieldName%>.setSelection( <%= field.otherEntityNameLower %>List.indexOfFirst { it -> it.id==item.<%= field.fieldName %> } )
      <% } else { %>
      input_<%=field.fieldName%>.setText( item.<%=field.fieldName%>?.toString()?:"" )
      <% } break;
    }%>
    <% }); %>

    }

    private fun parseDate(dateTime: String): Date? {
      val date = dateTime.split(' ').firstOrNull()
      val time = dateTime.split(' ').lastOrNull()

      val calendar = Calendar.getInstance()
      calendar.time = DateFormat.getDateFormat(this).parse(date)

      val timeCalendar = Calendar.getInstance()
      timeCalendar.time = DateFormat.getTimeFormat(this).parse(time)

      calendar.set(Calendar.HOUR_OF_DAY,timeCalendar.get(Calendar.HOUR_OF_DAY))
      calendar.set(Calendar.MINUTE,timeCalendar.get(Calendar.MINUTE))
      calendar.set(Calendar.SECOND,timeCalendar.get(Calendar.SECOND))

      return calendar.time
    }

    private fun attempSave() {
        if (isSaving) {
            return
        }

        <% fields.forEach(function(field){  if(field.isDerived){return;} %>
        input_<%=field.fieldName%>.error = null
        <%switch (field.fieldType) {
          case 'String':%>
        item.<%=field.fieldName%> = input_<%=field.fieldName%>.text.toString()
          <%break;
          case 'Long': if (field.isDependency){%>
          item.<%=field.fieldName%> = (spinner_<%=field.fieldName%>.selectedItem as? <%=field.otherEntityName%>Dto)?.id
          <% } else {%>
            item.<%=field.fieldName%> = input_<%=field.fieldName%>.text.toString().toLongOrNull()
          <%} break;
          case 'Int':%>
          item.<%=field.fieldName%> = input_<%=field.fieldName%>.text.toString().toIntOrNull()
          <%break;
          case 'Float':%>
          item.<%=field.fieldName%> = input_<%=field.fieldName%>.text.toString().toFloatOrNull()
          <%break;
          case 'Double':%>
          item.<%=field.fieldName%> = input_<%=field.fieldName%>.text.toString().toDoubleOrNull()
          <%break;
          case 'Date':%>
          item.<%=field.fieldName%> = parseDate(input_<%=field.fieldName%>.text.toString())
          <%break;
          default: if(field.isEnum){%>
          item.<%=field.fieldName%> = <%=field.fieldType %>.valueOf(input_<%=field.fieldName%>.text.toString())
          <% } else {%>
          item.<%=field.fieldName%> = input_<%=field.fieldName%>.text.toString()
          <%} } %>
        <% }); %>


        var cancel = false
        var focusView: View? = null

        <% fields.forEach(function(field){  if(field.isDerived){return;} %>
        if (!is<%=field.fieldName.substring(0,1).toUpperCase()%><%=field.fieldName.substring(1)%>Valid(item.<%=field.fieldName%>)) {
          input_<%=field.fieldName%>.error = getString(R.string.error_<%=entityNameLower%>_invalid_<%=field.fieldName%>)
          focusView = input_<%=field.fieldName%>
          cancel = true
        }
        <% }); %>

        if (cancel) {
            focusView?.requestFocus()
        } else {
            showProgress(true)
            isSaving=true

            val service = getCore().<%=entityNameLower%>Service()

            if(isNew) {
                service.create(item,{ saved -> onSaveSuccess(saved) }, {statusCode, response -> onSaveError(response) })
            }
            else{
                service.update(item,{ saved -> onSaveSuccess(saved) }, {statusCode, response -> onSaveError(response) })
            }
        }
    }

    private fun onSaveError(response: String) {
        showProgress(false)
        Toast.makeText(this,R.string.save_error,Toast.LENGTH_SHORT).show()
    }

    private fun onSaveSuccess(<%= entityName %>Dto: <%= entityName %>Dto) {
        finish()
    }
    <% fields.forEach(function(field){ if(field.isDerived){return;} %>

    private fun is<%=field.fieldName.substring(0,1).toUpperCase()%><%=field.fieldName.substring(1)%>Valid(field: <%= field.fieldType %>?): Boolean {
        //TODO: Replace this with your own logic
        <%switch (field.fieldType) {
        case 'String':%>
        return !TextUtils.isEmpty(field)
        <%break;
        case 'Long':%>
        return field?:0>0
        <%break;
        default: %>
        return field != null
        <% } %>
    }
    <% }); %>

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        entity_form.visibility = if (show) View.GONE else View.VISIBLE
        entity_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        entity_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

        save_progress.visibility = if (show) View.VISIBLE else View.GONE
        save_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        save_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

}
