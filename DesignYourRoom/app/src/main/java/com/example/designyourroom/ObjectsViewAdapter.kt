package com.example.designyourroom

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity.apply
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.GravityCompat.apply
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.objects_card_layout.view.*
import java.net.URI


class ObjectsViewAdapter(
    private val listObj: ArrayList<ExampleObj>
) : RecyclerView.Adapter<ObjectsViewAdapter.ObjectsViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectsViewHolder {
        val viewLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.objects_card_layout,
            parent,
            false
        )
        return ObjectsViewHolder(viewLayout)
    }

    override fun onBindViewHolder(holder: ObjectsViewHolder, position: Int) {
        holder.bind(listObj[position])

        holder.itemView.setOnClickListener(View.OnClickListener {
            val url = listObj[position].getLink()
            Utils.startNewActivity(it.context, url)


        })
    }

    override fun getItemCount(): Int {
       return listObj.size
    }

    inner class ObjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(obj: ExampleObj) {
            itemView.txv_content.text = obj.getObjText()
        }

    }

    object Utils {

        fun startNewActivity(context: Context, uriStr: String) {

            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
               data = Uri.parse(uriStr)
            }
            context.startActivity(intent)

        }


    }


}