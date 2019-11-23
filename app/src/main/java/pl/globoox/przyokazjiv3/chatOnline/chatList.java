package pl.globoox.przyokazjiv3.chatOnline;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
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
import pl.globoox.przyokazjiv3.Utils.ChatRequest;

public class chatList extends AppCompatActivity {

    ListView messages;
    ArrayList<String> idList = new ArrayList<>();
    ArrayList<String> receiverList = new ArrayList<>();
    ArrayList<String> roadList = new ArrayList<>();
    ArrayList<String> messageList = new ArrayList<>();
    ArrayList<String> timeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages_list);
        final TextView textViewError = (TextView) findViewById(R.id.textViewError);

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    Integer messagesCount = jsonResponse.getInt("messagesCount");
                    if (messagesCount == 0) {
                        textViewError.setText(R.string.noMessagesToDisplay);
                    } else {
                        JSONArray messagesArray = jsonResponse.getJSONArray("messages");
                        Log.d("TAG", String.valueOf(messagesArray));

                        for (int i = 0; i < messagesCount; i++) {
                            JSONObject jsonObject = messagesArray.getJSONObject(i);
                            idList.add(jsonObject.getString("uid"));
                            receiverList.add(jsonObject.getString("receiver"));
                            roadList.add(jsonObject.getString("road"));
                            messageList.add(jsonObject.getString("message"));
                            timeList.add(jsonObject.getString("timestamp"));
                        }


                        messages = (ListView) findViewById(R.id.listViewMessages);
                        CustomAdapter customAdapter = new CustomAdapter();
                        messages.setAdapter(customAdapter);

                        messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getApplicationContext(), chatMessage.class);
                                intent.putExtra("uid", idList.get(position));
                                startActivity(intent);
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        HashMap<String, String> user = new SessionManagment(getApplicationContext()).getUserDetails();
        ChatRequest MessagesRequest = new ChatRequest(user.get(SessionManagment.KEY_USERNAME), user.get(SessionManagment.KEY_PASSWORD), responseListener);
        RequestQueue queue = Volley.newRequestQueue(chatList.this);
        queue.add(MessagesRequest);



    }


    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return receiverList.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout_messageslist, null);
            TextView textViewSender = (TextView) convertView.findViewById(R.id.textViewSender);
            TextView textViewRoad = (TextView) convertView.findViewById(R.id.textViewRoad);
            TextView textViewMessages = (TextView) convertView.findViewById(R.id.textViewMessage);
            TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewTime);

            textViewSender.setText(receiverList.get(position));
            textViewRoad.setText(roadList.get(position));
            textViewTime.setText(timeList.get(position));

            if (messageList.get(position).length() > 100){
                textViewMessages.setText(messageList.get(position).substring(0,100) + "...");
            } else {
                textViewMessages.setText(messageList.get(position));
            }
            return convertView;
        }

    }


}
