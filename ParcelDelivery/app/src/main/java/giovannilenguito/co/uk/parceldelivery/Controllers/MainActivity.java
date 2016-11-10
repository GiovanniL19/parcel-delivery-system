package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.R;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private CustomerDatabaseController customerDatabase;

    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);

        //set up database
        customerDatabase = new CustomerDatabaseController(this, null, null, 1);
    }

    public void goToRegister(View view){
        intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view){
        String sUsername = username.getText().toString();
        String sPassword = password.getText().toString();

        if(!sUsername.matches("")){
            if(!sPassword.matches("")){
                Customer customer = customerDatabase.authenticate(sUsername, sPassword);

                if(customer != null) {
                    intent = new Intent(this, DashboardActivity.class);
                    intent.putExtra("Customer", customer);
                    startActivity(intent);
                }else{
                    Snackbar.make(view, "Incorrect login details", Snackbar.LENGTH_LONG).show();
                }
            }else {
                Snackbar.make(view, "Please enter your password", Snackbar.LENGTH_LONG).show();
            }
        }else{
            Snackbar.make(view, "Please enter your username", Snackbar.LENGTH_LONG).show();
        }
    }
}
