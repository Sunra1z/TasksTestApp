package com.example.taskstestapp.presentation.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.taskstestapp.databinding.FragmentUsersListBinding
import kotlinx.coroutines.launch
import java.util.Locale

class UsersListFragment : Fragment() {

    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: UserListAdapter

    private var _binding: FragmentUsersListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersListBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = binding.progressBar
        val errorLayout = binding.layoutError
        val rvList = binding.rvUsers
        val searchView = binding.searchView
        val emptyLayout = binding.layoutEmpty
        val retryButton = binding.btnRetry

        progressBar.isVisible = true
        rvList.isVisible = false
        emptyLayout.isVisible = false
        errorLayout.isVisible = false

        setupSearchView()

        retryButton.setOnClickListener {
            viewModel.fetchUserList()
        }

        adapter = UserListAdapter() { clickedUser ->
            val action = UsersListFragmentDirections
                .actionUsersListFragmentToUserDetailsFragment(userId = clickedUser.id)

            findNavController().navigate(action)
        }
        rvList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { state ->
                    progressBar.isVisible = state.isLoading
                    errorLayout.isVisible = state.isError

                    searchView.isVisible = !state.isLoading and !state.isError

                    adapter.submitList(state.userList){
                        val hasData = state.userList.isNotEmpty()

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

    private fun setupSearchView(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText?.lowercase(Locale.getDefault()).orEmpty()
                viewModel.onQueryChanged(searchText)
                return true
            }
        })
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}