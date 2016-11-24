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
import android.widget.TextView;

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
        TextView welcome = (TextView) findViewById(R.id.welcomeMsg);

        if(driver == null){
            welcome.setText("Hello " + customer.getFullName());
        }else{
            welcome.setText("Hello " + driver.getFullName());
        }
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
                url = new URL(getString(R.string.WS_IP) + "/parcels/byCustomer/"+ customer.getId());
            }else if(driver != null){
                url = new URL(getString(R.string.WS_IP) + "/parcels/byCreatedId/"+ driver.getId());
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

                TextView processing = (TextView) findViewById(R.id.numberOfProcessing);
                TextView onWay = (TextView) findViewById(R.id.numberOfOnWay);
                TextView totalParcels = (TextView) findViewById(R.id.numberOfParcels);

                totalParcels.setText(String.valueOf(parcelList.size()));

                int processingCount = 0;
                int onWayCount = 0;
                for(int i = 0; i < parcelList.size(); i++){
                    if(parcelList.get(i).isOutForDelivery()){
                        onWayCount++;
                    }else if(parcelList.get(i).isProcessing()){
                        processingCount++;
                    }
                }

                processing.setText(String.valueOf(processingCount));
                onWay.setText(String.valueOf(onWayCount));
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
