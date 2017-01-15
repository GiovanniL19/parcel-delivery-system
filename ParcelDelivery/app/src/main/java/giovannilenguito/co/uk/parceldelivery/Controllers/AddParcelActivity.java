package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
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
    private Customer customer;
    private Spinner deliveryType;
    private EditText contents, recipient, lineOne, lineTwo, city, postcode, country;
    private String deliveryDate;
    private Intent intent;
    private List<Customer> customers;
    private Spinner spinner;
    private ImageView previewImage;
    private UserHTTPManager userHTTPManager;
    private ParcelHTTPManager parcelHTTPManager;
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
        //Hide keyboard on transition, use has to focus on element to bring up keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setTitle("New Parcel");
        //Get customer
        intent = getIntent();
        customer = (Customer) intent.getSerializableExtra("Customer");

        deliveryType = (Spinner) findViewById(R.id.deliveryType);
        contents = (EditText) findViewById(R.id.contents);
        previewImage = (ImageView) findViewById(R.id.preview);

        dateTitle = (TextView) findViewById(R.id.dateTitle);

        recipient = (EditText) findViewById(R.id.recipientName);
        lineOne = (EditText) findViewById(R.id.lineOne);
        lineTwo = (EditText) findViewById(R.id.lineTwo);
        city = (EditText) findViewById(R.id.city);
        postcode = (EditText) findViewById(R.id.postcode);
        country = (EditText) findViewById(R.id.country);

        userHTTPManager = new UserHTTPManager();

        try {
            customers = (List<Customer>) userHTTPManager.execute(new URL(getString(R.string.WS_IP) + "/customers/all"), "GET", "customers", null).get();
            userHTTPManager.cancel(true);
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
        intent.putExtra("Customer", customer);
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
                String date = day + "/" + (month + 1) + "/" + year;
                deliveryDate = date;
                dateTitle.setText("Estimated Delivery Date: "+ date);
            }
        }, cYear, cMonth, cDay);
        datePickerDialog.show();
    }

    private void addParcel(View view) throws MalformedURLException {
        Parcel parcel = new Parcel();

        if(deliveryDate != null && !deliveryDate.isEmpty()) {
            if(!contents.getText().toString().isEmpty()) {
                Driver driver = null;

                int selectedCustomerPosition = spinner.getSelectedItemPosition();

                Customer selectedRecipient = customers.get(selectedCustomerPosition);

                String spinnerChoice = deliveryType.getSelectedItem().toString();
                String recipientN = recipient.getText().toString();
                String cont = contents.getText().toString();
                String deliveryD = deliveryDate;

                //Package information
                parcel.setServiceType(spinnerChoice);
                parcel.setRecipientName(recipientN);
                parcel.setContents(cont);
                parcel.setDeliveryDate(deliveryD);

                if(recipient.getText().toString().isEmpty()){
                    //Recipient
                    parcel.setAddressLineOne(selectedRecipient.getAddressLineOne());
                    parcel.setAddressLineTwo(selectedRecipient.getAddressLineTwo());
                    parcel.setCity(selectedRecipient.getCity());
                    parcel.setCountry(selectedRecipient.getCountry());
                    parcel.setPostcode(selectedRecipient.getPostcode());
                }else{
                    //Recipient
                    parcel.setAddressLineOne(lineOne.getText().toString());
                    parcel.setAddressLineTwo(lineTwo.getText().toString());
                    parcel.setCity(city.getText().toString());
                    parcel.setCountry(country.getText().toString());
                    parcel.setPostcode(postcode.getText().toString());
                }

                //Customer information (collection information)
                parcel.setCustomerID(customer.getId());
                parcel.setCollectionLineOne(customer.getAddressLineOne());
                parcel.setCollectionPostCode(customer.getPostcode());

                try {
                    userHTTPManager = new UserHTTPManager();
                    driver = (Driver) userHTTPManager.execute(new URL(getString(R.string.WS_IP) +  "/drivers/random"), "GET", "driver").get();
                    userHTTPManager.cancel(true);
                    //set random driver
                    parcel.setDriverID(driver.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Date dateBooked = new Date();
                parcel.setDateBooked(dateBooked.toString());

                //Set default status
                parcel.setProcessing(true);
                parcel.setOutForDelivery(false);
                parcel.setDelivered(false);


                //Set image if one has been taken
                parcel.setImage(encImage);

                //Set location
                if (locationReady) {
                    Location location = new Location(parcel.getId(), lon, lat);
                    parcel.setLocation(location);

                    //POST REQUEST TO WEB SERVICE
                    parcelHTTPManager = new ParcelHTTPManager();
                    try {
                        boolean response = (boolean) parcelHTTPManager.execute(new URL(getString(R.string.WS_IP) + "/parcels/new"), "POST", null, null, parcel).get();
                        parcelHTTPManager.cancel(true);
                        if (response) {

                            Handler handler = new Handler();
                            final Driver finalDriver = driver;
                            final Context context = this;
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    //Create notification
                                    Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
                                    notificationBuilder.setSmallIcon(R.drawable.app_icon);
                                    notificationBuilder.setContentTitle("Parcel Confirmation");
                                    notificationBuilder.setContentText(finalDriver.getFullName() + " will be collecting your parcel.");
                                    notificationBuilder.setSound(sound);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                    notificationManager.notify(001, notificationBuilder.build());
                                }
                            }, 5000);
                            intent = new Intent(this, DashboardActivity.class);
                            intent.putExtra("Customer", customer);
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
                Snackbar.make(view, "Please tell us what the package is", Snackbar.LENGTH_LONG).show();
            }
        }else{
            Snackbar.make(view, "Please select delivery date", Snackbar.LENGTH_LONG).show();
        }
    }
}
