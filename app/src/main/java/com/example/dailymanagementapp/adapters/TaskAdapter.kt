package com.example.dailymanagementapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.dailymanagementapp.databinding.ItemListBinding
import com.example.dailymanagementapp.models.NotesData

class TaskAdapter(
    private val dataList: List<NotesData>,
    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<TaskAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onUpdateClicked(noteId: String , description:String)
        fun onDeleteClicked(noteId: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemList = dataList[position]
        holder.bind(itemList)
        holder.binding.taskEditButton.setOnClickListener {
            itemList.noteId?.let { it1 ->
                itemList.data?.let { it2 -> itemClickListener.onUpdateClicked(it1 , it2) }
            }
        }
        holder.binding.taskDeleteButton.setOnClickListener {
            itemList.noteId?.let { it1 ->
                itemClickListener.onDeleteClicked(it1)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(itemList: NotesData) {
            binding.taskDescription.text = itemList.data
        }
    }
}