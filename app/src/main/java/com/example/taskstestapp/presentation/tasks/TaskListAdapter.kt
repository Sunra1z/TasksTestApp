package com.example.taskstestapp.presentation.tasks

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taskstestapp.R
import com.example.taskstestapp.domain.tasks.Task

class TaskListAdapter(
    private val onCheckmarkClick: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskListAdapter.TaskListItemViewHolder>(TaskListDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_list_item, parent, false)
        return TaskListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskListItemViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("Adapter", "Got list item: ${item} ")
        holder.tvTitle.text = item.title
        holder.tvStatus.text =
            if (item.completed){
            "Completed"
        } else
            "In work"

        holder.tvLocalStatus.text =
            if (item.completedLocal){
            "Completed (local)"
        } else
            "In work (local)"

        holder.checkboxLocalStatus.isChecked = item.completedLocal
        holder.checkboxLocalStatus.setOnCheckedChangeListener { _, isChecked ->
            onCheckmarkClick(item, isChecked)
            holder.tvLocalStatus.text =
                if (isChecked){
                    "Completed (local)"
            } else
                "In work (local)"
        }
    }

    class TaskListItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
       val tvTitle: TextView = view.findViewById(R.id.tv_item_task_name)
       val tvStatus: TextView = view.findViewById(R.id.tv_item_task_status)
        val tvLocalStatus: TextView = view.findViewById(R.id.tv_item_local_status)
        val checkboxLocalStatus: CheckBox = view.findViewById(R.id.checkbox_status)
    }

    private object TaskListDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }
    }
}