package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import giovannilenguito.co.uk.parceldelivery.Models.Address;
import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, email, fullName, contactNumber, addressLineOne, addressLineTwo, city, postcode, country;
    private TextView textView10, textView11, textView12, textView13, textView14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTitle("Register Customer");

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        email = (EditText)findViewById(R.id.email);
        fullName = (EditText)findViewById(R.id.fullName);
        contactNumber = (EditText)findViewById(R.id.contactNumber);
        addressLineOne = (EditText)findViewById(R.id.addressLineOne);
        addressLineTwo = (EditText)findViewById(R.id.addressLineTwo);
        city = (EditText)findViewById(R.id.city);
        postcode = (EditText)findViewById(R.id.postcode);
        country = (EditText)findViewById(R.id.country);


        textView10 = (TextView)findViewById(R.id.textView10);
        textView11 = (TextView)findViewById(R.id.textView11);
        textView12 = (TextView)findViewById(R.id.textView12);
        textView13 = (TextView)findViewById(R.id.textView13);
        textView14 = (TextView)findViewById(R.id.textView14);
    }

    public void toggleClicked(View view){
        Switch driverSwitch = (Switch)findViewById(R.id.userType);

        if(driverSwitch.isChecked()){

            addressLineOne.setVisibility(View.GONE);
            addressLineTwo.setVisibility(View.GONE);
            city.setVisibility(View.GONE);
            postcode.setVisibility(View.GONE);
            country.setVisibility(View.GONE);

            textView10.setVisibility(View.GONE);
            textView11.setVisibility(View.GONE);
            textView12.setVisibility(View.GONE);
            textView13.setVisibility(View.GONE);
            textView14.setVisibility(View.GONE);


        }else{
            addressLineOne.setVisibility(View.VISIBLE);
            addressLineTwo.setVisibility(View.VISIBLE);
            city.setVisibility(View.VISIBLE);
            postcode.setVisibility(View.VISIBLE);
            country.setVisibility(View.VISIBLE);

            textView10.setVisibility(View.VISIBLE);
            textView11.setVisibility(View.VISIBLE);
            textView12.setVisibility(View.VISIBLE);
            textView13.setVisibility(View.VISIBLE);
            textView14.setVisibility(View.VISIBLE);
        }

    }

    public void registerCustomer(View view) throws MalformedURLException, ExecutionException, InterruptedException {
        String usN = String.valueOf(username.getText());
        String pass = String.valueOf(password.getText());
        String eM = String.valueOf(email.getText());
        String fullN = String.valueOf(fullName.getText());

        Long contact = null;

        try {
            contact = Long.parseLong(contactNumber.getText().toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        String lineOne = String.valueOf(addressLineOne.getText());
        String lineTwo = String.valueOf(addressLineTwo.getText());
        String cit = String.valueOf(city.getText());
        String postC = String.valueOf(postcode.getText());
        String crty = String.valueOf(country.getText());

        Switch driverSwitch = (Switch)findViewById(R.id.userType);

        if(!usN.equals("") && !pass.equals("") && !eM.equals("") && !fullN.equals("") && !lineOne.equals("") && !cit.equals("") && !postC.equals("") && !crty.equals("") || driverSwitch.isChecked()){
            UserHTTPManager userHTTPManager = new UserHTTPManager();

            if(driverSwitch.isChecked()){
                Driver driver = new Driver(eM, usN, pass, fullN, contact);

                driver.setContactNumber(contact);

                userHTTPManager.execute(new URL(getString(R.string.WS_IP) +  "/driver/new"), "POST", "driver", driver, getString(R.string.WS_IP)).get();
            }else{
                Address address = new Address();
                address.setAddressLineOne(lineOne);
                address.setAddressLineTwo(lineTwo);
                address.setCity(cit);
                address.setPostcode(postC);
                address.setCountry(crty);
                Customer customer = new Customer(eM, usN, pass, fullN, contact, address);

                userHTTPManager.execute(new URL(getString(R.string.WS_IP) +  "/customer/new"), "POST", "customer", customer, getString(R.string.WS_IP)).get();
            }

            Snackbar.make(view, "Account Created", Snackbar.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            //Hide keyboard
            if (view != null) {
                InputMethodManager inputMethod = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }else{
            Snackbar.make(view, "Please fill in all required fields", Snackbar.LENGTH_SHORT).show();
        }
    }
}
