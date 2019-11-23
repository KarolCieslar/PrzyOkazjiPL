package pl.globoox.przyokazjiv3.Utils;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GlobooX on 06.06.2018.
 */

public class MessageRequest extends StringRequest{
    private static final String MESSAGES_REQUEST_URL = "http://www.globoox.pl/przyokazji/getMessage.php";
    private Map<String, String> params;

    public MessageRequest(String username, String password, String chatUID, Response.Listener<String> listener) {
        super(Method.POST, MESSAGES_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("chatUID", chatUID);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
