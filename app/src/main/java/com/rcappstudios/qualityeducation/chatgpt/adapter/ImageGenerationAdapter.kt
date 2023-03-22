package com.rcappstudios.qualityeducation.chatgpt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.rcappstudios.qualityeducation.R
import com.squareup.picasso.Picasso
import com.rcappstudios.qualityeducation.chatgpt.adapter.model.ImageGenerationAdapterModel

class ImageGenerationAdapter(
    private val context: Context,
    private var imageGenerationList: MutableList<ImageGenerationAdapterModel>
) : RecyclerView.Adapter<ImageGenerationAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivBot = view.findViewById<RoundedImageView>(R.id.ivBot)
        val textBot = view.findViewById<TextView>(R.id.botText)
        val header = view.findViewById<TextView>(R.id.header)
        val ivGenerated = view.findViewById<RoundedImageView>(R.id.ivGenerated)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bot, parent, false))
    }

    override fun getItemCount(): Int = imageGenerationList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageGenerationData = imageGenerationList[position]
        if (imageGenerationData.isBot) {
            holder.textBot.visibility = View.GONE
            holder.ivGenerated.visibility = View.VISIBLE
            holder.ivBot.setImageResource(R.drawable.chat_gpt_logo)
            holder.header.text = "Bot"
            if (imageGenerationData.url != null && imageGenerationData.url != "") {
                Picasso.get()
                    .load(imageGenerationData.url)
                    .fit().centerInside()
                    .into(holder.ivGenerated)
            }
        } else {
            holder.textBot.visibility = View.VISIBLE
            holder.textBot.text = imageGenerationData.url
            holder.ivGenerated.visibility = View.GONE
            holder.ivBot.setImageResource(R.drawable.user_img)
            holder.header.text = "You"
        }

        if (imageGenerationList.size == 1) {
            holder.textBot.visibility = View.VISIBLE
            holder.ivGenerated.visibility = View.GONE
            holder.textBot.text = imageGenerationData.url
        }

    }

    fun addImageGeneratedData(imageGenerationData: ImageGenerationAdapterModel) {
        imageGenerationList.add(imageGenerationData)
        notifyDataSetChanged()
    }
}