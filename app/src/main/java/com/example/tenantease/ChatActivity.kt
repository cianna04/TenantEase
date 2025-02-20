
package com.example.tenantease

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: chatadapter
    private val messageList = mutableListOf<message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView = findViewById(R.id.chat_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        chatAdapter = chatadapter(messageList)
        recyclerView.adapter = chatAdapter

        loadDummyData()

        val messageInput: EditText = findViewById(R.id.message_input)
        val sendButton: ImageView = findViewById(R.id.send_button)
        val backButton: ImageView = findViewById(R.id.back_button)  // Reference to your back button

        sendButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                messageList.add(message("You", messageText, "10:35 AM", "tenant"))
                chatAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messageList.size - 1)
                messageInput.text.clear()
            }
        }

        // Set OnClickListener for the back button
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()  // Optionally finish this activity to remove it from the back stack
        }
    }

    private fun loadDummyData() {
        messageList.add(message("Tenant 1", "Hi, can we install shelves?", "10:30 AM", "tenant"))
        messageList.add(message("Landlord", "Yes, as long as they don't damage the walls.", "10:31 AM", "landlord"))
        messageList.add(message("Property Manager", "Please ensure proper installation.", "10:32 AM", "property_manager"))
        chatAdapter.notifyDataSetChanged()
    }
}
