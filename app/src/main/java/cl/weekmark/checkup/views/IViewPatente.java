package cl.weekmark.checkup.views;

import cl.weekmark.checkup.models.Patente;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.List;

public interface IViewPatente {

    void setListPatentes(ArrayList<Patente> patentes);

    String getHash();

    void setHash(String hash);

    String getIDW();

    void setIDW(String idw);

    String getNroSolicitud();

    String getCookie();

    String getTitulo();

    String getResumen();

    String getNombreSolicitante();

    String getPaisSolicitante();

    String getInventor();

    String getTipoPatente();

    void addRequest(StringRequest request);

    String getCip();

    String getNroPrioridad();

    String getRegistro();

    String getPaisPrioridad();
}
