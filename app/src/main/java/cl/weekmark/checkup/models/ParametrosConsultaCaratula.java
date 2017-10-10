package cl.weekmark.checkup.models;

/**
 * Created by Carlos_Tapia on 03-10-2017.
 */

public class ParametrosConsultaCaratula {

    private String IDW;
    private String Hash;
    private int Ori;
    private String Sol_Nro;

    public ParametrosConsultaCaratula(){}

    public void setIDW(String IDW) {
        this.IDW = IDW;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public void setOri(int ori) {
        Ori = ori;
    }

    public void setSol_Nro(String sol_Nro) {
        Sol_Nro = sol_Nro;
    }
}