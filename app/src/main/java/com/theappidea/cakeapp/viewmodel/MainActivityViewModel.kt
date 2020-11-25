package com.theappidea.cakeapp.viewmodel

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theappidea.cakeapp.R
import com.theappidea.cakeapp.adapter.CakeAdapter
import com.theappidea.cakeapp.model.Cake
import com.theappidea.cakeapp.model.CakeItem
import com.theappidea.cakeapp.retrofit.RetroInstance
import com.theappidea.cakeapp.retrofit.RetroService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel(), CakeAdapter.OnItemViewClick {
    lateinit var cakeListData: MutableLiveData<Cake>
    lateinit var cakeAdapter: CakeAdapter

    init {
        cakeListData = MutableLiveData()
        cakeAdapter = CakeAdapter(this)
    }

    fun getAdapter(): CakeAdapter {
        return cakeAdapter
    }

    fun setAdapterData(data: ArrayList<CakeItem>) {
        cakeAdapter.setDataList(data)
        cakeAdapter.notifyDataSetChanged()
    }

    fun getCakeDataObserver(): MutableLiveData<Cake> {
        return cakeListData
    }

    fun makeApiCall() {
        val retroInstance = RetroInstance.getRetroInstance().create(RetroService::class.java)
        val call = retroInstance.getCakeDataFromApi()
        call.enqueue(object : Callback<Cake> {
            override fun onResponse(call: Call<Cake>, response: Response<Cake>) {
                if (response.isSuccessful) {
                    cakeListData.postValue(response.body())
                    Log.e("TAG", "onResponse: success")
                } else {
                    cakeListData.postValue(null)
                    Log.e("TAG", "onResponse: fail")
                }
            }

            override fun onFailure(call: Call<Cake>, t: Throwable) {
                cakeListData.postValue(null)
                Log.e("TAG", "onResponse: failure")
                Log.e("TAG", "onResponse: failure", t)
            }

        })
    }

    companion object {
        @JvmStatic
        @BindingAdapter("onRefresh")
        fun loadImage(thubmImage: ImageView, url: String) {
            Glide.with(thubmImage)
                .load(url)
                .circleCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .fallback(R.drawable.ic_launcher_foreground)
                .into(thubmImage)
        }

    }

    override fun onClick(position: Int) {
        Log.e("TAG", "onClick: ")
    }
}