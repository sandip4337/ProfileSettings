package com.example.profilesettings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.profilesettings.data.App
import com.example.profilesettings.databinding.AppListItemViewBinding
import kotlin.collections.ArrayList

class AppListAdapter(private var context: Context, var itemList : ArrayList<App>): RecyclerView.Adapter<AppListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = AppListItemViewBinding.inflate(
            LayoutInflater
                .from(context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            return@setOnClickListener
        }
        val items = itemList[position]

        holder.binding.appName.text = items.app_name

        Glide.with(context)
            .load(items.app_icon)
            .into(holder.binding.image)

        holder.binding.toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "${items.app_name} is on", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "${items.app_name} is Off", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun setFilteredList(mList: ArrayList<App>){
        this.itemList = mList
        notifyDataSetChanged()
    }


    class ViewHolder(val binding: AppListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}