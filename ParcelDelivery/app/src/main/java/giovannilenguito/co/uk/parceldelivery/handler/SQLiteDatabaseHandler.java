package giovannilenguito.co.uk.parceldelivery.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import giovannilenguito.co.uk.parceldelivery.model.Address;
import giovannilenguito.co.uk.parceldelivery.model.Customer;
import giovannilenguito.co.uk.parceldelivery.model.Driver;


/**
 * Created by giovannilenguito on 08/11/2016.
 */

//to call - = new SQLiteDatabaseHandler(this, null, null, 0);
public class SQLiteDatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 15;
    private static final String DATABASE_NAME = "parcel_system.db"; //name of the database (file)

    private static final String TABLE_USERS = "users"; //table name
    private static final String TABLE_GLOBAL = "driverGlobal"; //table name

    //user table columns
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULLNAME = "fullName";
    private static final String COLUMN_CONTACT_NUMBER = "contactNumber";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ADDRESS_ONE = "addressLineOne";
    private static final String COLUMN_ADDRESS_TWO = "addressLineTwo";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_POSTCODE = "postcode";
    private static final String COLUMN_COUNTRY = "country";
    private static final String COLUMN_ADDRESS_ID = "addressId";


    //driver global table columns
    private static final String COLUMN_NUMBER_OF_PARCELS = "numberOfParcelsToCollect";
    private static final String COLUMN_DRIVER_ID = "driverID";

    public SQLiteDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String customerQuery = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_CONTACT_NUMBER + " TEXT, " +
                COLUMN_ADDRESS_ONE + " TEXT, " +
                COLUMN_ADDRESS_TWO + " TEXT, " +
                COLUMN_CITY + " TEXT, " +
                COLUMN_POSTCODE + " TEXT, " +
                COLUMN_COUNTRY + " TEXT, " +
                COLUMN_ADDRESS_ID + " TEXT);";

        //Execute queries
        db.execSQL(customerQuery);


        String driverGlobal = "CREATE TABLE " + TABLE_GLOBAL + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_NUMBER_OF_PARCELS + " INTEGER, " +
                COLUMN_DRIVER_ID + " TEXT);";

        db.execSQL(driverGlobal);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Deletes table
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GLOBAL);
        //Create new one
        onCreate(db);
    }

    //Add new row to the database
    public int addNumberOfParcels(int number, int driverId) {
        //Create list of values
        ContentValues values = new ContentValues();

        values.put(COLUMN_NUMBER_OF_PARCELS, number);
        values.put(COLUMN_DRIVER_ID, driverId);

        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_GLOBAL, null, values);

        //Close db
        db.close();

        return id;
    }

    public void updateNumberOfParcels(int number, int id) {
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("UPDATE " + TABLE_GLOBAL + " SET " + COLUMN_NUMBER_OF_PARCELS + "=\"" + number + "\" WHERE " + COLUMN_DRIVER_ID + "=\"" + id + "\";");

        db.close();
    }

    public int getNumberOfParcels(int id) {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_GLOBAL + " WHERE " + COLUMN_DRIVER_ID + "=\"" + id + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //Close db
        db.close();
        return Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER_OF_PARCELS)));
    }

    //Add new row to the database
    public int addCustomer(Customer customer) {
        //Create list of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, "Customer");
        values.put(COLUMN_ID, customer.getCustomerId());
        values.put(COLUMN_EMAIL, customer.getEmail());
        values.put(COLUMN_USERNAME, customer.getUsername());
        values.put(COLUMN_PASSWORD, customer.getPassword());
        values.put(COLUMN_FULLNAME, customer.getFullName());
        values.put(COLUMN_CONTACT_NUMBER, customer.getContactNumber());


        values.put(COLUMN_ADDRESS_ONE, customer.getAddressId().getAddressLineOne());
        values.put(COLUMN_ADDRESS_TWO, customer.getAddressId().getAddressLineTwo());
        values.put(COLUMN_CITY, customer.getAddressId().getCity());
        values.put(COLUMN_POSTCODE, customer.getAddressId().getPostcode());
        values.put(COLUMN_COUNTRY, customer.getAddressId().getCountry());
        values.put(COLUMN_ADDRESS_ID, customer.getAddressId().getAddressId());

        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_USERS, null, values);

        //Close db
        db.close();

        return id;
    }

    //Add new row to the database
    public int addDriver(Driver driver) {
        //Create list of values
        ContentValues values = new ContentValues();
        values.put(COLUMN_TYPE, "Driver");
        values.put(COLUMN_ID, driver.getDriverId());
        values.put(COLUMN_EMAIL, driver.getEmail());
        values.put(COLUMN_USERNAME, driver.getUsername());
        values.put(COLUMN_PASSWORD, driver.getPassword());
        values.put(COLUMN_FULLNAME, driver.getFullName());
        values.put(COLUMN_CONTACT_NUMBER, driver.getContactNumber());

        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_USERS, null, values);

        //Close db
        db.close();

        return id;
    }

    public void deleteCustomer(int id) {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + "=\"" + id + "\";");

        db.close();

    }

    public Customer getCustomer(int id) {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_ID + "=\"" + id + "\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        //Close db
        db.close();
        return parseToCustomer(cursor);
    }

    public List<Customer> getAllCustomers() {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_TYPE + "=\"Customer\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        List<Customer> customersList = new ArrayList<>();

        for (int i = 1; i <= cursor.getCount(); i++) {
            customersList.add(parseToCustomer(cursor));
            cursor.moveToNext();
        }

        //Close db
        db.close();
        return customersList;
    }

    public List<Driver> getAllDrivers() {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_TYPE + "=\"Driver\";";
        //Cursor point to a location in the results
        Cursor cursor = db.rawQuery(query, null);
        //Move to first row in result
        cursor.moveToFirst();

        List<Driver> driversList = new ArrayList<>();

        for (int i = 1; i <= cursor.getCount(); i++) {
            driversList.add(parseToDriver(cursor));
            cursor.moveToNext();
        }

        //Close db
        db.close();
        return driversList;
    }

    public Customer parseToCustomer(Cursor cursor) {
        //Create the user object
        int rowId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        String fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME));
        String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
        Long contactNumber = cursor.getLong(cursor.getColumnIndex(COLUMN_CONTACT_NUMBER));

        String addressLineOne = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_ONE));
        String addressLineTwo = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS_TWO));
        String city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY));
        String postcode = cursor.getString(cursor.getColumnIndex(COLUMN_POSTCODE));
        String country = cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY));
        int addressId = cursor.getInt(cursor.getColumnIndex(COLUMN_ADDRESS_ID));

        Address address = new Address();
        address.setAddressId(addressId);
        address.setAddressLineOne(addressLineOne);
        address.setAddressLineTwo(addressLineTwo);
        address.setCity(city);
        address.setPostcode(postcode);
        address.setCountry(country);

        Customer customer = new Customer(email, username, password, fullName, contactNumber, address);
        customer.setCustomerId(rowId);
        return customer;
    }

    public Driver parseToDriver(Cursor cursor) {
        //Create the user object
        int rowId = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
        String username = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
        String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
        String fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULLNAME));
        String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
        Long contactNumber = cursor.getLong(cursor.getColumnIndex(COLUMN_CONTACT_NUMBER));

        Driver driver = new Driver(email, username, password, fullName, contactNumber);
        driver.setDriverId(rowId);
        return driver;
    }

    public void dropUsers() {
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_USERS);

        db.close();
    }
}