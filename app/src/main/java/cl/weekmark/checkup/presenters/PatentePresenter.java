package cl.weekmark.checkup.presenters;

import android.app.AlertDialog;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cl.weekmark.checkup.models.ParametrosConsultaBusqueda;
import cl.weekmark.checkup.models.ParametrosConsultaCaratula;
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

    public void buscarPorParametros()
    {
        String nroSolicitud =  _View.getNroSolicitud();
        String titulo = _View.getTitulo();
        String resumen = _View.getResumen();
        String solicitante = _View.getNombreSolicitante();
        String paisSolicitante = _View.getPaisSolicitante();
        String inventor = _View.getInventor();
        String tipoPatente = _View.getTipoPatente();
        String cip = _View.getCip();
        String registro = _View.getRegistro();
        String paisPrioridad = _View.getPaisPrioridad();
        String nroPrioridad = _View.getNroPrioridad();

        String hash = _View.getHash();
        String idw = _View.getIDW();
        final String cookie = _View.getCookie();

        ParametrosConsultaBusqueda paramentrosPost = new ParametrosConsultaBusqueda();
        paramentrosPost.Hash = hash;
        paramentrosPost.IDW = idw;
        paramentrosPost.d.nrosolicitud = nroSolicitud;
        paramentrosPost.d.titulo = titulo;
        paramentrosPost.d.resumen = resumen;
        paramentrosPost.d.solicitante = solicitante;
        paramentrosPost.d.paissolicitante = paisSolicitante;
        paramentrosPost.d.inventor = inventor;
        paramentrosPost.d.tipopatente = tipoPatente;
        paramentrosPost.d.cip   = cip;
        paramentrosPost.d.registro = registro;
        paramentrosPost.d.paisprioridad = paisPrioridad;
        paramentrosPost.d.nroprioridad = nroPrioridad;

        Gson gson = new Gson();
        final String requestBody = gson.toJson(paramentrosPost);
        Log.i("post_data->", requestBody);

        String url = "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx/Busqueda";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("d")) {

                                String d = jsonResponse.getString("d");
                                JSONObject jsonObject = new JSONObject(d);
                                if(!jsonObject.has("ErrorMessage")) {
                                    String strHash = jsonObject.getString("Hash");
                                    String strPatentes = jsonObject.getString("Patentes");
                                    Log.i("New Hash", strHash);
                                    Log.i("Patentes", strPatentes);

                                    ArrayList<Patente> patentes = new ArrayList<Patente>();

                                    JSONArray arr = new JSONArray(strPatentes);
                                    for (int i = 0; i < arr.length(); i++) {
                                        Patente p = new Patente(arr.getJSONObject(i));
                                        patentes.add(p);
                                    }
                                    _View.setHash(strHash);
                                    _View.setListPatentes(patentes);
                                }else
                                {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(_View.getActivity());
                                    builder.setMessage(jsonObject.getString("ErrorMessage"));
                                    builder.setTitle("Sargent 2017");
                                    builder.setPositiveButton("OK",null);
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }

                            _View.setHideProgressDialog(true);
                        } catch (Exception ex) {
                            Log.e("Error Convert JSON", ex.getMessage());
                            AlertDialog.Builder builder = new AlertDialog.Builder(_View.getActivity());
                            builder.setMessage(ex.getMessage());
                            builder.setTitle("Sargent 2017");
                            builder.setPositiveButton("OK",null);
                            AlertDialog alert = builder.create();
                            alert.show();
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
                            _View.setListPatentes(null);
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


    public void buscarCaratula()
    {
        String titulo = _View.getTitulo();
        String resumen = _View.getResumen();
        String solicitante = _View.getNombreSolicitante();
        String paisSolicitante = _View.getPaisSolicitante();
        String inventor = _View.getInventor();
        String tipoPatente = _View.getTipoPatente();
        String cip = _View.getCip();
        String registro = _View.getRegistro();
        String paisPrioridad = _View.getPaisPrioridad();
        String paisNroPrioridad = _View.getNroPrioridad();


        String solicitud =  _View.getNroSolicitud();
        String hash = _View.getHash();
        String idw = _View.getIDW();
        final String cookie = _View.getCookie();

        if (!solicitud.isEmpty()) {
            ParametrosConsultaCaratula parametros = new ParametrosConsultaCaratula();
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
                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("d")) {

                                    String d = jsonResponse.getString("d");
                                    JSONObject jsonObject = new JSONObject(d);
                                    String strHash = jsonObject.getString("Hash");
                                    String strPatentes = jsonObject.getString("Patentes");
                                    Log.i("New Hash", strHash);
                                    Log.i("Patentes", strPatentes);

                                    ArrayList<Patente> patentes = new ArrayList<Patente>();

                                    JSONArray arr = new JSONArray(strPatentes);
                                    for (int i=0; i < arr.length(); i++) {
                                        Patente p = new Patente(arr.getJSONObject(i));
                                        patentes.add(p);
                                    }
                                    _View.setHash(strHash);
                                    _View.setListPatentes(patentes);
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
                                _View.setListPatentes(null);
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
