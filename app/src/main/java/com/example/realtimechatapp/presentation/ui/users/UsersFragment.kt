package com.example.realtimechatapp.presentation.ui.users

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimechatapp.R
import com.example.realtimechatapp.data.repository.UserRepositoryImpl
import com.example.realtimechatapp.data.source.remote.FirebaseUserDataSource
import com.example.realtimechatapp.databinding.FragmentUsersBinding
import com.example.realtimechatapp.domain.model.User
import com.example.realtimechatapp.domain.usecase.GetUsersUseCase
import com.example.realtimechatapp.presentation.utils.Utils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UsersFragment : Fragment() {

    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: UserListAdapter

    private val viewModel: UserListViewModel by viewModels (
        factoryProducer = { UserListViewModel.Factory }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstancedState: Bundle?) {
        super.onViewCreated(view, savedInstancedState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewModel.getUsers()
        setUpRecyclerView()
        observeUsers()
        setupListener()
    }

    private fun setUpRecyclerView() {
        adapter = UserListAdapter {user ->
            val action = UsersFragmentDirections.actionUsersFragmentToChatFragment(user.uid)
            findNavController().navigate(action)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupListener(){
        with(binding) {
            btnLogOut.setOnClickListener {
                logout()
            }
        }
    }

    private fun logout() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout Confirmation")
            .setMessage("Do you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_usersFragment_to_loginFragment)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun observeUsers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userList.collectLatest { userList ->
                adapter.submitList(userList)
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition(userList.size - 1)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}