package giovannilenguito.co.uk.parceldelivery.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.Driver;
import giovannilenguito.co.uk.parceldelivery.Models.Parcel;


/**
 * Created by giovannilenguito on 08/11/2016.
 */

//to call - = new SQLiteDatabaseController(this, null, null, 0);

public class SQLiteDatabaseController extends SQLiteOpenHelper {
    private static final int Database_VERSION = 8;
    private static final String DATABASE_NAME = "parcel_system.db"; //name of the database (file)


    private static final String TABLE_PARCEL = "parcels"; //table name
    private static final String TABLE_USERS = "users"; //table name

    //customer table columns
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULLNAME = "fullName";
    private static final String COLUMN_CONTACTNUMBER = "contactNumber";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ID  = "id";
    private static final String COLUMN_ADDRESS_ONE  = "addressLineOne";
    private static final String COLUMN_ADDRESS_TWO = "addressLineTwo";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_POSTCODE = "postcode";
    private static final String COLUMN_COUNTRY = "country";

    public SQLiteDatabaseController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, Database_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String customerQuery = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT, " +
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Deletes table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARCEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        //Create new one
        onCreate(db);
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
        values.put(COLUMN_TYPE, customer.getType());

        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_USERS, null, values);

        //Close db
        db.close();

        return id;
    }

    //Add new row to the database
    public int addDriver(Driver driver){
        //Create list of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, driver.getEmail());
        values.put(COLUMN_USERNAME, driver.getUsername());
        values.put(COLUMN_PASSWORD, driver.getPassword());
        values.put(COLUMN_FULLNAME, driver.getFullName());
        values.put(COLUMN_CONTACTNUMBER, driver.getContactNumber());


        values.put(COLUMN_ADDRESS_ONE, driver.getAddressLineOne());
        values.put(COLUMN_ADDRESS_TWO, driver.getAddressLineTwo());
        values.put(COLUMN_CITY, driver.getCity());
        values.put(COLUMN_POSTCODE, driver.getPostcode());
        values.put(COLUMN_COUNTRY, driver.getCountry());
        values.put(COLUMN_TYPE, driver.getType());

        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_USERS, null, values);

        //Close db
        db.close();

        return id;
    }

    public void deleteCustomer(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_ID +"=\"" + id + "\";");

        db.close();

    }

    public Customer getCustomer(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID +"=\"" + id + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //Close db
        db.close();
        return parseToCustomer(cursor);
    }

    public List<Customer> getAllCustomers(){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_TYPE +"=\"Customer\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        List<Customer> customersList = new ArrayList<>();

        for(int i = 1; i <= cursor.getCount(); i++){
            customersList.add(parseToCustomer(cursor));
            cursor.moveToNext();
        }

        //Close db
        db.close();
        return customersList;
    }

    public List<Driver> getAllDrivers(){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_TYPE +"=\"Driver\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        List<Driver> driversList = new ArrayList<>();

        for(int i = 1; i <= cursor.getCount(); i++){
            driversList.add(parseToDriver(cursor));
            cursor.moveToNext();
        }

        //Close db
        db.close();
        return driversList;
    }

    public Customer parseToCustomer(Cursor cursor){
        //Create the user object
        String rowId = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        String fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME));
        String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
        int contactNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACTNUMBER));

        String addressLineOne = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_ONE));
        String addressLineTwo = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TWO));
        String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
        String postcode = cursor.getString(cursor.getColumnIndex(COLUMN_POSTCODE));
        String country = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY));

        Customer customer = new Customer(email, username, password, fullName, contactNumber, addressLineOne, addressLineTwo, city, postcode, country, null);
        customer.setId(rowId);
        return customer;
    }

    public Driver parseToDriver(Cursor cursor){
        //Create the user object
        String rowId = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        String fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME));
        String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
        int contactNumber = cursor.getInt(cursor.getColumnIndex(COLUMN_CONTACTNUMBER));

        String addressLineOne = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_ONE));
        String addressLineTwo = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TWO));
        String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
        String postcode = cursor.getString(cursor.getColumnIndex(COLUMN_POSTCODE));
        String country = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY));

        Driver driver = new Driver(email, username, password, fullName, contactNumber, addressLineOne, addressLineTwo, city, postcode, country);
        driver.setId(rowId);
        return driver;
    }

    public void dropUsers(){
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_USERS);

        db.close();
    }
}
