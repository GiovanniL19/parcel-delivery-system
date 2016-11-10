package giovannilenguito.co.uk.parceldelivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Models.Parcel;


/**
 * Created by giovannilenguito on 08/11/2016.
 */

public class CustomAdapter extends ArrayAdapter<Parcel>{

    public CustomAdapter(Context context, List array) {
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

        title.setText(String.valueOf(item.getParcelID()));

        return customView;
    }
}
