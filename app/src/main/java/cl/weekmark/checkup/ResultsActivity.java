package cl.weekmark.checkup;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.Arrays;

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
            Patente[] patentes = (Patente[])getIntent().getSerializableExtra("patentes");
            if (patentes != null) {
                ListAdapterPatente adapter = new ListAdapterPatente(this, new ArrayList<Patente>(Arrays.asList(patentes)));
                final ListView lv = (ListView) findViewById(R.id.lv_patentes);
                if (lv != null)
                    lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Patente p =  (Patente)lv.getItemAtPosition(position);
                        Log.i("Titulo->", p.getTitulo());
                        Intent intent = new Intent(getApplicationContext(), DetailsPatenteActivity.class);
                        intent.putExtra("patente", p);
                        startActivity(intent);
                    }
                });
            }

        }catch (Exception ex)
        {
            Log.e("ERROR", ex.getMessage());
        }
    }
}
