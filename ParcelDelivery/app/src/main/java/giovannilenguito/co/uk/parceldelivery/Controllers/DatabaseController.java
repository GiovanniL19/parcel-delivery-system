package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;


/**
 * Created by giovannilenguito on 08/11/2016.
 */

public class DatabaseController extends SQLiteOpenHelper {
    private static final int Database_VERSION = 6;
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
                COLUMN_IS_DELIVERED + " INTEGER, " +
                COLUMN_ID_OUT_FOR_DELIVERY + " INTEGER, " +
                COLUMN_IS_PROCESSING + " INTEGER, " +
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARCEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);

        //Create new one
        onCreate(db);
    }

    //Add new row to the database
    public int addParcel(Parcel parcel){
        //Create list of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRIVER, parcel.getDriverID());
        values.put(COLUMN_RECIPIENT_NAME, parcel.getRecipientName());
        values.put(COLUMN_SERVICE_TYPE, parcel.getServiceType());
        values.put(COLUMN_CONTENTS, parcel.getContents());
        values.put(COLUMN_DATE_BOOKED, parcel.getDateBooked());
        values.put(COLUMN_DELIVERY_DATE, parcel.getDeliveryDate());

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

    public boolean deleteParcel(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_PARCEL + " WHERE " + COLUMN_ID +"=\"" + id + "\";");

        String query = "SELECT * FROM " + TABLE_PARCEL + " WHERE " + COLUMN_ID +"=\"" + id + "\" ORDER BY id DESC;";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        db.close();


        if(cursor.getCount() == 0){
            return true;
        }else{
            return false;
        }
    }

    public Parcel getParcel(int id){
        //if is parcel id
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PARCEL + " WHERE " + COLUMN_ID +"=\"" + id + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //Close db
        db.close();

        return parseToParcel(cursor);
    }

    public int updateParcel(Parcel parcel){
        //Create list of values
        ContentValues values = new ContentValues();

        values.put(COLUMN_DRIVER, parcel.getDriverID());
        values.put(COLUMN_RECIPIENT_NAME, parcel.getRecipientName());
        values.put(COLUMN_SERVICE_TYPE, parcel.getServiceType());
        values.put(COLUMN_CONTENTS, parcel.getContents());
        values.put(COLUMN_DATE_BOOKED, parcel.getDateBooked());
        values.put(COLUMN_DELIVERY_DATE, parcel.getDeliveryDate());
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

        System.out.println("Updating where id is " + parcel.getId());

        System.out.println(parcel.isProcessing());
        System.out.println(parcel.isOutForDelivery());
        System.out.println(parcel.isDelivered());

        //Insert new row into users table
        int rows = db.update(TABLE_PARCEL, values, COLUMN_ID + "=" + parcel.getId(), null);

        //Close db
        db.close();

        System.out.println("Returning id of " + rows);
        return rows;
    }


    public List<Parcel> getRowsByCustomer(int customerId) {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PARCEL + " WHERE " + COLUMN_CREATED_BY +"=\"" + customerId + "\" ORDER BY id DESC;";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //Close db
        db.close();

        List<Parcel> parcelList = new ArrayList<>();

        if(cursor.getCount() != 0) {
            for(int i = 1; i <= cursor.getCount(); i++){
                parcelList.add(parseToParcel(cursor));
                cursor.moveToNext();
            }
        }

        return parcelList;
    }


    public Parcel parseToParcel(Cursor cursor) {
        //Create the object
        String rowId = cursor.getString(cursor.getColumnIndex("id"));

        String recipientName = cursor.getString(cursor.getColumnIndex(COLUMN_RECIPIENT_NAME));
        String serviceType = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE_TYPE));
        String contents = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENTS));
        String dateBooked = cursor.getString(cursor.getColumnIndex(COLUMN_DATE_BOOKED));
        String deliveryDate = cursor.getString(cursor.getColumnIndex(COLUMN_DELIVERY_DATE));
        String createdBy = cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_BY));

        boolean isDelivered = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_DELIVERED)) > 0;
        boolean isOutForDelivery = cursor.getInt(cursor.getColumnIndex(COLUMN_ID_OUT_FOR_DELIVERY)) > 0;
        boolean isProcessing = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_PROCESSING)) > 0;

        String addressLineOne = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_ONE));
        String addressLineTwo = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TWO));
        String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
        String postcode = cursor.getString(cursor.getColumnIndex(COLUMN_POSTCODE));
        String country = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY));

        Parcel parcel = new Parcel();

        parcel.setServiceType(serviceType);
        parcel.setRecipientName(recipientName);
        parcel.setContents(contents);
        parcel.setDeliveryDate(deliveryDate);
        parcel.setDateBooked(dateBooked);
        parcel.setCreatedByID(Integer.parseInt(createdBy));


        parcel.setDelivered(isDelivered);
        parcel.setOutForDelivery(isOutForDelivery);
        parcel.setProcessing(isProcessing);

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
