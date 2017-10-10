package cl.weekmark.checkup;
import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;



import java.util.ArrayList;

import cl.weekmark.checkup.models.Patente;

public class ResultsActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter = null;
    ArrayList<String> listItems = null;

    @Override
    protected void onCreate(Bundle bundle) {
        try {

            super.onCreate(bundle);
            setContentView(R.layout.activity_results);



            listItems=new ArrayList<String>();


            ArrayList<Patente> patentes = (ArrayList<Patente>)getIntent().getSerializableExtra("patentes");
            /*
            if (patentes != null) {
                for (Patente p : patentes) {

                    StringBuilder sb = new StringBuilder();
                    sb.append("Nro Solicitud\n");
                    sb.append( p.getNroSolicitud());

                    sb.append("\nInt. CI.\n");
                    sb.append(p.getCip());

                    sb.append("\nTitulo\n");
                    sb.append(p.getTitulo());
                    listItems.add(sb.toString());
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1 , listItems);
                setListAdapter(adapter);
            }*/


            ListAdapterPatente adapter = new ListAdapterPatente(this, patentes);
            ListView lv = (ListView) findViewById(R.id.lv_patentes);
            if (lv != null)
                lv.setAdapter(adapter);



        }catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }
}
