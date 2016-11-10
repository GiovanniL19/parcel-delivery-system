package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.CustomAdapter;
import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class DashboardActivity extends AppCompatActivity {
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        Customer customer = (Customer) intent.getSerializableExtra("Customer");

        setTitle(customer.getFullName());
        generateTable();


    }

    private void generateTable(){
        Parcel parcel = new Parcel();
        parcel.setParcelID(12345);
        parcel.setServiceType("First Class");

        List<Parcel> parcelItems = new ArrayList<Parcel>();
        parcelItems.add(parcel);

        //Implements custom adapter
        ListAdapter adapter = new CustomAdapter(this, parcelItems);

        ListView dashboardList = (ListView)findViewById(R.id.dashboardList);
        dashboardList.setAdapter(adapter);

        dashboardList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = String.valueOf(parent.getItemAtPosition(position));

                    }
                }
        );
    }
}
