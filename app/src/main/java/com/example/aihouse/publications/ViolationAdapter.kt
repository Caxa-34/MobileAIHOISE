package com.example.aihouse.publications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.aihouse.R
import com.example.aihouse.models.Violation

class ViolationAdapter(
    private val Violations: List<Violation>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ViolationAdapter.ViolationViewHolder>() {

    private var selectedPosition: Int = -1

    inner class ViolationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val textView: TextView = view.findViewById(R.id.txtComplaintText_itemcomplaint)
        private val background: ImageView = view.findViewById(R.id.bckgr_itemcomplaint)

        fun bind(Violation: Violation, isSelected: Boolean) {
            textView.text = Violation.text
            if (isSelected) {
                background.visibility = View.VISIBLE
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                background.visibility = View.GONE
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.gray_primary_color))
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViolationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_violation, parent, false)
        return ViolationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViolationViewHolder, position: Int) {
        holder.bind(Violations[position], position == selectedPosition)
    }

    override fun getItemCount() = Violations.size
}
