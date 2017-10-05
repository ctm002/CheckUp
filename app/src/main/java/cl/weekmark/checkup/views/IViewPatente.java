package cl.weekmark.checkup.views;

import cl.weekmark.checkup.models.Patente;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Carlos_Tapia on 05-10-2017.
 */

public interface IViewPatente {

    void setPatente(Patente patente);

    String getHash();

    String getIDW();

    String getNroSolicitud();

    String getCookie();

    void addRequest(StringRequest request);

}
