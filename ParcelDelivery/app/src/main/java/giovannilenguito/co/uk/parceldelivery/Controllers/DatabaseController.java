package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;

import static giovannilenguito.co.uk.parceldelivery.R.id.lineOne;
import static giovannilenguito.co.uk.parceldelivery.R.id.lineTwo;

/**
 * Created by giovannilenguito on 08/11/2016.
 */

public class DatabaseController extends SQLiteOpenHelper {
    private static final int Database_VERSION = 1;
    private static final String DATABASE_NAME = "parcel_system.db"; //name of the database (file)


    private static final String TABLE_PARCEL = "parcels"; //table name
    private static final String TABLE_CUSTOMERS = "customers"; //table name

    //generic table columns
    private static final String COLUMN_ID  = "id";
    private static final String COLUMN_ADDRESS_ONE  = "addressLineOne";
    private static final String COLUMN_ADDRESS_TWO = "addressLineTwo";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_POSTCODE = "postcode";
    private static final String COLUMN_COUNTRY = "country";


    //customer table columns
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULLNAME = "fullName";
    private static final String COLUMN_CONTACTNUMBER = "contactNumber";


    //parcel table columns
    private static final String COLUMN_DRIVER = "driver";
    private static final String COLUMN_RECIPIENT_NAME = "recipientName";
    private static final String COLUMN_SERVICE_TYPE = "serviceType";
    private static final String COLUMN_CONTENTS = "contents";
    private static final String COLUMN_DATE_BOOKED = "dateBooked";
    private static final String COLUMN_DELIVERY_DATE = "deliveryDate";
    private static final String COLUMN_CREATED_BY = "createdBy";

    private static final String COLUMN_IS_DELIVERED = "isDelivered";
    private static final String COLUMN_ID_OUT_FOR_DELIVERY = "isOutForDelivery";
    private static final String COLUMN_IS_PROCESSING = "isProcessing";

    public DatabaseController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, Database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create parcels table
        String parcelQuery = "CREATE TABLE " + TABLE_PARCEL + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DRIVER + " INTEGER, " +
                COLUMN_RECIPIENT_NAME + " TEXT, " +
                COLUMN_SERVICE_TYPE + " TEXT, " +
                COLUMN_CONTENTS + " TEXT, " +
                COLUMN_DATE_BOOKED + " TEXT, " +
                COLUMN_DELIVERY_DATE + " TEXT, " +
                COLUMN_CREATED_BY + " INTEGER, " +
                COLUMN_IS_DELIVERED + " TEXT, " +
                COLUMN_ID_OUT_FOR_DELIVERY + " TEXT, " +
                COLUMN_IS_PROCESSING + " TEXT, " +
                COLUMN_ADDRESS_ONE + " TEXT, " +
                COLUMN_ADDRESS_TWO + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_POSTCODE + " TEXT, " +
                COLUMN_COUNTRY + " TEXT);";

        String customerQuery = "CREATE TABLE " + TABLE_CUSTOMERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_CONTACTNUMBER + " TEXT, " +
                COLUMN_ADDRESS_ONE + " TEXT, " +
                COLUMN_ADDRESS_TWO + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_POSTCODE + " TEXT, " +
                COLUMN_COUNTRY + " TEXT);";

        //Execute queries
        db.execSQL(customerQuery);
        db.execSQL(parcelQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Deletes table
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_PARCEL);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_CUSTOMERS);

        //Create new one
        onCreate(db);
    }

    //Add new row to the database
    public int addRow(Parcel parcel){
        //Create list of values

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRIVER, parcel.getDriverID());
        values.put(COLUMN_RECIPIENT_NAME, parcel.getRecipientName());
        values.put(COLUMN_SERVICE_TYPE, parcel.getServiceType());
        values.put(COLUMN_CONTENTS, parcel.getContents());
        values.put(COLUMN_DATE_BOOKED, parcel.getDateBooked().toString());
        values.put(COLUMN_DELIVERY_DATE, parcel.getDeliveryDate().toString());

        values.put(COLUMN_CREATED_BY, parcel.getCreatedByID());
        values.put(COLUMN_IS_DELIVERED, parcel.isDelivered());
        values.put(COLUMN_ID_OUT_FOR_DELIVERY, parcel.isOutForDelivery());
        values.put(COLUMN_IS_PROCESSING, parcel.isProcessing());

        values.put(COLUMN_ADDRESS_ONE, parcel.getAddressLineOne());
        values.put(COLUMN_ADDRESS_TWO, parcel.getAddressLineTwo());
        values.put(COLUMN_CITY, parcel.getCity());
        values.put(COLUMN_POSTCODE, parcel.getPostcode());
        values.put(COLUMN_COUNTRY, parcel.getCountry());

        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_PARCEL, null, values);

        //Close db
        db.close();

        return id;
    }

    public void deleteRow(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_PARCEL + " WHERE " + COLUMN_ID +"=\"" + id + "\";");

        db.close();

    }

    public Parcel getRow(int id) throws ParseException {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PARCEL + " WHERE " + COLUMN_ID +"=\"" + id + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //to loop more than on do while(!cursor.isAfterLast()

        //Close db
        db.close();



        return parseToParcel(cursor);
    }

    public ArrayList getRowsByCustomer(int customerId) throws ParseException {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PARCEL + " WHERE " + COLUMN_CREATED_BY +"=\"" + customerId + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //Close db
        db.close();

        ArrayList parcelList = new ArrayList();

        if(cursor.getCount() != 0) {
            while (!cursor.isAfterLast()) {
                parcelList.add(parseToParcel(cursor));
            }
        }

        return parcelList;
    }


    public Parcel parseToParcel(Cursor cursor) throws ParseException {
        //Create the object
        String rowId = cursor.getString(cursor.getColumnIndex("id"));

        String driver = cursor.getString(cursor.getColumnIndex("driver"));
        String recipientName = cursor.getString(cursor.getColumnIndex("recipientName"));
        String serviceType = cursor.getString(cursor.getColumnIndex("serviceType"));
        String contents = cursor.getString(cursor.getColumnIndex("contents"));
        String dateBooked = cursor.getString(cursor.getColumnIndex("dateBooked"));
        String deliveryDate = cursor.getString(cursor.getColumnIndex("deliveryDate"));
        String createdBy = cursor.getString(cursor.getColumnIndex("createdBy"));
        String isDelivered = cursor.getString(cursor.getColumnIndex("isDelivered"));
        String isOutForDelivery = cursor.getString(cursor.getColumnIndex("isOutForDelivery"));
        String isProcessing = cursor.getString(cursor.getColumnIndex("isProcessing"));

        String addressLineOne = cursor.getString(cursor.getColumnIndex("addressLineOne"));
        String addressLineTwo = cursor.getString(cursor.getColumnIndex("addressLineTwo"));
        String city = cursor.getString(cursor.getColumnIndex("city"));
        String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
        String country = cursor.getString(cursor.getColumnIndex("country"));

        Parcel parcel = new Parcel();
        DateFormat dateFormat = new SimpleDateFormat("DD/MM/YYYY");
        Date dateOfDelivery = dateFormat.parse(deliveryDate);
        Date bookedDate = dateFormat.parse(dateBooked);

        parcel.setDriverID(Integer.parseInt(driver));

        parcel.setServiceType(serviceType);
        parcel.setRecipientName(recipientName);
        parcel.setContents(contents);
        parcel.setDeliveryDate(dateOfDelivery);
        parcel.setDateBooked(bookedDate);
        parcel.setCreatedByID(Integer.parseInt(createdBy));
        parcel.setDelivered(Boolean.parseBoolean(isDelivered));
        parcel.setOutForDelivery(Boolean.parseBoolean(isOutForDelivery));
        parcel.setProcessing((Boolean.parseBoolean(isProcessing)));

        parcel.setAddressLineOne(addressLineOne);
        parcel.setAddressLineTwo(addressLineTwo);
        parcel.setCity(city);
        parcel.setCountry(country);
        parcel.setPostcode(postcode);
        parcel.setId(Integer.parseInt(rowId));

        return parcel;
    }
    //Add new row to the database
    public int addCustomer(Customer customer){
        //Create list of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, customer.getEmail());
        values.put(COLUMN_USERNAME, customer.getUsername());
        values.put(COLUMN_PASSWORD, customer.getPassword());
        values.put(COLUMN_FULLNAME, customer.getFullName());
        values.put(COLUMN_CONTACTNUMBER, customer.getContactNumber());


        values.put(COLUMN_ADDRESS_ONE, customer.getAddressLineOne());
        values.put(COLUMN_ADDRESS_TWO, customer.getAddressLineTwo());
        values.put(COLUMN_CITY, customer.getCity());
        values.put(COLUMN_POSTCODE, customer.getPostcode());
        values.put(COLUMN_COUNTRY, customer.getCountry());

        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_CUSTOMERS, null, values);

        //Close db
        db.close();

        return id;
    }

    public void deleteCustomer(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_ID +"=\"" + id + "\";");

        db.close();

    }

    public Customer getCustomer(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_ID +"=\"" + id + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //to loop more than on do while(!cursor.isAfterLast()

        //Create the user object
        String rowId = cursor.getString(cursor.getColumnIndex("id"));
        String username = cursor.getString(cursor.getColumnIndex("username"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        String fullName = cursor.getString(cursor.getColumnIndex("fullName"));
        String email = cursor.getString(cursor.getColumnIndex("email"));
        int contactNumber = cursor.getInt(cursor.getColumnIndex("contactNumber"));

        String addressLineOne = cursor.getString(cursor.getColumnIndex("addressLineOne"));
        String addressLineTwo = cursor.getString(cursor.getColumnIndex("addressLineTwo"));
        String city = cursor.getString(cursor.getColumnIndex("city"));
        String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
        String country = cursor.getString(cursor.getColumnIndex("country"));



        //Close db
        db.close();
        return new Customer(email, username, password, fullName, contactNumber, addressLineOne, addressLineTwo, city, postcode, country, null);
    }

    public Customer authenticate(String pstUsername, String pstPassword){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_USERNAME +"=\"" + pstUsername + "\" AND "  + COLUMN_PASSWORD +"=\"" + pstPassword + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();
        System.out.println(cursor.getCount());
        if(cursor.getCount() > 0) {
            String password = cursor.getString(cursor.getColumnIndex("password"));
            //Create the user object
            String rowId = cursor.getString(cursor.getColumnIndex("id"));
            String username = cursor.getString(cursor.getColumnIndex("username"));
            String fullName = cursor.getString(cursor.getColumnIndex("fullName"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            int contactNumber = cursor.getInt(cursor.getColumnIndex("contactNumber"));

            String addressLineOne = cursor.getString(cursor.getColumnIndex("addressLineOne"));
            String addressLineTwo = cursor.getString(cursor.getColumnIndex("addressLineTwo"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String postcode = cursor.getString(cursor.getColumnIndex("postcode"));
            String country = cursor.getString(cursor.getColumnIndex("country"));

            Customer customer = new Customer(email, username, password, fullName, contactNumber, addressLineOne, addressLineTwo, city, postcode, country, null);
            customer.setId(Integer.parseInt(rowId));
            return customer;
        }else{
            return null;
        }
    }
}
