package giovannilenguito.co.uk.parceldelivery.controller;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import giovannilenguito.co.uk.parceldelivery.handler.UserHTTPHandler;
import giovannilenguito.co.uk.parceldelivery.model.Customer;
import giovannilenguito.co.uk.parceldelivery.model.Driver;
import giovannilenguito.co.uk.parceldelivery.handler.SQLiteDatabaseHandler;
import giovannilenguito.co.uk.parceldelivery.R;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    private EditText username, password;
    private Switch isDriver;

    private UserHTTPHandler userHTTPHandler;

    private SQLiteDatabaseHandler database = new SQLiteDatabaseHandler(this, null, null, 0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        isDriver = (Switch)findViewById(R.id.isDriver);
        getSupportActionBar().hide();

        intent = new Intent(this, DashboardActivity.class);

        //Check if user is logged in
        if (database.getAllCustomers().size() > 0){
            //User logged in
            intent.putExtra("Customer", database.getAllCustomers().get(0));
            startActivity(intent);
        }else{
            if(database.getAllDrivers().size() > 0){
                intent.putExtra("Driver", database.getAllDrivers().get(0));
                startActivity(intent);
            }
        }
    }

    public void goToRegister(View view){
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public <T> T isAuthenticatedCustomer(String username) throws MalformedURLException {
        try {
            //JSON
            userHTTPHandler = new UserHTTPHandler();
            if(isDriver.isChecked()){
                return (T) userHTTPHandler.execute(new URL(getString(R.string.WS_IP) +  "/driver/findByUsername/"+ username), "GET", "driver", null, getString(R.string.WS_IP)).get();
            }else{
                return (T) userHTTPHandler.execute(new URL(getString(R.string.WS_IP) + "/customer/findByUsername/"+ username), "GET", "customer", null, getString(R.string.WS_IP)).get();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void login(View view) throws MalformedURLException, ExecutionException, InterruptedException, JSONException {
        String sUsername = username.getText().toString();
        String sPassword = password.getText().toString();

        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if(!sUsername.matches("")){
            if(!sPassword.matches("")){
                Snackbar.make(view, "Attempting login...", Snackbar.LENGTH_LONG).show();

                Object user = isAuthenticatedCustomer(sUsername);
                userHTTPHandler.cancel(true);
                if(user == null){
                    hideSoftKeyboard();
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(view, "Incorrect login details", Snackbar.LENGTH_LONG).show();
                }else {
                    if (user instanceof Customer) {
                        Customer customer = (Customer) user;
                        if (customer.getPassword().equals(sPassword)) {
                            intent.putExtra("Customer", (Customer) user);
                            userHTTPHandler.cancel(true);

                            userHTTPHandler = new UserHTTPHandler();

                            //LOG USER LOGIN
                            JSONObject jsonLog = new JSONObject();
                            jsonLog.put("title", "Login");
                            jsonLog.put("message", customer.getFullName() + " logged in at " + new Date().toString());
                            jsonLog.put("customerId", customer.getCustomerId());
                            userHTTPHandler.execute(new URL(getString(R.string.WS_IP) +  "/logs/new"), "LOG", null, jsonLog, getString(R.string.WS_IP)).get();
                            database.addCustomer(customer);
                            userHTTPHandler.cancel(true);
                            startActivity(intent);
                        } else {
                            hideSoftKeyboard();
                            progressBar.setVisibility(View.INVISIBLE);
                            Snackbar.make(view, "Incorrect password", Snackbar.LENGTH_LONG).show();
                        }
                    } else if (user instanceof Driver) {
                        Driver driver = (Driver) user;
                        if (driver.getPassword().equals(sPassword)) {
                            intent.putExtra("Driver", (Driver) user);
                            userHTTPHandler.cancel(true);

                            userHTTPHandler = new UserHTTPHandler();
                            //LOG USER LOGIN
                            JSONObject jsonLog = new JSONObject();
                            jsonLog.put("title", "Login");
                            jsonLog.put("message", driver.getFullName() + " logged in at " + new Date().toString());
                            jsonLog.put("driverId", driver.getDriverId());
                            userHTTPHandler.execute(new URL(getString(R.string.WS_IP) +  "/logs/new"), "LOG", null, jsonLog, getString(R.string.WS_IP)).get();
                            database.addDriver(driver);
                            database.addNumberOfParcels(0, driver.getDriverId());

                            userHTTPHandler.cancel(true);
                            startActivity(intent);
                        } else {
                            hideSoftKeyboard();
                            progressBar.setVisibility(View.INVISIBLE);
                            Snackbar.make(view, "Incorrect password", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }else {
                progressBar.setVisibility(View.INVISIBLE);
                hideSoftKeyboard();
                Snackbar.make(view, "Please enter your password", Snackbar.LENGTH_LONG).show();
            }
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            hideSoftKeyboard();
            Snackbar.make(view, "Please enter your username", Snackbar.LENGTH_LONG).show();
        }
    }

    public void hideSoftKeyboard(){
        //Hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethod = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}