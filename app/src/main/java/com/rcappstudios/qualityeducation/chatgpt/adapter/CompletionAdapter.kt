package com.rcappstudios.qualityeducation.chatgpt.adapter

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.makeramen.roundedimageview.RoundedImageView
import com.rcappstudios.qualityeducation.R
import com.rcappstudios.qualityeducation.chatgpt.adapter.model.CompletionAdapterModel

class CompletionAdapter(
    private val context: Context,
    private var completionList: MutableList<CompletionAdapterModel>
): RecyclerView.Adapter<CompletionAdapter.ViewHolder> (){

    private var voiceUrl = "https://translate.google.com/translate_tts?ie=UTF-&&client=tw-ob&tl=en&q="
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ivBot = view.findViewById<RoundedImageView>(R.id.ivBot)
        val textBot = view.findViewById<TextView>(R.id.botText)
        val header  = view.findViewById<TextView>(R.id.header)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_bot, parent, false))
    }

    override fun getItemCount(): Int = completionList.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val completionData = completionList[position]
        if(completionData.isBot){
            holder.ivBot.setImageResource(R.drawable.chat_gpt_logo)
            holder.header.text = "Bot"
        } else{
            holder.ivBot.setImageResource(R.drawable.user_img)
            holder.header.text = "You"
        }
        holder.textBot.text = completionData.text.toString().trimStart().removePrefix("\n")

    }

    fun addCompletionData(completionResponse: CompletionAdapterModel){
        completionList.add(completionResponse)
        notifyDataSetChanged()
    }


}