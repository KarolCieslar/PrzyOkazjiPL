package pl.globoox.przyokazjiv3.chatOnline;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import pl.globoox.przyokazjiv3.R;
import pl.globoox.przyokazjiv3.SessionManagment;
import pl.globoox.przyokazjiv3.Utils.MessageRequest;
import pl.globoox.przyokazjiv3.Utils.SendMessageRequest;

public class chatMessage extends AppCompatActivity {

    ListView chatMessages;
    ArrayList<String> messagesList = new ArrayList();
    ArrayList<String> senderOrReciver = new ArrayList();
    ArrayList<String> sendTime = new ArrayList();
    String senderUsername;
    String chatUID;
    TextView textViewLoadingMessage;
    EditText editTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_chat_message);

        Bundle extras = getIntent().getExtras();
        chatUID = extras.getString("uid");

        chatMessages = (ListView) findViewById(R.id.listViewChat);
        textViewLoadingMessage = (TextView) findViewById(R.id.textViewLoadingMessage);
        editTextMessage = (EditText) findViewById(R.id.editTextChatEnterText);
        showMessage();


        final Button buttonSend = (Button) findViewById(R.id.buttonChatSendMessage);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonResponse = null;
                        try {
                            jsonResponse = new JSONObject(response);
                            String error = jsonResponse.getString("error");
                            Boolean success = jsonResponse.getBoolean("success");
                            AlertDialog.Builder builder = new AlertDialog.Builder(chatMessage.this);
                            if (success == true){
                                builder.setMessage(error).setPositiveButton(R.string.messageSendButton, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        buttonSend.setEnabled(true);
                                        editTextMessage.setText("");
                                        showMessage();
                                    }
                                }).create().show();
                            } else {
                                builder.setMessage(error).setNegativeButton(R.string.tryAgain, null).create().show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                if (editTextMessage.length() > 0) {
                    buttonSend.setEnabled(false);
                    HashMap<String, String> user = new SessionManagment(getApplicationContext()).getUserDetails();
                    SendMessageRequest SendMessageRequest = new SendMessageRequest(user.get(SessionManagment.KEY_USERNAME), user.get(SessionManagment.KEY_PASSWORD), chatUID, editTextMessage.getText().toString(), responseListener);
                    RequestQueue queue = Volley.newRequestQueue(chatMessage.this);
                    queue.add(SendMessageRequest);
                } else {
                    editTextMessage.setError(getText(R.string.chat_emptyMessage));
                }
            }
        });




    }


    public void showMessage() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    senderUsername = jsonResponse.getString("senderUsername");
                    Integer messagesCount = jsonResponse.getInt("messagesCount");
                    JSONArray messagesArray = jsonResponse.getJSONArray("messages");

                    for (int i = 0; i < messagesCount; i++) {
                        JSONObject jsonObject = messagesArray.getJSONObject(i);
                        messagesList.add(jsonObject.getString("message"));
                        senderOrReciver.add(jsonObject.getString("senderOrReceiver"));
                        sendTime.add(jsonObject.getString("sendTime"));
                    }

                    CustomAdapter customAdapter = new CustomAdapter();
                    chatMessages.setAdapter(customAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        HashMap<String, String> user = new SessionManagment(getApplicationContext()).getUserDetails();
        MessageRequest MessagesRequest = new MessageRequest(user.get(SessionManagment.KEY_USERNAME), user.get(SessionManagment.KEY_PASSWORD), chatUID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(chatMessage.this);
        queue.add(MessagesRequest);
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return messagesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            textViewLoadingMessage.setVisibility(TextView.INVISIBLE);
            if (senderOrReciver.get(position).equalsIgnoreCase("sender")){
                convertView = getLayoutInflater().inflate(R.layout.customlayout_chatmessage_sender, null);
            } else {
                convertView = getLayoutInflater().inflate(R.layout.customlayout_chatmessage_receiver, null);
                TextView textViewUsername = (TextView) convertView.findViewById(R.id.text_message_name);
                textViewUsername.setText(senderUsername);
            }
            TextView textViewBody = (TextView) convertView.findViewById(R.id.text_message_body);
            TextView textViewTime = (TextView) convertView.findViewById(R.id.text_message_time);
            textViewBody.setText(messagesList.get(position));
            textViewTime.setText(sendTime.get(position));

            return convertView;
        }

    }
}
