package pl.globoox.przyokazjiv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import pl.globoox.przyokazjiv3.chatOnline.chatList;

public class UserAreaActivity extends AppCompatActivity {

    SessionManagment session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_area);

        final TextView textViewusername = (TextView) findViewById(R.id.textViewUsername);
        final Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        final Button buttonMessage = (Button) findViewById(R.id.buttonMessage);
        session = new SessionManagment(getApplicationContext());


        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if(session.checkLogin())
            finish();

        // get user data from session
        HashMap<String, String> user = session.getUserDetails();

        // get name
        String uid = user.get(SessionManagment.KEY_UID);

        // get email
        String username = user.get(SessionManagment.KEY_USERNAME);

        // get email
        String email = user.get(SessionManagment.KEY_EMAIL);

        // Show user data on activity
        textViewusername.setText(username + " (" + uid + ") \n" + email);


        buttonLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                session.logoutUser();
                finish();
            }
        });


        buttonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserAreaActivity.this, chatList.class);
                startActivity(intent);
            }
        });

    }
}
