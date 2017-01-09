package giovannilenguito.co.uk.parceldelivery.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;


/**
 * Created by giovannilenguito on 08/11/2016.
 */

public class ParcelAdapter extends ArrayAdapter<Parcel>{

    public ParcelAdapter(Context context, List array) {
        //array is the array of items to display, the custom_row is the custom row
        super(context, R.layout.custom_row, array);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflateLayout = LayoutInflater.from(getContext());
        //sets data for one row
        View customView = inflateLayout.inflate(R.layout.custom_row, parent, false);

        Parcel item = getItem(position);
        TextView title = (TextView) customView.findViewById(R.id.title);
        TextView status = (TextView) customView.findViewById(R.id.statusText);
        TextView type = (TextView) customView.findViewById(R.id.type);

        switch(item.getStatus()){
            case "Delivered":
                status.setTextColor(Color.parseColor("#009688"));
                break;
            case "Out For Delivery":
                status.setTextColor(Color.parseColor("#D84315"));
                break;
        }

        type.setText(item.getServiceType());
        title.setText(item.getTitle());
        status.setText(item.getStatus());
        return customView;
    }
}
