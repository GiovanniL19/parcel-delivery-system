package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class ViewParcelActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Customer customer;
    private Driver driver;
    private Parcel parcel;
    private Intent intent;

    private TextView deliveryStatus, lineOne, lineTwo, city, postcode, country, contents, deliveryType, collectionLocationTxt;

    private View thisA;

    private ParcelHTTPManager parcelHTTPManager;

    //LOCATION
    private double lat;
    private double lon;
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;
    private boolean locationReady = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parcel);

        intent = getIntent();

        parcel = (Parcel) intent.getSerializableExtra("Parcel");

        if(intent.getSerializableExtra("Customer") != null){
            customer = (Customer) intent.getSerializableExtra("Customer");
        }else{
            driver = (Driver) intent.getSerializableExtra("Driver");
        }

        setTitle(parcel.getTitle());
        setUpView();


        thisA = findViewById(R.id.activity_view_parcel);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connection) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
                locationReady = true;
            }
        }
    }
    @Override
    public void onConnectionSuspended(int i) {locationReady = false;}
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {locationReady = false;}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the item to action bar
        getMenuInflater().inflate(R.menu.view_map_parcel_button, menu);
        if(driver != null) {
            MenuItem collectIcon = menu.findItem(R.id.action_collect_parcel);
            collectIcon.setVisible(false);
        }else{
            if(!parcel.getLocationId().isProcessing()) {
                MenuItem collectIcon = menu.findItem(R.id.action_collect_parcel);
                collectIcon.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = this.getCurrentFocus();
        switch (item.getItemId()) {
            case R.id.action_view_map_parcel:
                //go to google maps with parcel longitude and latitude
                Uri gmmIntentUri = Uri.parse("geo:" + parcel.getLocationId().getLatitude() +"," + parcel.getLocationId().getLongitude() + "?q=" + parcel.getLocationId().getLatitude() +"," + parcel.getLocationId().getLongitude() + "(" + parcel.getTitle() + ")");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                startActivity(mapIntent);
                return true;
            case R.id.action_collect_parcel:
                parcel.getLocationId().setCollecting(!parcel.getLocationId().isCollecting());

                //Set all buttons to hidden
                Button buttonProcessing = (Button) findViewById(R.id.processingBtn);
                Button buttonOnRoute = (Button) findViewById(R.id.onRouteBtn);
                Button buttonDelivered = (Button) findViewById(R.id.deliveredBtn);
                FloatingActionButton cancelButton = (FloatingActionButton) findViewById(R.id.cancelParcel);

                if(parcel.getLocationId().isCollecting()){
                    buttonProcessing.setVisibility(View.INVISIBLE);
                    buttonOnRoute.setVisibility(View.INVISIBLE);
                    buttonDelivered.setVisibility(View.INVISIBLE);
                    cancelButton.setVisibility(View.INVISIBLE);
                }else{

                    buttonProcessing.setVisibility(View.VISIBLE);
                    buttonOnRoute.setVisibility(View.VISIBLE);
                    buttonDelivered.setVisibility(View.VISIBLE);
                    cancelButton.setVisibility(View.VISIBLE);
                }


                try {
                    parcelHTTPManager = new ParcelHTTPManager();
                    boolean didUpdate = (boolean) parcelHTTPManager.execute(new URL(getString(R.string.WS_IP) + "/parcels/update"), "PUT", null, null, parcel).get();
                    parcelHTTPManager.cancel(true);
                    if (didUpdate) {
                        if(parcel.getLocationId().isCollecting()) {
                            deliveryStatus.setText("Recipient is Collecting Parcel");
                        }else{
                            deliveryStatus.setText(parcel.getLocationId().getStatus());
                        }
                        Snackbar.make(thisA, "Updated Parcel", Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(thisA, "Did not update", Snackbar.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Snackbar.make(thisA, "Error updating parcel", Snackbar.LENGTH_SHORT).show();
                }
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public Intent getParentActivityIntent() {
        intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("Customer", customer);
        intent.putExtra("Driver", driver);
        return intent;
    }

    @RequiresApi(api = android.os.Build.VERSION_CODES.LOLLIPOP)
    public void setUpView(){
        deliveryStatus = (TextView) findViewById(R.id.deliveryStatus);
        lineOne = (TextView) findViewById(R.id.lineOne);
        lineTwo = (TextView) findViewById(R.id.lineTwo);
        city = (TextView) findViewById(R.id.city);
        postcode = (TextView) findViewById(R.id.postcode);
        country = (TextView) findViewById(R.id.country);
        contents = (TextView) findViewById(R.id.contents);
        deliveryType = (TextView) findViewById(R.id.deliveryType);

        collectionLocationTxt = (TextView) findViewById(R.id.collectionLocationTxt);

        deliveryStatus.setText(parcel.getLocationId().getStatus());
        lineOne.setText(parcel.getAddressId().getAddressLineOne());
        if(parcel.getAddressId().getAddressLineTwo() == null || parcel.getAddressId().getAddressLineTwo().isEmpty()){
            lineTwo.setVisibility(View.GONE);
        }
        lineTwo.setText(parcel.getAddressId().getAddressLineTwo());
        city.setText(parcel.getAddressId().getCity());
        postcode.setText(parcel.getAddressId().getPostcode());
        country.setText(parcel.getAddressId().getCountry());
        contents.setText(parcel.getContents());
        deliveryType.setText(parcel.getServiceType());
        collectionLocationTxt.setText("Parcel to be collected at: " + parcel.getCollectionLineOne() + ", " + parcel.getCollectionPostCode());

        //Set all buttons to unselected colour
        Button buttonProcessing = (Button) findViewById(R.id.processingBtn);
        Button buttonOnRoute = (Button) findViewById(R.id.onRouteBtn);
        Button buttonDelivered = (Button) findViewById(R.id.deliveredBtn);
        FloatingActionButton cancelBtn = (FloatingActionButton) findViewById(R.id.cancelParcel);
        TextView txt = (TextView) findViewById(R.id.txt);

        ImageView image = (ImageView) findViewById(R.id.image);

        if(customer != null){
            txt.setText("As requested, your parcel will be delivered via " + parcel.getServiceType() + " on " + parcel.getDeliveryDate() + " to " + parcel.getRecipientName());
        }else if(driver != null){
            txt.setText("This parcel is to be delivered via " + parcel.getServiceType() + " on " + parcel.getDeliveryDate() + " to " + parcel.getRecipientName());
        }
        try {
            byte[] decodedString = Base64.decode(parcel.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(customer != null) {
            buttonProcessing.setEnabled(false);
            buttonOnRoute.setEnabled(false);
            buttonDelivered.setEnabled(false);
            cancelBtn.setVisibility(View.GONE);
        }
        buttonProcessing.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        buttonOnRoute.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        buttonDelivered.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        //Set appropriate button
        if (parcel.getLocationId().isOutForDelivery()) {
            buttonOnRoute.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (parcel.getLocationId().isProcessing()) {
            buttonProcessing.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (parcel.getLocationId().isDelivered()) {
            buttonDelivered.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            cancelBtn.setVisibility(View.GONE);
        }

        if(parcel.getLocationId().isCollecting()) {
            FloatingActionButton cancelButton = (FloatingActionButton) findViewById(R.id.cancelParcel);

            buttonProcessing.setVisibility(View.INVISIBLE);
            buttonOnRoute.setVisibility(View.INVISIBLE);
            buttonDelivered.setVisibility(View.INVISIBLE);
            cancelButton.setVisibility(View.INVISIBLE);

            deliveryStatus.setText("Recipient is Collecting Parcel");
        }
    }

    public void cancelParcel(View view) throws MalformedURLException, ExecutionException, InterruptedException {
        parcelHTTPManager = new ParcelHTTPManager();
        boolean didDelete = (boolean) parcelHTTPManager.execute(new URL(getString(R.string.WS_IP) + "/parcels/delete/" + parcel.getParcelId()), "DELETE", null, null).get();
        if (didDelete) {
            Snackbar.make(thisA, "Parcel Canceled (Deleted)", Snackbar.LENGTH_LONG).show();
            Intent dashboard = new Intent(this, DashboardActivity.class);
            dashboard.putExtra("Customer", customer);
            dashboard.putExtra("Driver", driver);
            startActivity(dashboard);
        }else{
            Snackbar.make(thisA, "There was a problem, please try again", Snackbar.LENGTH_LONG).show();
        }
        parcelHTTPManager.cancel(true);
    }

    @RequiresApi(api = android.os.Build.VERSION_CODES.LOLLIPOP)
    public void changeStatus(View view) throws MalformedURLException {
        //Save changes

        //Get selected button
        Button buttonPressed = (Button) view;
        String nameOfButton = String.valueOf(buttonPressed.getText());
        
        parcel.getLocationId().setDelivered(false);
        parcel.getLocationId().setProcessing(false);
        parcel.getLocationId().setOutForDelivery(false);
        parcel.getLocationId().setLatitude(lat);
        parcel.getLocationId().setLongitude(lon);

        FloatingActionButton cancelBtn = (FloatingActionButton)findViewById(R.id.cancelParcel);

        if(nameOfButton.toUpperCase().equals("DELIVERED")){
            parcel.getLocationId().setDelivered(true);
            cancelBtn.setVisibility(View.GONE);
            deliveryStatus.setText("Delivered");
        }else if(nameOfButton.toUpperCase().equals("ON ROUTE")){
            parcel.getLocationId().setOutForDelivery(true);
            cancelBtn.setVisibility(View.GONE);

            deliveryStatus.setText("On Route");
        }else  if(nameOfButton.toUpperCase().equals("PROCESSING")){
            parcel.getLocationId().setProcessing(true);
            cancelBtn.setVisibility(View.VISIBLE);
            deliveryStatus.setText("Processing");
        }

        try {
            parcelHTTPManager = new ParcelHTTPManager();
            boolean didUpdate = (boolean) parcelHTTPManager.execute(new URL(getString(R.string.WS_IP) + "/parcel/update"), "PUT", null, null, parcel).get();
            parcelHTTPManager.cancel(true);
            if (didUpdate) {
                //Set all buttons to unselected colour
                Button buttonProcessing = (Button) findViewById(R.id.processingBtn);
                Button buttonOnRoute = (Button) findViewById(R.id.onRouteBtn);
                Button buttonDelivered = (Button) findViewById(R.id.deliveredBtn);

                buttonProcessing.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonOnRoute.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                buttonDelivered.setBackgroundColor(getResources().getColor(R.color.colorAccent));

                buttonPressed.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


                Snackbar.make(thisA, "Updated Parcel", Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(thisA, "Did not update", Snackbar.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
            Snackbar.make(thisA, "Error updating parcel", Snackbar.LENGTH_SHORT).show();
        }
    }
}
