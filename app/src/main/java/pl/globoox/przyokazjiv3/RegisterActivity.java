package pl.globoox.przyokazjiv3;

import android.content.DialogInterface;
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

import pl.globoox.przyokazjiv3.Utils.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText editTextUsername = (EditText) findViewById(R.id.editTextUsernameRegister);
        final EditText editTextEmail = (EditText) findViewById(R.id.editTextEmailRegister);
        final EditText editTextPassword = (EditText) findViewById(R.id.editTextPasswordRegister);
        final EditText editTextPasswordRepeat = (EditText) findViewById(R.id.editTextPasswordRepeatRegister);

        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = editTextUsername.getText().toString();
                final String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();
                final String passwordRepeat = editTextPasswordRepeat.getText().toString();
                boolean error = false;

                // CHECK FORM EMPTY
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || passwordRepeat.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage(R.string.formEmpty).setNegativeButton(R.string.tryAgain, null).create().show();
                    error = true;
                    return;
                }

                // CHECK PASS = REPEAT PASS
                if (!password.toString().equals(passwordRepeat)) {
                    editTextPassword.setError(getText(R.string.passNotMatch));
                    editTextPasswordRepeat.setError(getText(R.string.passNotMatch));
                    error = true;
                    return;
                }

                // CHECK PASS LENGHT
                if (password.length() < 5 || passwordRepeat.length() < 5) {
                    editTextPassword.setError(getText(R.string.passLenghtLow));
                    editTextPasswordRepeat.setError(getText(R.string.passLenghtLow));
                    error = true;
                    return;
                }

                // CHECK NICKNAME LENGHT
                if (username.length() < 3) {
                    editTextUsername.setError(getText(R.string.usernameLenghtLow));
                    error = true;
                    return;
                }

                // CHECK IS EMAIL
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextEmail.setError(getText(R.string.emailNotVaild));
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage(R.string.registerSuccess).setPositiveButton(R.string.registerSuccessButton, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                        }
                                    }).create().show();

                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                    builder.setMessage(getText(R.string.registerError) + error).setNegativeButton(R.string.tryAgain, null).create().show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    };

                    RegisterRequest registerRequest = new RegisterRequest(username, email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}
