package cl.weekmark.checkup;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import cl.weekmark.checkup.models.ParametrosConsultaCaratula;
import cl.weekmark.checkup.models.Patente;

public class ResultsActivity extends AppCompatActivity {

    ArrayList<String> listItems = null;


    @Override
    protected void onCreate(Bundle bundle) {
        try {

            final String hash = getIntent().getExtras().getString("hash");
            final String idw = getIntent().getExtras().getString("idw");
            final String cookie = getIntent().getExtras().getString("cookie");

            super.onCreate(bundle);
            setContentView(R.layout.activity_results);
            listItems=new ArrayList<String>();
            Patente[] patentes = (Patente[])getIntent().getSerializableExtra("patentes");
            if (patentes != null) {

                ListAdapterPatente adapter = new ListAdapterPatente(this, new ArrayList<Patente>(Arrays.asList(patentes)));
                final ListView lv = (ListView) findViewById(R.id.lv_patentes);
                if (lv != null)
                    lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Patente p =  (Patente)lv.getItemAtPosition(position);
                        if (p == null)  return;

                        ParametrosConsultaCaratula postData = new ParametrosConsultaCaratula();
                        postData.setHash(hash);
                        postData.setIDW(idw);
                        postData.setOri(1);
                        postData.setSol_Nro(p.getNroSolicitud());

                        Gson gson = new Gson();
                        final String requestBody = gson.toJson(postData);
                        Log.i("post_data->", requestBody);
                        String url = "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx/GetCaratula";

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            if (jsonResponse.has("d")) {
                                                //Patente p =  (Patente)lv.getItemAtPosition(position);
                                                //Intent intent = new Intent(getApplicationContext(), DetailsPatenteActivity.class);
                                                //intent.putExtra("patente", p);
                                                //startActivity(intent);
                                            }
                                            //_View.setHideProgressDialog(true);
                                        } catch (Exception ex) {
                                            Log.e("Error Convert JSON", ex.getMessage());
                                            //AlertDialog.Builder builder = new AlertDialog.Builder(_View.getActivity());
                                            //builder.setMessage(ex.getMessage());
                                            //builder.setTitle("Sargent 2017");
                                            //builder.setPositiveButton("OK",null);
                                            //AlertDialog alert = builder.create();
                                            //alert.show();
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
                        )
                        {
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
                                    //Log.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody);
                                    return null;
                                }
                            }
                        };

                        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                        mRequestQueue.add(stringRequest);
                    }
                });
            }

        }catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }
}
