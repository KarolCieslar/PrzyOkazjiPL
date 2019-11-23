package pl.globoox.przyokazjiv3.Utils;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GlobooX on 06.06.2018.
 */

public class ChatRequest extends StringRequest{
    private static final String CHAT_REQUEST_URL = "http://www.globoox.pl/przyokazji/getChat.php";
    private Map<String, String> params;

    public ChatRequest(String username, String password, Response.Listener<String> listener) {
        super(Method.POST, CHAT_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
