package pl.globoox.przyokazjiv3;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GlobooX on 10.06.2018.
 */

public class SessionCheck extends StringRequest{

    private static final String SESSION_REQUEST_URL = "http://www.globoox.pl/przyokazji/session.php";
    private Map<String, String> params;

    public SessionCheck(String username, String session, Response.Listener<String> listener) {
        super(Request.Method.POST, SESSION_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("password", session);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }



}
