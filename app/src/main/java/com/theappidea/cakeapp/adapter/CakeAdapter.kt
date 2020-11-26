package com.theappidea.cakeapp.adapter

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.theappidea.cakeapp.R
import com.theappidea.cakeapp.activity.MainActivity
import com.theappidea.cakeapp.databinding.ItemCakeBinding
import com.theappidea.cakeapp.model.CakeItem

class CakeAdapter(val mainActivity: Context) : RecyclerView.Adapter<CakeAdapter.ViewHolder>() {

    var cakeList = ArrayList<CakeItem>()

    fun setDataList(data: ArrayList<CakeItem>) {
        this.cakeList = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCakeBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cakeList[position])
    }

    override fun getItemCount() = cakeList.size

    inner class ViewHolder(val binding: ItemCakeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: CakeItem) {
            binding.cakeItem = data
            binding.frame.setOnClickListener {
                showDialog(data.desc)
            }
            binding.executePendingBindings()

        }
    }

    private fun showDialog(desc: String) {
        Log.e("TAG", "showDialog: " + desc)
        val dialog = Dialog(mainActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dailog_cake)
        val body = dialog.findViewById(R.id.txtDesc) as TextView
        body.text = desc
        dialog.show()
    }

    companion object {
        @JvmStatic
        @BindingAdapter("loadImage")
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
}