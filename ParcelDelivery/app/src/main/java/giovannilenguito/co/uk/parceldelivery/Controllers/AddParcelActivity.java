package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class AddParcelActivity extends AppCompatActivity {
    private Driver driver;
    private Spinner deliveryType;
    private EditText recipientName, contents, deliveryDate;
    private Intent intent;
    private List<Customer> customers;
    private Spinner spinner;
    private ImageView previewImage;
    private SQLiteDatabaseController database;
    private UserContentProvider UCP;
    private ParcelContentProvider PCP;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String encImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parcel);
        setTitle("New Parcel");

        //set up database
        database = new SQLiteDatabaseController(this, null, null, 0);

        //Get customer
        intent = getIntent();
        driver = (Driver) intent.getSerializableExtra("Driver");

        deliveryType = (Spinner) findViewById(R.id.deliveryType);
        recipientName = (EditText) findViewById(R.id.recipientName);
        contents = (EditText) findViewById(R.id.contents);
        deliveryDate = (EditText) findViewById(R.id.deliveryDate);
        previewImage = (ImageView) findViewById(R.id.preview);

        UCP = new UserContentProvider();

        try {
            customers = (List<Customer>) UCP.execute(new URL(getString(R.string.WS_IP) + "/customers/all"), "GETALL", "customer", null).get();
            UCP.cancel(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        spinner = (Spinner)findViewById(R.id.customers);
        ArrayList<String> spinnerArray = new ArrayList<>();

        for(Customer customer: customers){
            spinnerArray.add(customer.getFullName() + " (" + customer.getId() + ")");
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
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
                    Snackbar.make(view, "There was an error, please try again", Snackbar.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                return true;
            case R.id.action_add_image:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            previewImage.setImageBitmap(imageBitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
            encImage = "data:image/jpeg;base64," + encodedImage;
        }
    }

    @Override
    public Intent getParentActivityIntent() {
        intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("Driver", driver);
        return intent;
    }

    private void addParcel(View view) throws MalformedURLException {
        Parcel parcel = new Parcel();

        String spinnerChoice = deliveryType.getSelectedItem().toString();
        String recipientN = recipientName.getText().toString();
        String cont = contents.getText().toString();
        String deliveryD = deliveryDate.getText().toString();


        parcel.setServiceType(spinnerChoice);
        parcel.setRecipientName(recipientN);
        parcel.setContents(cont);
        parcel.setDeliveryDate(deliveryD);


        int selectedCustomerPosition = spinner.getSelectedItemPosition();

        Customer customer = customers.get(selectedCustomerPosition);

        parcel.setCustomerID(customer.getId());
        parcel.setAddressLineOne(customer.getAddressLineOne());
        parcel.setAddressLineTwo(customer.getAddressLineTwo());
        parcel.setCity(customer.getCity());
        parcel.setCountry(customer.getCountry());
        parcel.setPostcode(customer.getPostcode());

        parcel.setCreatedByID(driver.getId());

        Date dateBooked = new Date();
        parcel.setDateBooked(dateBooked.toString());

        //Set status
        parcel.setProcessing(true);
        parcel.setOutForDelivery(false);
        parcel.setDelivered(false);


        //Set image
        parcel.setImage(encImage);
        //POST REQUEST TO WEB SERVICE
        PCP = new ParcelContentProvider();
        try {
            boolean response = (boolean) PCP.execute(new URL(getString(R.string.WS_IP) + "/parcels/new"), "POST", null, null, parcel).get();
            PCP.cancel(true);
            if(response){
                intent = new Intent(this, DashboardActivity.class);
                intent.putExtra("Driver", driver);
                startActivity(intent);
            }else{
                Snackbar.make(view, "There was an error", Snackbar.LENGTH_LONG).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Snackbar.make(view, "There was an error creating parcel", Snackbar.LENGTH_LONG).show();

    }
}
