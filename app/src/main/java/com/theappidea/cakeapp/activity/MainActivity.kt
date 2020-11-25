package com.theappidea.cakeapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.theappidea.cakeapp.R
import com.theappidea.cakeapp.adapter.CakeAdapter
import com.theappidea.cakeapp.databinding.ActivityMainBinding
import com.theappidea.cakeapp.model.Cake
import com.theappidea.cakeapp.model.CakeItem
import com.theappidea.cakeapp.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CakeAdapter.OnItemViewClick {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.makeApiCall()
        viewModel.getCakeDataObserver().observe(this, Observer {
            progressBar.visibility = GONE
            if (swipeRefresh.isRefreshing) {
                swipeRefresh.isRefreshing = false
            }
            if (it != null) {

                var ds: List<CakeItem> = LinkedHashSet(it).toMutableList()
                var list = ds as ArrayList<CakeItem>
                viewModel.setAdapterData(list)
                Log.e("TAG", "onResponse: success" + it)
            } else {
                Toast.makeText(this, "Error in fetching data", Toast.LENGTH_LONG)
            }
        })
        setUpBinding(viewModel)
    }

    fun setUpBinding(viewModel: MainActivityViewModel) {

        val activityMainBinding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.setVariable(BR.viewModel, viewModel)
        activityMainBinding.executePendingBindings()

        rvcake.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.makeApiCall()
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onClick(position: Int) {
        Log.e("TAG", "onClick: " + position)

    }
}
