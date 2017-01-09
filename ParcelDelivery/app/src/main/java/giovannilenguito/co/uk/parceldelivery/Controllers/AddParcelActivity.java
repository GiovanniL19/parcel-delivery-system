package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Location;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;
import giovannilenguito.co.uk.parceldelivery.R;

public class AddParcelActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Driver driver;
    private Spinner deliveryType;
    private EditText contents;
    private String deliveryDate;
    private Intent intent;
    private List<Customer> customers;
    private Spinner spinner;
    private ImageView previewImage;
    private UserHTTPManager UCP;
    private ParcelHTTPManager PCP;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private String encImage;
    private TextView dateTitle;

    //LOCATION
    private double lat;
    private double lon;
    private GoogleApiClient mGoogleApiClient;
    private android.location.Location mLastLocation;

    private boolean locationReady = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parcel);
        setTitle("New Parcel");
        //Get customer
        intent = getIntent();
        driver = (Driver) intent.getSerializableExtra("Driver");

        deliveryType = (Spinner) findViewById(R.id.deliveryType);
        contents = (EditText) findViewById(R.id.contents);
        previewImage = (ImageView) findViewById(R.id.preview);

        dateTitle = (TextView) findViewById(R.id.dateTitle);

        UCP = new UserHTTPManager();

        try {
            customers = (List<Customer>) UCP.execute(new URL(getString(R.string.WS_IP) + "/customers/all"), "GET", "customers", null).get();
            UCP.cancel(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        spinner = (Spinner) findViewById(R.id.customers);
        ArrayList<String> spinnerArray = new ArrayList<>();

        for (Customer customer : customers) {
            spinnerArray.add(customer.getFullName() + " (" + customer.getId() + ")");
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                lat = mLastLocation.getLatitude();
                lon = mLastLocation.getLongitude();
                locationReady = true;
            }
        }else {
            Snackbar.make(this.getCurrentFocus(), "You need to allow the permission", Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public void onConnectionSuspended(int i) {locationReady = false;}
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {locationReady = false;}

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
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
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

    public void selectDate(View view){
        final Calendar calendar = Calendar.getInstance();
        int cYear = calendar.get(Calendar.YEAR);
        int cMonth = calendar.get(Calendar.MONTH);
        int cDay = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = day + "-" + (month + 1) + "-" + year;
                deliveryDate = date;
                dateTitle.setText("Prefered Delivery Date ("+date+")");
            }
        }, cYear, cMonth, cDay);
        datePickerDialog.show();
    }

    private void addParcel(View view) throws MalformedURLException {
        Parcel parcel = new Parcel();

        if(deliveryDate != null && !deliveryDate.isEmpty()) {

            int selectedCustomerPosition = spinner.getSelectedItemPosition();

            Customer customer = customers.get(selectedCustomerPosition);
            String recipientName = customer.getFullName();

            String spinnerChoice = deliveryType.getSelectedItem().toString();
            String recipientN = recipientName;
            String cont = contents.getText().toString();
            String deliveryD = deliveryDate;


            parcel.setServiceType(spinnerChoice);
            parcel.setRecipientName(recipientN);
            parcel.setContents(cont);
            parcel.setDeliveryDate(deliveryD);


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

            //Set location
            if (locationReady) {
                Location location = new Location(parcel.getId(), lon, lat);
                parcel.setLocation(location);

                //POST REQUEST TO WEB SERVICE
                PCP = new ParcelHTTPManager();
                try {
                    boolean response = (boolean) PCP.execute(new URL(getString(R.string.WS_IP) + "/parcels/new"), "POST", null, null, parcel).get();
                    PCP.cancel(true);
                    if (response) {
                        intent = new Intent(this, DashboardActivity.class);
                        intent.putExtra("Driver", driver);
                        System.out.println(parcel.getLocation().getLongitude());
                        System.out.println(parcel.getLocation().getLatitude());
                        startActivity(intent);
                    } else {
                        Snackbar.make(view, "There was an error", Snackbar.LENGTH_LONG).show();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Snackbar.make(view, "Connection request issue", Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(view, "Could not get location", Snackbar.LENGTH_LONG).show();
            }
        }else{
            Snackbar.make(view, "Please select delivery date", Snackbar.LENGTH_LONG).show();
        }
    }
}
