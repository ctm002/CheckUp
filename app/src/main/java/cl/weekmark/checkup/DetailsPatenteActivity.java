package cl.weekmark.checkup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cl.weekmark.checkup.models.Patente;

public class DetailsPatenteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_patente);
        Object object = getIntent().getSerializableExtra("patente");

        if (object instanceof Patente)
        {
            Patente p = (Patente)object;
            ((TextView) findViewById(R.id.details_nro_solicitud)).setText(p.getNroSolicitud());
            ((TextView) findViewById(R.id.details_titulo)).setText(p.getTitulo());
            ((TextView) findViewById(R.id.details_resumen)).setText(p.getResumen());
            ((TextView) findViewById(R.id.details_solicitante)).setText(p.getSolicitante());
            ((TextView) findViewById(R.id.details_inventor)).setText("");
            ((TextView) findViewById(R.id.details_representante)).setText(p.getRepresentante());
            ((TextView) findViewById(R.id.details_fecha_registro)).setText(p.getFechaRegistro());
            ((TextView) findViewById(R.id.details_fecha_de_publicacion)).setText(p.getFechaPublicacion());
        }
    }
}
