package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class ViewParcelActivity extends AppCompatActivity {
    Customer customer;
    Parcel parcel;
    Intent intent;

    TextView deliveryStatus, lineOne, lineTwo, city, postcode, country, contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_parcel);

        intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("Customer");
        parcel = (Parcel) intent.getSerializableExtra("Parcel");

        setTitle(parcel.getTitle());
        setUpView();
    }

    @Override
    public Intent getParentActivityIntent() {
        intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("Customer", customer);
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

        deliveryStatus.setText(parcel.getStatus());
        lineOne.setText(parcel.getAddressLineOne());
        lineTwo.setText(parcel.getAddressLineTwo());
        city.setText(parcel.getCity());
        postcode.setText(parcel.getPostcode());
        country.setText(parcel.getCountry());
        contents.setText(parcel.getContents());
    }
}
