package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.R;

public class AddParcelActivity extends AppCompatActivity {
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parcel);
        setTitle("New Parcel");
        //Get customer
        Intent intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("Customer");
    }

    private void addParcel(View view){

    }
}
