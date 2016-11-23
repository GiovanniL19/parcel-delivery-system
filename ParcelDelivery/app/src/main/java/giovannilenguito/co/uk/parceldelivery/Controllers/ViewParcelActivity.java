package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class ViewParcelActivity extends AppCompatActivity {
    Customer customer;
    Driver driver;
    Parcel parcel;
    Intent intent;

    TextView deliveryStatus, lineOne, lineTwo, city, postcode, country, contents, deliveryType;

    View thisA;
    private SQLiteDatabaseController database;

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

        //set up database
        database = new SQLiteDatabaseController(this, null, null, 0);

        thisA = findViewById(R.id.activity_view_parcel);
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

    public void cancelParcel(View view){
        //if(database.deleteParcel(parcel.getId())){
        //    Snackbar.make(thisA, "Parcel Canceled (Deleted)", Snackbar.LENGTH_LONG).show();
        //    Intent dashboard = new Intent(this, DashboardActivity.class);
        //    dashboard.putExtra("Customer", customer);
        //    startActivity(dashboard);
       // }else{
       //     Snackbar.make(thisA, "There was a problem, please try again", Snackbar.LENGTH_LONG).show();
       // }
    }

    public void changeStatus(View view){
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


        if(database.updateParcel(parcel) > 0){
            //Set all buttons to unselected colour
            Button buttonProcessing = (Button) findViewById(R.id.processingBtn);
            Button buttonOnRoute = (Button) findViewById(R.id.onRouteBtn);
            Button buttonDelivered = (Button) findViewById(R.id.deliveredBtn);

            buttonProcessing.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonOnRoute.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            buttonDelivered.setBackgroundColor(getResources().getColor(R.color.colorAccent));

            buttonPressed.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


            Snackbar.make(thisA, "Updated Parcel", Snackbar.LENGTH_SHORT).show();
        }else{
            Snackbar.make(thisA, "Error updating parcel", Snackbar.LENGTH_SHORT).show();
        }
    }
}
