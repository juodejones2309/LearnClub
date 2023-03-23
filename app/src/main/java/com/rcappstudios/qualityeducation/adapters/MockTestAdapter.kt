package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemRoomBinding
import com.rcappstudios.qualityeducation.databinding.ItemTestBinding
import com.rcappstudios.qualityeducation.model.RoomModel
import com.rcappstudios.qualityeducation.model.Test

class MockTestAdapter(
    private val context: Context,
    private val testList: MutableList<String>,
    private val onClick: (Int, String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemTestBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemTestBinding.inflate(LayoutInflater.from(context),parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val test = testList[position]
        binding.testName.text = test
        binding.root.setOnClickListener {
            onClick.invoke(position, test)
        }
    }

    override fun getItemCount(): Int {
        return testList.size
    }
}