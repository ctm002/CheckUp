package cl.weekmark.checkup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


import cl.weekmark.checkup.models.Patente;

/**
 * Created by Carlos_Tapia on 06-10-2017.
 */

public class ListAdapterPatente extends BaseAdapter{


    protected Activity context;
    protected ArrayList<Patente> items;


    public ListAdapterPatente(Activity context, ArrayList<Patente> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_patente, null);
        }

        Patente p = this.items.get(position);

        TextView txtNroSolicitud = (TextView) v.findViewById(R.id.solicitud_v);
        txtNroSolicitud.setText(p.getNroSolicitud());

        TextView txtCip = (TextView) v.findViewById(R.id.cip_v);
        txtCip.setText(p.getCip());


        TextView txtTitulo = (TextView) v.findViewById(R.id.titulo_v);
        txtTitulo.setText(p.getTitulo());
        return v;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
