package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Adapters.ParcelAdapter;
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
        customer = (Customer) intent.getSerializableExtra("Customer");

        setTitle("Your Parcels");
        generateTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the item to action bar
        getMenuInflater().inflate(R.menu.add_parcel_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_parcel:
                Intent intent = new Intent(this, AddParcelActivity.class);
                intent.putExtra("Customer", customer);
                startActivity(intent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void generateTable(){
        Parcel parcel = new Parcel();
        parcel.setId(12345);
        parcel.setServiceType("First Class");

        List<Parcel> parcelItems = new ArrayList<Parcel>();
        parcelItems.add(parcel);

        //Implements custom adapter
        ListAdapter adapter = new ParcelAdapter(this, parcelItems);

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
