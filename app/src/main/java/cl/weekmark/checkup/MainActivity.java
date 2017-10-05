package cl.weekmark.checkup;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cl.weekmark.checkup.models.Pais;
import cl.weekmark.checkup.models.TipoSolicitud;
import cl.weekmark.checkup.presenters.PaisPresenter;
import cl.weekmark.checkup.presenters.TipoSolicitudPresenter;
import cl.weekmark.checkup.views.IViewPais;
import cl.weekmark.checkup.views.IViewTipoSolicitud;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Response.Listener<String>, Response.ErrorListener, IViewPais, IViewTipoSolicitud {

    private TextView mTextView;
    public RequestQueue mRequestQueue;
    private EditText mEdit;
    public String mKeyCookie = "ASP.NET_SessionId";
    public String mValueSessionId;
    private PaisPresenter mPaisPresenter;
    private TipoSolicitudPresenter mTipoSolicitudPresenter;
    private Spinner mSpinnerPaises;
    private Spinner mSpinnerTiposSolicitud;
    private String hash = "";
    private String idw = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mEdit = (EditText) findViewById(R.id.TxtEditNroSolicitud);
        mEdit.setText("200502383");

        Button btn = (Button) findViewById(R.id.BtnBuscar);
        btn.setOnClickListener(this);

        mTextView = (TextView) findViewById(R.id.TxtResultados);
        mTextView.setMovementMethod(new ScrollingMovementMethod());
        mTextView.setText("");


        this.mSpinnerPaises = (Spinner) findViewById(R.id.CboPaises);
        this.mSpinnerTiposSolicitud = (Spinner) findViewById(R.id.CboTipos);

        initialize();
    }

    private void initialize() {
        /************************************Primera Consulta**************************************/
        mRequestQueue = Volley.newRequestQueue(this);
        String url = "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String,String> params =  super.getHeaders();

                Map<String, String> params = null;
                if (params == null) params = new HashMap<String, String>();

                params.put("User-agent", "Mozilla/5.0");
                params.put("Host", "ion.inapi.cl");
                params.put("Connection", "keep-alive");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                // since we don't know which of the two underlying network vehicles
                // will Volley use, we have to handle and store session cookies manually
                Log.i("response", response.headers.toString());
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                mValueSessionId = rawCookies.split(";")[0].split("=")[1];
                Log.i("cookies->", mValueSessionId);
                return super.parseNetworkResponse(response);
            }
        };
        mRequestQueue.add(stringRequest);
        /**Fin*********************************************************************************/}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {

        v.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTextView.getWindowToken(), 0);
        mTextView.setText("Consultando......");

        String numSolicitud = mEdit.getText().toString();
        if (!numSolicitud.isEmpty()) {

            final ParametrosConsultaPatente parametros = new ParametrosConsultaPatente();
            parametros.setHash(hash);
            parametros.setIDW(idw);
            parametros.setOri(1);
            parametros.setSol_Nro(numSolicitud);

            Gson gson = new Gson();
            final String requestBody = gson.toJson(parametros);
            Log.i("post_data->", requestBody);

            String url = "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx/GetCaratula";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            //Response
                            //Log.i("Segundo Response->", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                if (json.has("d")) {
                                    String content = json.getString("d");
                                    mTextView.setText(content);
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
                                mTextView.setText("Error" + error.getStackTrace());
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
                    params.put("Cookie", mKeyCookie + "=" + mValueSessionId);
                    params.put("Origin", "https://ion.inapi.cl");
                    params.put("Connection", "keep-alive");
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
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        Log.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody);
                        return null;
                    }
                }
            };

            mRequestQueue.add(stringRequest);
        }
    }

    @Override
    public void onResponse(String response) {
        Pattern p = Pattern.compile("^\\s*setHash\\s*\\(\\s*['\"\"]([0-9a-f]*)['\"\"]\\s*,\\s*['\"\"]([0-9]*)['\"\"]\\s*\\)\\s*;", Pattern.MULTILINE);
        Matcher m = p.matcher(response);

        if (m.find()) {
            hash = m.group(1);
            idw = m.group(2);
        }

        mPaisPresenter = new PaisPresenter(this);
        mPaisPresenter.getListPaises();

        mTipoSolicitudPresenter = new TipoSolicitudPresenter(this);
        mTipoSolicitudPresenter.getListTipoSolicitud();

        //mTextView.setText("idw->" +  idw + " hash->" + hash);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        NetworkResponse response = error.networkResponse;

        if (response != null) {
            Log.d("That didn't work!", response.data.toString());
        }
    }

    @Override
    public void setListPaises(List<Pais> paises) {

        ArrayList<String> sPaises = new ArrayList<String>();

        for (Pais p : paises) {
            sPaises.add(p.Nombre);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sPaises);
        mSpinnerPaises.setAdapter(adapter);
    }

    @Override
    public void setListTipoSolicitud(List<TipoSolicitud> tipos) {
        ArrayList<String> sTipos = new ArrayList<String>();

        for (TipoSolicitud t : tipos) {
            sTipos.add(t.tipo);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sTipos);
        mSpinnerTiposSolicitud.setAdapter(adapter);
    }
}
