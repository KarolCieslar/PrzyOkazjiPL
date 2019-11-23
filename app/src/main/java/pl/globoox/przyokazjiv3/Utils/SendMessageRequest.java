package pl.globoox.przyokazjiv3.Utils;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GlobooX on 06.06.2018.
 */

public class SendMessageRequest extends StringRequest{
    private static final String SEND_MESSAGES_REQUEST_URL = "http://www.globoox.pl/przyokazji/sendMessage.php";
    private Map<String, String> params;

    public SendMessageRequest(String username, String password, String chatUID, String message, Response.Listener<String> listener) {
        super(Method.POST, SEND_MESSAGES_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("message", message);
        params.put("chatUID", chatUID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
