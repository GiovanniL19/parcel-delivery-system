package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.SQLiteDatabaseController;
import giovannilenguito.co.uk.parceldelivery.R;

public class AccountActivity extends AppCompatActivity {
    private Customer customer = null;
    private Driver driver = null;
    private TextView username, userType;
    private SQLiteDatabaseController database = new SQLiteDatabaseController(this, null, null, 0);
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        username = (TextView) findViewById(R.id.usernameTxt);
        userType = (TextView) findViewById(R.id.userTypeTxt);
        Intent intent = getIntent();

        setTitle("Account Overview");

        if(intent.getSerializableExtra("Customer") != null){
            customer = (Customer) intent.getSerializableExtra("Customer");
            username.setText(customer.getUsername());
            userType.setText("Customer");
        }else{
            driver = (Driver) intent.getSerializableExtra("Driver");
            username.setText(driver.getUsername());
            userType.setText("Driver");
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("Driver", driver);
        intent.putExtra("Customer", customer);
        return intent;
    }

    public void logout(View view) {
        //Delete local users
        database.dropUsers();
        //Go to login
        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
    }
}
