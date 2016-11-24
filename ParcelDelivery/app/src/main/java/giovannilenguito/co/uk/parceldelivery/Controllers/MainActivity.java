package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;

import java.net.MalformedURLException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.R;

public class MainActivity extends AppCompatActivity {
    private Intent intent;

    private EditText username, password;
    private Switch isDriver;

    private UserContentProvider UCP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        isDriver = (Switch)findViewById(R.id.isDriver);
        getSupportActionBar().hide();
    }

    public void goToRegister(View view){
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public <T> T isAuthenticatedCustomer(String username) throws MalformedURLException {
        try {
            //XML
            //return (T) new UserContentProvider().execute(new URL("http://10.205.205.198:8080/main/PDS?WSDL"), "XML", username).get();

            //JSON
            UCP = new UserContentProvider();
            if(isDriver.isChecked()){
                return (T) UCP.execute(new URL(getString(R.string.WS_IP) +  "/drivers/byUsername/"+ username), "GET", "driver").get();
            }else{
                return (T) UCP.execute(new URL(getString(R.string.WS_IP) + "/customers/byUsername/"+ username), "GET", "customer").get();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void login(View view) throws MalformedURLException {
        String sUsername = username.getText().toString();
        String sPassword = password.getText().toString();

        if(!sUsername.matches("")){
            if(!sPassword.matches("")){
                Snackbar.make(view, "Attempting login...", Snackbar.LENGTH_LONG).show();
                intent = new Intent(this, DashboardActivity.class);

                Object user = isAuthenticatedCustomer(sUsername);

                if(user == null){
                    hideSoftKeyboard();
                    Snackbar.make(view, "Incorrect login details", Snackbar.LENGTH_LONG).show();
                }else {
                    if (user instanceof Customer) {
                        Customer customer = (Customer) user;
                        if (customer.getPassword().equals(sPassword)) {
                            intent.putExtra("Customer", (Customer) user);
                            startActivity(intent);
                            UCP.cancel(true);
                        } else {
                            hideSoftKeyboard();
                            Snackbar.make(view, "Incorrect password", Snackbar.LENGTH_LONG).show();
                        }
                    } else if (user instanceof Driver) {
                        Driver driver = (Driver) user;
                        if (driver.getPassword().equals(sPassword)) {
                            intent.putExtra("Driver", (Driver) user);
                            startActivity(intent);
                            UCP.cancel(true);
                        } else {
                            hideSoftKeyboard();
                            Snackbar.make(view, "Incorrect password", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            }else {
                hideSoftKeyboard();
                Snackbar.make(view, "Please enter your password", Snackbar.LENGTH_LONG).show();
            }
        }else{
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
