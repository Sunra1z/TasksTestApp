package com.example.taskstestapp.presentation.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskstestapp.R
import com.example.taskstestapp.databinding.FragmentUserDetailsBinding
import kotlinx.coroutines.launch

class UserDetailsFragment : Fragment() {

    private val viewModel: UserDetailsViewModel by viewModels()

    private val args: UserDetailsFragmentArgs by navArgs<UserDetailsFragmentArgs>()

    private var _binding: FragmentUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentUserDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedUserId = args.userId
        binding.btnShowTasks.setOnClickListener {
            val action = UserDetailsFragmentDirections
                .actionUserDetailsFragmentToTaskListFragment(args.userId)
            findNavController().navigate(action)
        }
        viewModel.fetchUser(selectedUserId)

        binding.btnRetry.setOnClickListener {
            viewModel.fetchUser(selectedUserId)
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.uiState.collect { state ->

                    binding.progressBar.isVisible = state.isLoading
                    binding.layoutError.isVisible = state.isError
                    binding.layoutDetails.isVisible = !state.isLoading and !state.isError
                    binding.btnShowTasks.isVisible = !state.isLoading and !state.isError

                    if (state.isError) {
                        Toast.makeText(context, "Error fetching user", Toast.LENGTH_SHORT).show()
                    }

                    state.user?.let { user ->
                        binding.tvDetailsName.text = user.name
                        binding.tvDetailsEmail.text = user.email
                        binding.tvDetailsUsername.text = user.username
                        binding.tvDetailsCity.text = user.address.city
                        binding.tvDetailsSite.text = user.website
                        binding.tvDetailsCompany.text = user.company.name
                        binding.tvDetailsNumber.text = user.phone
                        binding.tvDetailsStreet.text = user.address.street
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}