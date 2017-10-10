package cl.weekmark.checkup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cl.weekmark.checkup.models.Pais;
import cl.weekmark.checkup.models.Patente;
import cl.weekmark.checkup.models.TipoSolicitud;
import cl.weekmark.checkup.presenters.PaisPresenter;
import cl.weekmark.checkup.presenters.PatentePresenter;
import cl.weekmark.checkup.presenters.TipoSolicitudPresenter;
import cl.weekmark.checkup.views.IViewPais;
import cl.weekmark.checkup.views.IViewPatente;
import cl.weekmark.checkup.views.IViewTipoSolicitud;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Response.Listener<String>, Response.ErrorListener, IViewPais, IViewTipoSolicitud, IViewPatente {

    public  RequestQueue            mRequestQueue;
    private EditText                mTxtNroSolicitud;
    public  String                  mNameCookie = "ASP.NET_SessionId";
    public  String                  mValueCookie;
    private PaisPresenter           mPaisPresenter;
    private TipoSolicitudPresenter  mTipoSolicitudPresenter;
    private PatentePresenter        mPatentePresenter;
    private Spinner                 mCboPaisSolicitante;
    private Spinner                 mCboPaisPrioridad;
    private Spinner                 mCboTipoPatente;
    private String                  mHash = "";
    private String                  mIDW = "";
    private String                  mTipoPatenteSelected;
    private ProgressDialog          mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTxtNroSolicitud = (EditText) findViewById(R.id.TxtNroSolicitud);
        //mTxtNroSolicitud.setText("200502383");

        Button btn = (Button) findViewById(R.id.BtnBuscar);
        btn.setOnClickListener(this);

        this.mCboPaisSolicitante = (Spinner) findViewById(R.id.CboPaisSolicitante);
        this.mCboPaisPrioridad= (Spinner) findViewById(R.id.CboPaisPrioridad);
        this.mCboTipoPatente = (Spinner) findViewById(R.id.CboTipoPatente);
        this.mCboTipoPatente.setSelection(0);

        this.mRequestQueue = Volley.newRequestQueue(this);
        initialize();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mProgressDialog = new ProgressDialog(this);
    }

    private void initialize() {
        /************************************Primera Consulta**************************************/
        String url = "https://ion.inapi.cl/Patente/ConsultaAvanzadaPatentes.aspx";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, this, this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = null;
                if (params == null) params = new HashMap<String, String>();

                params.put("User-agent", "Mozilla/5.0");
                params.put("Host", "ion.inapi.cl");
                params.put("Connection", "keep-alive");
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                Log.i("response", response.headers.toString());
                Map<String, String> responseHeaders = response.headers;
                String rawCookies = responseHeaders.get("Set-Cookie");
                mValueCookie = rawCookies.split(";")[0].split("=")[1];
                Log.i("cookies->", mValueCookie);
                return super.parseNetworkResponse(response);
            }
        };
        addRequest(stringRequest);
        /**Fin*************************************************************************************/
    }

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
        try {
            v.requestFocus();
            mProgressDialog.setMessage("Consultando");
            mProgressDialog.show();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    mPatentePresenter.buscarPorParametros();
                }
            });
            t.join();
            t.start();
        } catch(Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }

    @Override
    public void onResponse(String response) {
        Pattern p = Pattern.compile("^\\s*setHash\\s*\\(\\s*['\"\"]([0-9a-f]*)['\"\"]\\s*,\\s*['\"\"]([0-9]*)['\"\"]\\s*\\)\\s*;", Pattern.MULTILINE);
        Matcher m = p.matcher(response);

        if (m.find()) {
            setHash(m.group(1));
            setIDW(m.group(2));

            mPaisPresenter = new PaisPresenter(this);
            mPaisPresenter.getListPaises();

            mTipoSolicitudPresenter = new TipoSolicitudPresenter(this);
            mTipoSolicitudPresenter.getListTipoSolicitud();

            mPatentePresenter = new PatentePresenter(this);
        }
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
        mCboPaisSolicitante.setAdapter(adapter);
        mCboPaisPrioridad.setAdapter(adapter);
    }

    @Override
    public void setListTipoSolicitud(List<TipoSolicitud> tipos) {

        AdapterView.OnItemSelectedListener onItemSelectedListener2 =
            new AdapterView.OnItemSelectedListener(){

                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    TipoSolicitud tipoSelected = (TipoSolicitud)(parent.getItemAtPosition(position));
                    setTipoPatente(String.valueOf(tipoSelected.getValue()));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}

            };

        TipoSolicitud[] aTipos = tipos.toArray(new TipoSolicitud[tipos.size()]);
        ListAdapterTipoPatente adapter = new ListAdapterTipoPatente(this, android.R.layout.simple_spinner_item, aTipos);
        mCboTipoPatente.setAdapter(adapter);
        mCboTipoPatente.setOnItemSelectedListener(onItemSelectedListener2);
    }

    @Override
    public void setListPatentes(ArrayList<Patente> patentes)
    {
        Patente[] temp =  new Patente[patentes.size()];
        Patente[] aPatentes = patentes.toArray(temp);
        Intent intent = new Intent(getApplicationContext(), ResultsActivity.class);
        intent.putExtra("patentes", aPatentes);
        intent.putExtra("hash", getHash());
        intent.putExtra("idw", getIDW());
        intent.putExtra("cookie", getCookie());

        startActivity(intent);
//        startActivityForResult(intent, 0);
    }

    @Override
    public String getNroSolicitud()
    {
        return mTxtNroSolicitud.getText().toString();
    }

    @Override
    public String getIDW()
    {
        return this.mIDW;
    }

    @Override
    public void setIDW(String idw) {
        this.mIDW = idw;
    }

    @Override
    public String getHash() {
        return mHash;
    }

    @Override
    public void setHash(String hash) {
        this.mHash = hash;
    }

    @Override
    public String getCookie()
    {
        return this.mNameCookie + "=" + this.mValueCookie;
    }

    @Override
    public String getTitulo() {
        return ((TextView) findViewById(R.id.TxtTitulo)).getText().toString();
    }

    @Override
    public String getResumen() {
        return ((TextView) findViewById(R.id.TxtResumen)).getText().toString();
    }

    @Override
    public String getNombreSolicitante() {
        return ((TextView) findViewById(R.id.TxtNombreSolicitante)).getText().toString();
    }

    @Override
    public String getPaisSolicitante() {
        return mCboPaisSolicitante.getSelectedItem().toString();
    }

    @Override
    public String getInventor() {
        return ((TextView) findViewById(R.id.TxtInventor)).getText().toString();
    }

    @Override
    public String getTipoPatente() {
        return this.mTipoPatenteSelected;
    }

    public void setTipoPatente(String tipo)
    {
        this.mTipoPatenteSelected = tipo;
    }

    @Override
    public void addRequest(StringRequest request)
    {
        this.mRequestQueue.add(request);
    }

    @Override
    public String getCip() {
        return ((TextView) findViewById(R.id.TxtCip)).getText().toString();
    }

    @Override
    public String getNroPrioridad() {
        return ((TextView) findViewById(R.id.TxtNroPrioridad)).getText().toString();
    }

    @Override
    public String getRegistro() {
        return ((TextView) findViewById(R.id.TxtNroRegistro)).getText().toString();
    }

    @Override
    public String getPaisPrioridad() {
        return mCboPaisPrioridad.getSelectedItem().toString();
    }


    @Override
    public void setHideProgressDialog(boolean b){

        if(mProgressDialog != null)
        {
            mProgressDialog.hide();
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
