package cl.weekmark.checkup.models;

/**
 * Created by Carlos_Tapia on 06-10-2017.
 */

public class ParametrosConsultaBusqueda {

    public ParametrosConsultaBusqueda() {
        d = new Busqueda();
    }

    public String Hash;
    public String IDW;
    public Busqueda d;


    public class Busqueda {
        public String nrosolicitud;
        public String titulo = "";
        public String resumen = "";
        public String solicitante = "";
        public String paissolicitante = "Seleccionar";
        public String inventor = "";
        public String cip = "";
        public String registro = "";
        public String paisprioridad = "Seleccionar";
        public String nroprioridad = "";
        public String fechapresentacion1 = "";
        public String fechapresentacion2 = "";
        public String fechaprioridad1 = "";
        public String fechaprioridad2 = "";
        public String tipopatente = "9";
    }
}




