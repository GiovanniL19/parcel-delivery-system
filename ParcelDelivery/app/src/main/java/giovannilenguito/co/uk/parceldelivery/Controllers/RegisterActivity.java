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

import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, password, email, fullName, contactNumber, addressLineOne, addressLineTwo, city, postcode, country;
    private DatabaseController database;

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

        database = new DatabaseController(this, null, null, 0);
    }

    public void registerCustomer(View view){
        String usN = String.valueOf(username.getText());
        String pass = String.valueOf(password.getText());
        String eM = String.valueOf(email.getText());
        String fullN = String.valueOf(fullName.getText());
        int contact = 0;

        try {
            contact = Integer.parseInt(contactNumber.getText().toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        String lineOne = String.valueOf(addressLineOne.getText());
        String lineTwo = String.valueOf(addressLineTwo.getText());
        String cit = String.valueOf(city.getText());
        String postC = String.valueOf(postcode.getText());
        String crty = String.valueOf(country.getText());

        Switch driverSwitch = (Switch)findViewById(R.id.userType);
        if(driverSwitch.isChecked()){
            Driver driver = new Driver(eM, usN, pass, fullN, 0, lineOne, lineTwo, cit, postC, crty);
            driver.setContactNumber(contact);

            //add the customer and return the id
            int id = database.addDriver(driver);
        }else{
            Customer customer = new Customer(eM, usN, pass, fullN, 0, lineOne, lineTwo, cit, postC, crty, null);
            customer.setContactNumber(contact);

            //add the customer and return the id
            //int id = database.addCustomer(customer); //SQLITE

            //RETURNS OBJECT
            (T) new UserDAOController().execute(new URL("http://10.205.205.198:9998/drivers/byUsername/"+ username), "POST", "driver").get();
        }

        //Hide keyboard
        if (view != null) {
            InputMethodManager inputMethod = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        Snackbar.make(view, "Account Created", Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}
