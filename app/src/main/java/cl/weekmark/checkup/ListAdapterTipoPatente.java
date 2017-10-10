package cl.weekmark.checkup;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cl.weekmark.checkup.models.TipoSolicitud;

/**
 * Created by Carlos_Tapia on 06-10-2017.
 */

public class ListAdapterTipoPatente  extends ArrayAdapter<TipoSolicitud> {

    private Context context;
    private TipoSolicitud[] items;

    public ListAdapterTipoPatente(Context context, int textViewResourceId,
                            TipoSolicitud[] items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }

    public int getCount(){
        return items.length;
    }

    public TipoSolicitud getItem(int position){
        return items[position];
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextSize(16);
        label.setTextColor(Color.BLACK);
        label.setPadding(20,20,20,20);
        label.setText(items[position].getText());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextSize(16);
        label.setTextColor(Color.BLACK);
        label.setPadding(20,20,20,20);
        label.setText(items[position].getText());
        return label;
    }
}
