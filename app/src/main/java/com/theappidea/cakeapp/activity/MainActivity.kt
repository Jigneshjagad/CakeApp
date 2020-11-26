package com.theappidea.cakeapp.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
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
                //TODO:2 Remove duplicate entries by cake name
                //remove same title
                val ds: List<CakeItem> = LinkedHashSet(it).toMutableList()
                var list = ds as ArrayList<CakeItem>
                //TODO:3 Order entries by name
                //sort acending order
                list.sortBy { it.title }
                viewModel.setAdapterData(list)
            } else {
                Toast.makeText(applicationContext, "Error in fetching data", Toast.LENGTH_LONG)
                    .show()
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

        val lac = LayoutAnimationController(
            AnimationUtils.loadAnimation(
                this,
                R.anim.item_animation_fall_down
            )
        )
        lac.delay = 0.20f
        lac.order = LayoutAnimationController.ORDER_NORMAL

        //TODO:11 Animate in list items
        rvcake.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            layoutAnimation = lac
        }

        //TODO:7 Provide some kind of refresh option that reloads the list
        swipeRefresh.setOnRefreshListener {
            apiCall()
            swipeRefresh.isRefreshing = false
        }
    }

    private fun apiCall() {
        if (Utils.isOnline(this)) {
            viewModel.makeApiCall()
        } else {
            //TODO:8 Display an error message if the list cannot be loaded
            //TODO:10 Provide an option to retry when an error is presented
            val snackbar = Snackbar.make(
                activityMainBinding.mainLayout,
                "Check your Internet connection!",
                Snackbar.LENGTH_LONG
            )
            snackbar.setAction("Reload") {
                apiCall()
            }.show()

        }
    }


    //TODO:9 Handle orientation changes, ideally without reloading the list :- condition check for ViewModel factory
}
