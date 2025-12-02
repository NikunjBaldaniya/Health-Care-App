package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareapp.Adapter.MessageAdapter;
import com.example.healthcareapp.Model.ChatMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AiChatActivity extends AppCompatActivity {

    private String API_KEY;
    // We use Openrouter Api Key with "mistral" ai model
    private static final String MODEL = "mistralai/mistral-small-3.2-24b-instruct:free";

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private List<ChatMessage> messageList;
    private MessageAdapter adapter;
    TextView txtWelcome;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ai_chat);


        // Find View By Id's
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.messageEditText);
        ImageButton sendBtn = findViewById(R.id.sendBtn);
        txtWelcome =findViewById(R.id.txtWelcome);
        btnBack =findViewById(R.id.btnBack);
        API_KEY = "<PROVIDE_API_KEY>"; // Get API Key From : https://openrouter.ai/

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(adapter);

        sendBtn.setOnClickListener(v -> {
            String message = messageEditText.getText().toString().trim();
            if (!message.isEmpty()) {
                messageList.add(new ChatMessage(message, ChatMessage.SENT_BY_ME));
                adapter.notifyItemInserted(messageList.size() - 1);
                messageEditText.setText("");
                txtWelcome.setVisibility(View.GONE);
                sendToOpenRouter(message);

            }
        });

        // Back to Dashboard Activity
        btnBack.setOnClickListener(v ->{
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }

    private void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new ChatMessage(message, sentBy));
            adapter.notifyItemInserted(messageList.size() - 1);
            chatRecyclerView.scrollToPosition(messageList.size() - 1);
        });
    }

    private void sendToOpenRouter(String prompt) {
        addToChat("Thinking...", ChatMessage.SENT_BY_TYPING);

        try {
            JSONObject json = new JSONObject();
            json.put("model", MODEL);

            JSONArray messages = new JSONArray();

            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a helpful and knowledgeable health and fitness assistant. Always answer in short, clear, and polite sentences. If the question is not related to health, politely say: 'I'm here only to help with health and fitness-related topics.' Do not generate long paragraphs or answer unrelated questions.");

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);

            messages.put(systemMessage);
            messages.put(userMessage);

            json.put("messages", messages);

            // Create Request
            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    "https://openrouter.ai/api/v1/chat/completions",
                    json,
                    response -> {
                        removeTyping();
                        try {
                            String reply = response
                                    .getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");
                            addToChat(reply.trim(), ChatMessage.SENT_BY_BOT);
                        } catch (Exception e) {
                            addToChat("Parse Error: " + e.getMessage(), ChatMessage.SENT_BY_BOT);
                        }
                    },
                    error -> {
                        removeTyping();
                        addToChat("Server Busy or No Internet Connection", ChatMessage.SENT_BY_BOT);
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + API_KEY);
                    return headers;
                }
            };

            // Add to Volley Queue
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);

        } catch (Exception e) {
            removeTyping();
            addToChat("Internal Error: " + e.getMessage(), ChatMessage.SENT_BY_BOT);
        }
    }

    private void removeTyping() {
        runOnUiThread(() -> {
            for (int i = 0; i < messageList.size(); i++) {
                if (messageList.get(i).getSentBy().equals(ChatMessage.SENT_BY_TYPING)) {
                    messageList.remove(i);
                    adapter.notifyItemRemoved(i);
                    break;
                }
            }
        });
    }
}