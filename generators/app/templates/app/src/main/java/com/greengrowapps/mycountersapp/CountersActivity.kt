package <%= packageName %>

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import <%= packageName %>.core.counters.CounterDto
import <%= packageName %>.viewadapters.CounterViewAdapter

import kotlinx.android.synthetic.main.activity_counters.*
import kotlinx.android.synthetic.main.content_counters.*

class CountersActivity : BaseActivity() {

    companion object {
        fun openIntent(from: Context) : Intent{
            return Intent(from,CountersActivity::class.java)
        }
    }

    private val items = ArrayList<CounterDto>()
    private lateinit var viewAdapter: CounterViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_counters)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewManager = LinearLayoutManager(this)
        viewAdapter = CounterViewAdapter(items)

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
                getCore().CounterService()
                        .readList(
                                true,
                                {list -> populateItems(list); dismissLoadingIndicator() },
                                { statusCode, response -> showError(); dismissLoadingIndicator() }
                        ))
    }

    private fun dismissLoadingIndicator() {
        swiperefresh.isRefreshing = false
    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.error_getting_items), Toast.LENGTH_SHORT).show()
    }

    private fun populateItems(list: List<CounterDto>) {
        items.clear()
        items.addAll(list)
        viewAdapter.notifyDataSetChanged()
    }


}
