package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class ViewParcelActivity extends AppCompatActivity {
    private Customer customer;
    private Driver driver;
    private Parcel parcel;
    private Intent intent;

    private TextView deliveryStatus, lineOne, lineTwo, city, postcode, country, contents, deliveryType;

    private View thisA;

    private ParcelContentProvider contentProvider;


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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the item to action bar
        getMenuInflater().inflate(R.menu.view_map_parcel_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = this.getCurrentFocus();
        switch (item.getItemId()) {
            case R.id.action_view_map_parcel:

                return true;
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

    public void setUpView(){
        deliveryStatus = (TextView) findViewById(R.id.deliveryStatus);
        lineOne = (TextView) findViewById(R.id.lineOne);
        lineTwo = (TextView) findViewById(R.id.lineTwo);
        city = (TextView) findViewById(R.id.city);
        postcode = (TextView) findViewById(R.id.postcode);
        country = (TextView) findViewById(R.id.country);
        contents = (TextView) findViewById(R.id.contents);
        deliveryType = (TextView) findViewById(R.id.deliveryType);

        deliveryStatus.setText(parcel.getStatus());
        lineOne.setText(parcel.getAddressLineOne());
        if(parcel.getAddressLineTwo() == null || parcel.getAddressLineTwo().isEmpty()){
            lineTwo.setVisibility(View.GONE);
        }
        lineTwo.setText(parcel.getAddressLineTwo());
        city.setText(parcel.getCity());
        postcode.setText(parcel.getPostcode());
        country.setText(parcel.getCountry());
        contents.setText(parcel.getContents());
        deliveryType.setText(parcel.getServiceType());

        //Set all buttons to unselected colour
        Button buttonProcessing = (Button) findViewById(R.id.processingBtn);
        Button buttonOnRoute = (Button) findViewById(R.id.onRouteBtn);
        Button buttonDelivered = (Button) findViewById(R.id.deliveredBtn);
        Button cancelBtn = (Button) findViewById(R.id.cancelParcel);
        TextView txt = (TextView) findViewById(R.id.txt);

        ImageView image = (ImageView) findViewById(R.id.image);
        try {
            byte[] decodedString = Base64.decode(parcel.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(decodedByte);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(customer != null){
            buttonProcessing.setVisibility(View.GONE);
            buttonOnRoute.setVisibility(View.GONE);
            buttonDelivered.setVisibility(View.GONE);
            cancelBtn.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);

            if (parcel.isProcessing()) {
                cancelBtn.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);
            }
        }else {
            buttonProcessing.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonOnRoute.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonDelivered.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            //Set appropriate button
            if (parcel.isOutForDelivery()) {
                buttonOnRoute.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                cancelBtn.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);

            } else if (parcel.isProcessing()) {
                buttonProcessing.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

            } else if (parcel.isDelivered()) {
                buttonDelivered.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                cancelBtn.setVisibility(View.GONE);
                txt.setVisibility(View.GONE);
            }
        }

    }

    public void cancelParcel(View view) throws MalformedURLException, ExecutionException, InterruptedException {
        contentProvider = new ParcelContentProvider();
        boolean didDelete = (boolean) contentProvider.execute(new URL(getString(R.string.WS_IP) + "/parcels/delete/" + parcel.getId()), "DELETE", null, null).get();
        if (didDelete) {
            Snackbar.make(thisA, "Parcel Canceled (Deleted)", Snackbar.LENGTH_LONG).show();
            Intent dashboard = new Intent(this, DashboardActivity.class);
            dashboard.putExtra("Customer", customer);
            dashboard.putExtra("Driver", driver);
            startActivity(dashboard);
        }else{
            Snackbar.make(thisA, "There was a problem, please try again", Snackbar.LENGTH_LONG).show();
        }
        contentProvider.cancel(true);
    }

    public void changeStatus(View view) throws MalformedURLException {
        //Save changes

        //Get selected button
        Button buttonPressed = (Button) view;
        String nameOfButton = String.valueOf(buttonPressed.getText());

        TextView txt = (TextView) findViewById(R.id.txt);
        
        parcel.setDelivered(false);
        parcel.setProcessing(false);
        parcel.setOutForDelivery(false);

        if(nameOfButton.toUpperCase().equals("DELIVERED")){
            parcel.setDelivered(true);
            Button cancelBtn = (Button)findViewById(R.id.cancelParcel);
            cancelBtn.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);
        }else if(nameOfButton.toUpperCase().equals("ON ROUTE")){
            parcel.setOutForDelivery(true);
            Button cancelBtn = (Button)findViewById(R.id.cancelParcel);
            cancelBtn.setVisibility(View.GONE);
            txt.setVisibility(View.GONE);
        }else  if(nameOfButton.toUpperCase().equals("PROCESSING")){
            parcel.setProcessing(true);
            Button cancelBtn = (Button)findViewById(R.id.cancelParcel);
            cancelBtn.setVisibility(View.VISIBLE);
            txt.setVisibility(View.VISIBLE);
        }

        try {
            contentProvider = new ParcelContentProvider();
            boolean didUpdate = (boolean) contentProvider.execute(new URL(getString(R.string.WS_IP) + "/parcels/update"), "PUT", null, null, parcel).get();
            contentProvider.cancel(true);
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
