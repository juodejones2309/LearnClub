package com.rcappstudios.qualityeducation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rcappstudios.qualityeducation.databinding.ItemPdfBinding
import com.rcappstudios.qualityeducation.model.PdfModel
import com.rcappstudios.qualityeducation.model.mentors.MentorData

class PDFAdapter(
    private val context: Context,
    private val pdfList: MutableList<PdfModel>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    private lateinit var binding: ItemPdfBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = ItemPdfBinding.inflate(LayoutInflater.from(context), null, false)
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return pdfList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val pdf = pdfList[position]
        binding.pdfFileName.text = pdf.pdfName
        binding.root.setOnClickListener {
            onClick.invoke(pdf.pdfUrl.toString())
        }
    }


}