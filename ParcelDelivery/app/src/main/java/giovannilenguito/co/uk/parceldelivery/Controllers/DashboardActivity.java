package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Adapters.ParcelAdapter;
import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.Models.SQLiteDatabaseController;
import giovannilenguito.co.uk.parceldelivery.NotificationService;
import giovannilenguito.co.uk.parceldelivery.R;

public class DashboardActivity extends AppCompatActivity {
    private Customer customer = null;
    private Driver driver = null;
    private SQLiteDatabaseController database = new SQLiteDatabaseController(this, null, null, 0);
    public static Intent notificationIntent;
    private ParcelHTTPManager parcelHTTPManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();

        if(intent.getSerializableExtra("Customer") != null){
            customer = (Customer) intent.getSerializableExtra("Customer");
        }else if(intent.getSerializableExtra("Driver") != null){
            driver = (Driver) intent.getSerializableExtra("Driver");

            //Start notification service and check for notifications
            notificationIntent = new Intent(this, NotificationService.class);
            startService(notificationIntent);
        }else{
            database.dropUsers();
            //Go to login
            Intent login = new Intent(this, MainActivity.class);
            startActivity(login);
        }

        setTitle("Your Parcels");
        TextView welcome = (TextView) findViewById(R.id.welcomeMsg);

        ImageView backImage = (ImageView) findViewById(R.id.backImage);
        if(driver != null){
            welcome.setText("Hello driver!");
            findViewById(R.id.newFAB).setVisibility(View.INVISIBLE);
            backImage.setImageResource(R.drawable.box);
        }else{
            welcome.setText("Hello " + customer.getFullName());
            findViewById(R.id.newFAB).setVisibility(View.VISIBLE);
            backImage.setImageResource(R.drawable.present);
        }

        //Get parcels
        generateTable();
    }

    @Override
    //Stays blank to prevent back button from doing anything
    public void onBackPressed() {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the item to action bar
        getMenuInflater().inflate(R.menu.add_parcel_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_account:
                Intent accountIntent = new Intent(this, AccountActivity.class);
                accountIntent.putExtra("Driver", driver);
                accountIntent.putExtra("Customer", customer);
                startActivity(accountIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void newParcel(View view){
        Intent intent = new Intent(this, AddParcelActivity.class);
        intent.putExtra("Customer", customer);
        startActivity(intent);
    }

    public <T> T getContent() throws MalformedURLException {
        try {
            URL url = null;
            //MAKE URL
            if(customer != null){
                url = new URL(getString(R.string.WS_IP) + "/parcels/byCustomer/"+ customer.getId());
            }else if(driver != null){
                url = new URL(getString(R.string.WS_IP) + "/parcels/byDriver/"+ driver.getId());
            }

            //GET CONTENT
            return (T) parcelHTTPManager.execute(url, "GET", null, "ARRAY").get();
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void generateTable() {
        List<Parcel> parcelList;
        parcelHTTPManager = new ParcelHTTPManager();

        try {
            Object parcels = getContent();
            if(parcels instanceof List) {
                parcelList = (List) parcels;
                parcelHTTPManager.cancel(true);

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

    //Update view if new parcels found from service via local broadcaster
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            generateTable();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("updateUI"));
        generateTable();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onPause();
    }

}
