package com.example.realtimechatapp.presentation.ui.chat

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.realtimechatapp.data.repository.ChatRepositoryImpl
import com.example.realtimechatapp.data.repository.NetworkRepositoryImpl
import com.example.realtimechatapp.data.repository.NotificationRepositoryImpl
import com.example.realtimechatapp.data.repository.UserRepositoryImpl
import com.example.realtimechatapp.data.source.remote.FirebaseChatDataSource
import com.example.realtimechatapp.data.source.remote.FirebaseUserDataSource
import com.example.realtimechatapp.databinding.FragmentChatBinding
import com.example.realtimechatapp.domain.model.Chat
import com.example.realtimechatapp.domain.usecase.GetMessagesUseCase
import com.example.realtimechatapp.domain.usecase.GetUserNameUseCase
import com.example.realtimechatapp.domain.usecase.PushNotificationUseCase
import com.example.realtimechatapp.domain.usecase.SendMessageUseCase
import com.example.realtimechatapp.presentation.NetworkChangeReceiver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var curId: String
    private lateinit var networkReceiver: NetworkChangeReceiver

    private val viewModel: ChatViewModel by viewModels(
        factoryProducer = {ChatViewModel.Factory}
    )
    private val args: ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        curId = auth.currentUser!!.uid

        setUpRecyclerView()
        observeMessages()
        setUpListener()
        setUpNetworkReceiver()
        observeName()
        getFCMToken()
        viewModel.getMessages(viewModel.getChatId(auth.currentUser!!.uid, args.uid))
        viewModel.getUserName(args.uid)
    }

    private fun setUpNetworkReceiver() {
        networkReceiver = NetworkChangeReceiver {
            viewModel.onNetworkAvailable(requireContext())
        }
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        requireContext().registerReceiver(networkReceiver, intentFilter)
    }

    private fun observeName() {
        lifecycleScope.launch {
            viewModel.userName.collectLatest { userName ->
                binding.tvName.text = userName
            }
        }
    }

    private fun setUpRecyclerView () {
        chatAdapter = ChatAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = chatAdapter
    }

    private fun observeMessages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
//                Log.d("MESSAGES", "${messages.size}")
                chatAdapter.submitList(messages)
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition(0)
                }
            }
        }
    }

    private fun setUpListener () {
        with(binding) {
            btnSend.setOnClickListener {
                sendMessage()
            }
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun sendMessage() {
        val messageText = binding.etMessage.text.toString().trim()
        if (messageText.isEmpty()) return
        val chat = Chat(
            id = curId,
            senderId = curId,
            receiverId = args.uid,
            text = messageText,
        )

        viewModel.sendMessage(chat, curId, requireContext())
        binding.etMessage.text.clear()
    }

    private fun getFCMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener{
            if(it.isSuccessful){
                val fcmToken = it.result
                FirebaseFirestore.getInstance().collection("users").document(curId)
                    .update("fcmToken", fcmToken.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        requireContext().unregisterReceiver(networkReceiver)
    }

}