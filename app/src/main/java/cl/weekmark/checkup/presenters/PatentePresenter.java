package cl.weekmark.checkup.presenters;

import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import cl.weekmark.checkup.models.ParametrosConsultaPatente;
import cl.weekmark.checkup.models.Patente;
import cl.weekmark.checkup.views.IViewPatente;

/**
 * Created by Carlos_Tapia on 05-10-2017.
 */

public class PatentePresenter {

    private IViewPatente _View;

    public PatentePresenter(IViewPatente view) {
        _View = view;
    }

    public void buscar()
    {
        String solicitud =  _View.getNroSolicitud();
        String hash = _View.getHash();
        String idw = _View.getIDW();
        final String cookie = _View.getCookie();

        if (!solicitud.isEmpty()) {
            ParametrosConsultaPatente parametros = new ParametrosConsultaPatente();
            parametros.setHash(hash);
            parametros.setIDW(idw);
            parametros.setOri(1);
            parametros.setSol_Nro(solicitud);

            Gson gson = new Gson();
            final String requestBody = gson.toJson(parametros);
            Log.i("post_data->", requestBody);

            String url = "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx/GetCaratula";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.has("d")) {
                                    String content = json.getString("d");
                                    Patente patente = new Patente();
                                    patente.setContent(content);
                                    _View.setPatente(patente);
                                }
                            } catch (Exception ex) {
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
                                Patente patente = new Patente();
                                patente.setContent(error.getStackTrace().toString());
                                _View.setPatente(patente);
                            }
                        }
                    }
            ) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = null;
                    if (params == null) params = new HashMap<String, String>();

                    params.put("User-agent", "Mozilla/5.0");
                    params.put("Content-Length", Integer.toString(requestBody.length()));
                    params.put("Host", "ion.inapi.cl");
                    params.put("Referer", "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx");
                    params.put("Cookie", cookie);
                    params.put("Origin", "https://ion.inapi.cl");
                    params.put("Connection", "keep-alive");
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    return super.parseNetworkResponse(response);
                }

                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        Log.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody);
                        return null;
                    }
                }
            };
            _View.addRequest(stringRequest);
        }
    }
}
