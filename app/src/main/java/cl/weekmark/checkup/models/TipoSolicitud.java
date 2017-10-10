package cl.weekmark.checkup.models;

/**
 * Created by Carlos_Tapia on 05-10-2017.
 */

public class TipoSolicitud {

    private String valor;

    public String getValue() {
        return valor;
    }

    public void setValue(String valor) {
        this.valor = valor;
    }


    private  String tipo;
    public String getText() {
        return tipo;
    }

    public void setText(String tipo) {
        this.tipo = tipo;
    }

    public TipoSolicitud(String valor, String tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }
}
