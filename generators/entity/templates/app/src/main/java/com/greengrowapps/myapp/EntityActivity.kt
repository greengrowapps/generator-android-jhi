package <%= packageName %>

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
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewManager = LinearLayoutManager(this)
        viewAdapter = <%= entityName %>ViewAdapter(items)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        swiperefresh.setOnRefreshListener { refreshList() }
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
