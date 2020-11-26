package com.theappidea.cakeapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.theappidea.cakeapp.R
import com.theappidea.cakeapp.databinding.ActivityMainBinding
import com.theappidea.cakeapp.model.CakeItem
import com.theappidea.cakeapp.utils.Utils
import com.theappidea.cakeapp.viewmodel.MainActivityViewModel
import com.theappidea.cakeapp.viewmodel.MainActivityViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var viewModelFactory: MainActivityViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)


        viewModelFactory = MainActivityViewModelFactory(this)

        viewModel =
            ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        viewModel.getCakeDataObserver().observe(this, Observer {
            progressBar.visibility = GONE
            txtError.visibility = GONE
            if (swipeRefresh.isRefreshing) {
                swipeRefresh.isRefreshing = false
            }
            if (it != null) {
                var ds: List<CakeItem> = LinkedHashSet(it).toMutableList()
                var list = ds as ArrayList<CakeItem>
                viewModel.setAdapterData(list)
            } else {
                Snackbar.make(
                    activityMainBinding.root,
                    "Error in fetching data",
                    Snackbar.LENGTH_LONG
                )
            }
        })
        setUpBinding(viewModel)
    }

    fun setUpBinding(viewModel: MainActivityViewModel) {

        activityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.setVariable(BR.viewModel, viewModel)
        activityMainBinding.executePendingBindings()

        if (Utils.isOnline(this)) {
            txtError.visibility = GONE
            viewModel.makeApiCall()
        } else {
            Log.e("TAG", "onCreate: status ")
            progressBar.visibility = GONE
            txtError.visibility = VISIBLE
        }

        rvcake.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
        swipeRefresh.setOnRefreshListener {
            if (Utils.isOnline(this)) {
                viewModel.makeApiCall()
            } else {
                Toast.makeText(applicationContext, "Check your Internet connection!", Toast.LENGTH_SHORT).show()
            }
            swipeRefresh.isRefreshing = false
        }
    }

}
