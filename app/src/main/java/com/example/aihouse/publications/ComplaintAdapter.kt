package com.example.aihouse.publications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.R
import com.example.aihouse.models.Complaint

class ComplaintAdapter(
    private val complaints: List<Complaint>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ComplaintAdapter.ComplaintViewHolder>() {

    private var selectedPosition: Int = -1

    inner class ComplaintViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.txtComplaintText_itemcomplaint)
        private val countView: TextView = view.findViewById(R.id.txtComplaintCount_itemcomplaint)
        private val background: ImageView = view.findViewById(R.id.bckgr_itemcomplaint)

        fun bind(complaint: Complaint, isSelected: Boolean) {
            textView.text = complaint.text
            countView.text = complaint.count.toString()
            if (isSelected) {
                background.visibility = View.VISIBLE
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
                countView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                background.visibility = View.GONE
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_primary_color))
                countView.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_primary_color))
            }
            itemView.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)
                onItemClick(selectedPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_complaint, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        holder.bind(complaints[position], position == selectedPosition)
    }

    override fun getItemCount() = complaints.size
}