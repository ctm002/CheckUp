package cl.weekmark.checkup.presenters;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cl.weekmark.checkup.MainActivity;
import cl.weekmark.checkup.models.Pais;
import cl.weekmark.checkup.views.IViewPais;

/**
 * Created by Carlos_Tapia on 05-10-2017.
 */

public class PaisPresenter {

    private IViewPais _IView;


    public PaisPresenter(IViewPais view)
    {
        _IView = view;
    }

    public void getListPaises()
    {
        String url = "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx/menuPaises";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Response
                        //Log.i("Paises Response->", response);
                        List<Pais> paises = new ArrayList<Pais>();
                        try
                        {
                            JSONObject json = new JSONObject(response);
                            JSONArray childArray = new JSONArray(json.getString("d"));
                            //Log.i("Cantidad de Paises->", Integer.toString(childArray.length()));
                            for (int i = 0; i < childArray.length(); i++) {
                                JSONObject JSONObject = childArray.getJSONObject(i);
                                if (!JSONObject.getString("Nombre").isEmpty()) {
                                    paises.add(new Pais(JSONObject.getString("Pais"), JSONObject.getString("Nombre")));
                                }
                            }
                            _IView.setListPaises(paises);
                        }
                        catch (Exception ex)
                        {
                            Log.d("Error Convert JSON", ex.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volly Error", error.getMessage());
                        NetworkResponse response = error.networkResponse;
                        if (response != null) {

                        }
                    }
                }
        ) {
            private String requestBody = "{}";

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String,String> params =  super.getHeaders();

                Map<String, String> params = null;
                if (params == null) params = new HashMap<String, String>();
                params.put("User-agent", "Mozilla/5.0");
                params.put("Content-Length", Integer.toString(this.requestBody.length()));
                params.put("Cookie",  ((MainActivity) _IView).mNameCookie + "=" +  ((MainActivity) _IView).mValueCookie);
                params.put("Referer", "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx");
                params.put("Origin", "https://ion.inapi.cl");
                params.put("Host", "ion.inapi.cl");
                params.put("Connection", "keep-alive");
                params.put("Accept", "application/json");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                //Log.i("response", response.headers.toString());
                //Map<String, String> responseHeaders = response.headers;
                //String rawCookies = responseHeaders.get("Set-Cookie");
                //String sessionId = rawCookies.split(";")[0].split("=")[1];
                //Log.i("cookies->", sessionId);

                return super.parseNetworkResponse(response);
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    //Gson gson = new Gson();
                    //this.requestBody =  "{}";//gson.toJson("{}");
                    return this.requestBody == null ? null : this.requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    //Log.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody);
                    return null;
                }
            }
        };

        ((MainActivity) _IView).mRequestQueue.add(stringRequest);


    }

}
