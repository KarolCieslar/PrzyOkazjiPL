package pl.globoox.przyokazjiv3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import pl.globoox.przyokazjiv3.Utils.LoginRequest;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText editTextUsername = (EditText) findViewById(R.id.editTextUsernameLogin);
        final EditText editTextPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final Button buttonRegisterLink = (Button) findViewById(R.id.buttonRegisterLink);


        buttonRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(openRegister);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = editTextUsername.getText().toString();
                final String password = editTextPassword.getText().toString();
                boolean error = false;

                // CHECK FORM EMPTY
                if (username.isEmpty() || password.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.formEmpty).setNegativeButton(R.string.tryAgain, null).create().show();
                    error = true;
                    return;
                }

                if (error == false) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                String error = jsonResponse.getString("error");
                                if (success == true) {

                                    SessionManagment session;
                                    session = new SessionManagment(getApplicationContext());
                                    session.createUserLoginSession(jsonResponse.getString("uid"), jsonResponse.getString("username"), jsonResponse.getString("email"), jsonResponse.getString("session"), password);
                                    Intent i = new Intent(getApplicationContext(), UserAreaActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage(getText(R.string.loginFailed) + error + ")").setNegativeButton(R.string.tryAgain, null).create().show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }

            }
        });


    }
}
