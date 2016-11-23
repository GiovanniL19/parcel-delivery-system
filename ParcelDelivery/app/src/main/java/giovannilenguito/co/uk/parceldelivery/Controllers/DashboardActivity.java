package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Adapters.ParcelAdapter;
import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class DashboardActivity extends AppCompatActivity {
    private Customer customer = null;
    private Driver driver = null;
    //private SQLiteDatabaseController database;

    private ParcelContentProvider contentProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();

        if(intent.getSerializableExtra("Customer") != null){
            customer = (Customer) intent.getSerializableExtra("Customer");
        }else{
            driver = (Driver) intent.getSerializableExtra("Driver");
        }

        setTitle("Your Parcels");

        //set up database sqlite
        //database = new SQLiteDatabaseController(this, null, null, 0);

        //Get parcels
        generateTable();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the item to action bar
        if(driver != null){
            getMenuInflater().inflate(R.menu.add_parcel_button, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_parcel:
                Intent intent = new Intent(this, AddParcelActivity.class);
                intent.putExtra("Driver", driver);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public <T> T getContent() throws MalformedURLException {
        try {
            URL url = null;
            //MAKE URL
            if(customer != null){
                //parcelList = database.getRowsByCustomer(customer.getId()); //SQLITE
                url = new URL("http://10.205.205.198:9998/parcels/byCustomer/"+ customer.getId());
            }else if(driver != null){
                //parcelList = database.getRowsByDriver(driver.getId());
                url = new URL("http://10.205.205.198:9998/parcels/byCreatedId/"+ driver.getId());
            }

            //GET CONTENT
            return (T) contentProvider.execute(url, "GET", null, "ARRAY").get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void generateTable() {
        List<Parcel> parcelList;
        contentProvider = new ParcelContentProvider();

        try {
            Object parcels = getContent();
            if(parcels instanceof List) {
                parcelList = (List) parcels;
                contentProvider.cancel(true);
                //Implements custom adapter
                ListAdapter adapter = new ParcelAdapter(this, parcelList);

                ListView dashboardList = (ListView) findViewById(R.id.dashboardList);
                dashboardList.setAdapter(adapter);

                dashboardList.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Parcel selectedParcel = (Parcel) parent.getItemAtPosition(position);
                                Intent intent = new Intent(view.getContext(), ViewParcelActivity.class);
                                intent.putExtra("Parcel", selectedParcel);
                                intent.putExtra("Customer", customer);
                                intent.putExtra("Driver", driver);
                                startActivity(intent);
                            }
                        }
                );
            }
        }catch(Exception e){
            e.printStackTrace();
            Snackbar.make(findViewById(R.id.activity_dashboard), "There was an error", Snackbar.LENGTH_LONG).show();
        }
    }
}
