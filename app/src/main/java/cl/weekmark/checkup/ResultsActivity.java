package cl.weekmark.checkup;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;


import java.util.ArrayList;

import cl.weekmark.checkup.models.Patente;

public class ResultsActivity extends ListActivity {

    ArrayAdapter<String> adapter = null;
    ArrayList<String> listItems = null;

    @Override
    protected void onCreate(Bundle bundle) {
        try {

            super.onCreate(bundle);
            setContentView(R.layout.activity_results);

            listItems=new ArrayList<String>();


            ArrayList<Patente> patentes = (ArrayList<Patente>)getIntent().getSerializableExtra("patentes");
            if (patentes != null) {
                for (Patente p : patentes) {
                    listItems.add("Titulo:" + p.getTitulo() + "\n" + "Resumen" + p.getResumen());
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
                setListAdapter(adapter);
            }
        }catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }
}
