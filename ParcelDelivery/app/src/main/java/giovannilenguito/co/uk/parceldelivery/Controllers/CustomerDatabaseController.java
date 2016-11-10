package giovannilenguito.co.uk.parceldelivery.Controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import giovannilenguito.co.uk.parceldelivery.Models.Customer;
import giovannilenguito.co.uk.parceldelivery.Models.User;

/**
 * Created by giovannilenguito on 08/11/2016.
 */

public class CustomerDatabaseController extends DatabaseController {
   private static final String TABLE_NAME = "customers"; //table name
    //table columns
    private static final String COLUMN_ID  = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULLNAME = "fullName";
    private static final String COLUMN_CONTACTNUMBER = "contactNumber";

    private static final String COLUMN_ADDRESS_ONE  = "addressLineOne";
    private static final String COLUMN_ADDRESS_TWO = "addressLineTwo";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_POSTCODE = "postcode";
    private static final String COLUMN_COUNTRY = "country";

    public CustomerDatabaseController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create customers table
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
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

        //EXECUTE TABLE
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Deletes table
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);

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

        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();

        //Insert new row into users table
        int id = (int) db.insert(TABLE_NAME, null, values);

        //Close db
        db.close();

        return id;
    }

    public void deleteCustomer(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +"=\"" + id + "\";");

        db.close();

    }

    public Customer getCustomer(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +"=\"" + id + "\";";
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
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME +"=\"" + pstUsername + "\" AND "  + COLUMN_PASSWORD +"=\"" + pstPassword + "\";";
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
