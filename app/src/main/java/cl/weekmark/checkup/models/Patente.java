package cl.weekmark.checkup.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Carlos_Tapia on 05-10-2017.
 */

public class Patente implements Serializable {
    private  String content;

    private  String mNroSolicitud;

    public String getContent() {
        return content;
    }

    public String getNroSolicitud() {
        return mNroSolicitud;
    }

    public void setNroSolicitud(String mNroSolicitud) {
        this.mNroSolicitud = mNroSolicitud;
    }

    public void setResumen(String mResumen) {
        this.mResumen = mResumen;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public void setTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }

    public String getSolicitante() {
        return mSolicitante;
    }

    public void setSolicitante(String mSolicitante) {
        this.mSolicitante = mSolicitante;
    }

    private  String mResumen;
    private  String mTitulo;

    public String getResumen() {
        return mResumen;
    }

    private  String mSolicitante;
    
    public Patente() {
    }

    public Patente(JSONObject json) throws JSONException {
        setTitulo(json.getString("TITULO"));
        setResumen(json.getString("RESUMEN"));
        //setSolicitante(json.getString("Solicitante"));
        setNroSolicitud(json.getString("sol_nro"));
    }


    public String toJSON() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
