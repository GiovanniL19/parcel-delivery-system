package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.R;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private DatabaseController database;

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        //set up database
        database = new DatabaseController(this, null, null, 0);
    }

    public void goToRegister(View view){
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public <T> T isAuthenticatedCustomer(String username, String password) throws MalformedURLException {
        try {
            return (T) new UserDAOController().execute(new URL("http://www.giovannilenguito.co.uk/XML/users.xml"), "XML", username, password).get();

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
                intent = new Intent(this, DashboardActivity.class);

                Object user = isAuthenticatedCustomer(sUsername, sPassword);

                if( user instanceof Customer )
                {
                    intent.putExtra("Customer", (Customer) user);
                }
                else if( user instanceof Driver)
                {
                    intent.putExtra("Driver", (Driver) user);
                }

                if(user != null){
                    startActivity(intent);
                }else{
                    hideSoftKeyboard();
                    Snackbar.make(view, "Incorrect login details", Snackbar.LENGTH_LONG).show();
                }

                /*
                Object user = database.authenticate(sUsername, sPassword);

                if( user instanceof Customer )
                {
                    intent.putExtra("Customer", (Customer) user);
                }
                else if( user instanceof Driver)
                {
                    intent.putExtra("Driver", (Driver) user);
                }

                if(user != null) {
                    startActivity(intent);
                }else{
                    hideSoftKeyboard();
                    Snackbar.make(view, "Incorrect login details", Snackbar.LENGTH_LONG).show();
                }
                */
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
