package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ViewPagerAdapter(context: Context, listImageMap: List<SlideObject>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    var listMap = listImageMap
    var mContext = context

    fun setList(listImageMap: List<SlideObject>){
        listMap = listImageMap
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mContext, listMap.get(position))
    }

    override fun getItemCount(): Int {
        return listMap.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var imgSlider: ImageView = itemView.findViewById(R.id.img_slider)
        private var tvName: TextView = itemView.findViewById(R.id.tv_slider)

        fun bind(context: Context, objectSlider: SlideObject) {
            tvName.text = objectSlider.iName
            Picasso.with(context).load(objectSlider.iUrl).into(imgSlider)
        }

    }
}