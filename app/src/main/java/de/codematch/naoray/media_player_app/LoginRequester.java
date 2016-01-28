package de.codematch.naoray.media_player_app;

import com.android.volley.Request;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by schmidt on 04.12.2015.
 */
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class LoginRequester extends Request<JSONObject>{

    private Listener<JSONObject> listener;
    private Map<String, String> params;

    /**
     * Constructor
     * @param url URL of the Server
     * @param params Parameters, which will be sent to Server
     * @param reponseListener
     * @param errorListener
     */
    public LoginRequester(String url, Map<String, String> params,
                          Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    /**
     * Constructor
     * @param method
     * @param url URL of the Server
     * @param params Parameters, which will be sent to Server
     * @param reponseListener
     * @param errorListener
     */
    public LoginRequester(int method, String url, Map<String, String> params,
                          Listener<JSONObject> reponseListener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    /**
     * Returns a Map of parameters to be used for a POST or PUT request.
     * @return Map<String, String>
     * @throws com.android.volley.AuthFailureError
     */
    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    };

    /**
     * perform delivery of the parsed
     * response to their listeners.  The given response is guaranteed to
     * be non-null; responses that fail to parse are not delivered.
     * @param response
     */
     @Override
    protected void deliverResponse(JSONObject response) {
        listener.onResponse(response);
    }


    /**
     * parse the raw network response
     * and return an appropriate response type
     * @param response Response from the network
     * @return The parsed response, or null in the case of an error
     */
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

}