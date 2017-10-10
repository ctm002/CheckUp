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
    private String mCip;
    private  String mResumen;
    private  String mTitulo;
    private  String mSolicitante;


    public void setNroSolicitud(String mNroSolicitud) {
        this.mNroSolicitud = mNroSolicitud;
    }

    public String getNroSolicitud() {
        return mNroSolicitud;
    }

    public void setResumen(String mResumen) {
        this.mResumen = mResumen;
    }

    public String getResumen() {
        return mResumen;
    }

    public void setTitulo(String mTitulo) {
        this.mTitulo = mTitulo;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public void setSolicitante(String mSolicitante) {
        this.mSolicitante = mSolicitante;
    }

    public String getSolicitante() {
        return mSolicitante;
    }

    public void setCip(String cip)
    {
        this.mCip = cip;
    }

    public String getCip()
    {
        return this.mCip;

    }

    public Patente() {
    }

    public Patente(JSONObject json) throws JSONException {

        if (json.has("titulo"))
            setTitulo(json.getString("titulo"));

        if (json.has("resumen"))
            setResumen(json.getString("resumen"));

        if (json.has("nrosolicitud"))
            setNroSolicitud(json.getString("nrosolicitud"));

        if (json.has("cip"))
            setCip(json.getString("cip"));
    }

    public String toJSON() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
