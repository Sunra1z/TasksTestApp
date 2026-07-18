package com.example.taskstestapp.presentation.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskstestapp.R
import com.example.taskstestapp.databinding.FragmentTaskListBinding
import com.example.taskstestapp.domain.tasks.Task
import kotlinx.coroutines.launch
import kotlin.getValue


class TaskListFragment : Fragment() {

    private val viewModel : TaskListViewModel by viewModels()
    private lateinit var adapter: TaskListAdapter

    private val args: TaskListFragmentArgs by navArgs<TaskListFragmentArgs>()

    private var _binding : FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.fetchTaskList(userId = args.userId, context = requireContext())

        setupDropdownFilter()

        val progressBar = binding.progressBar
        val errorLayout = binding.layoutError
        val menuFilter = binding.menuFilter
        val emptyLayout = binding.layoutEmpty
        val retryButton = binding.btnRetry

        retryButton.setOnClickListener {
            viewModel.fetchTaskList(requireContext(), userId = args.userId)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val rvList = binding.rvTasks
        adapter = TaskListAdapter(){ task, isChecked ->
            viewModel.saveLocalTask(
                task.userId, task.id, context = requireContext(), isChecked = isChecked
            )
        }
        rvList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { state ->
                    progressBar.isVisible = state.isLoading
                    errorLayout.isVisible = state.isError
                    menuFilter.isVisible = !state.isLoading && !state.isError

                    adapter.submitList(state.taskList){
                        val hasData = state.taskList.isNotEmpty()

                        if (state.isLoading || state.isError) {
                            rvList.isVisible = false
                            emptyLayout.isVisible = false
                        } else {
                            rvList.isVisible = hasData
                            emptyLayout.isVisible = !hasData

                        }
                    }
                }
            }
        }
    }

    private fun setupDropdownFilter(){
        val statusDisplayNames = TaskStatusFilter.entries.map { it.displayName }

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            statusDisplayNames
        )
        binding.autoCompleteFilter.setAdapter(adapter)

        binding.autoCompleteFilter.setText(TaskStatusFilter.ALL.displayName, false)

        binding.autoCompleteFilter.setOnItemClickListener { parent, view, position, id ->
            val selectedFilterOption = TaskStatusFilter.entries[position]
            viewModel.fetchTaskList(requireContext(), userId = args.userId, selectedFilterOption)
        }
    }


}