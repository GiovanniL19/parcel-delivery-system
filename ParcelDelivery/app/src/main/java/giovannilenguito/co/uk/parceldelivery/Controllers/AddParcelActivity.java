package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class AddParcelActivity extends AppCompatActivity {
    Customer customer;
    Spinner deliveryType;
    EditText recipientName, contents, deliveryDate, lineOne, lineTwo, city, country, postcode;
    Intent intent;

    private DatabaseController  database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parcel);
        setTitle("New Parcel");
        //Get customer
        intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("Customer");

        deliveryType = (Spinner) findViewById(R.id.deliveryType);
        recipientName = (EditText) findViewById(R.id.recipientName);
        contents = (EditText) findViewById(R.id.contents);
        deliveryDate = (EditText) findViewById(R.id.deliveryDate);
        lineOne = (EditText) findViewById(R.id.lineOne);
        lineTwo = (EditText) findViewById(R.id.lineTwo);
        city = (EditText) findViewById(R.id.city);
        country = (EditText) findViewById(R.id.country);
        postcode = (EditText) findViewById(R.id.postcode);

        //set up database
        database = new DatabaseController(this, null, null, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //add the item to action bar
        getMenuInflater().inflate(R.menu.create_parcel_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view = this.getCurrentFocus();
        switch (item.getItemId()) {
            case R.id.action_create_parcel:
                try {
                    addParcel(view);
                } catch (Exception e) {
                    Snackbar.make(view, "There was an error with the delivery date", Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public Intent getParentActivityIntent() {
        intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("Customer", customer);
        return intent;
    }

    private void addParcel(View view) throws ParseException {
        Parcel parcel = new Parcel();

        String spinnerChoice = deliveryType.getSelectedItem().toString();
        String recipientN = recipientName.getText().toString();
        String cont = contents.getText().toString();
        String deliveryD = deliveryDate.getText().toString();
        String lineO = lineOne.getText().toString();
        String lineT = lineTwo.getText().toString();
        String cit = city.getText().toString();
        String coun = country.getText().toString();
        String post = postcode.getText().toString();


        parcel.setServiceType(spinnerChoice);
        parcel.setRecipientName(recipientN);
        parcel.setContents(cont);
        parcel.setDeliveryDate(deliveryD);
        parcel.setAddressLineOne(lineO);
        parcel.setAddressLineTwo(lineT);
        parcel.setCity(cit);
        parcel.setCountry(coun);
        parcel.setPostcode(post);

        parcel.setCreatedByID(customer.getId());

        //need to set driver

        Date dateBooked = new Date();
        parcel.setDateBooked(dateBooked.toString());
        parcel.setProcessing(true);

        database.addRow(parcel);

        intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("Customer", customer);
        startActivity(intent);

    }
}
