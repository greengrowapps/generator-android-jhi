package <%= packageName %>

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import <%= packageName %>.core.data.<%= entityNameLower %>.<%= entityName %>Dto
import <%= packageName %>.viewadapters.<%= entityName %>ViewAdapter

import kotlinx.android.synthetic.main.activity_<%= entityNameLower %>.*
import kotlinx.android.synthetic.main.content_<%= entityNameLower %>.*

class <%= entityName %>Activity : BaseActivity() {

    companion object {
        fun openIntent(from: Context) : Intent{
            return Intent(from,<%= entityName %>Activity::class.java)
        }
    }

    private val items = ArrayList<<%= entityName %>Dto>()
    private lateinit var viewAdapter: <%= entityName %>ViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_<%= entityNameLower %>)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
          startActivity(<%= entityName %>DetailActivity.newIntent(this))
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewManager = LinearLayoutManager(this)
        viewAdapter = <%= entityName %>ViewAdapter(items, {item -> editItem(item) }, {item -> deleteItem(item) })

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        swiperefresh.setOnRefreshListener { refreshList() }
    }

    private fun deleteItem(item: <%= entityName %>Dto) {
      AlertDialog.Builder(this)
        .setMessage(R.string.sure_to_delete)
        .setPositiveButton(R.string.delete) { dialogInterface, i ->
          getCore().<%= entityNameLower %>Service().delete(item.id,{ deleteSuccess() }, { code,error -> deleteError(error)})
        }
        .setNegativeButton(R.string.cancel,null)
        .show()
    }

    private fun deleteError(error: String) {
      Toast.makeText(this,R.string.delete_error,Toast.LENGTH_SHORT).show()
    }

    private fun deleteSuccess() {
      refreshList()
    }

    private fun editItem(item: <%= entityName %>Dto) {
      startActivity(<%= entityName %>DetailActivity.editIntent(this,item))
    }

    override fun onResume() {
        super.onResume()
        refreshList()
    }

    private fun refreshList() {
        swiperefresh.isRefreshing = true
        populateItems(
                getCore().<%= entityNameLower %>Service()
                        .readList(
                                true,
                                {list: List<<%= entityName %>Dto> -> populateItems(list); dismissLoadingIndicator() },
                                { statusCode: Int, response: String -> showError(); dismissLoadingIndicator() }
                        ))
    }

    private fun dismissLoadingIndicator() {
        swiperefresh.isRefreshing = false
    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.error_getting_items), Toast.LENGTH_SHORT).show()
    }

    private fun populateItems(list: List<<%= entityName %>Dto>) {
        items.clear()
        items.addAll(list)
        viewAdapter.notifyDataSetChanged()
    }


}
