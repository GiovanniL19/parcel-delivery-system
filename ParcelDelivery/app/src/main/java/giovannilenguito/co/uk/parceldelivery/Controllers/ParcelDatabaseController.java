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

public class ParcelDatabaseController extends DatabaseController {

    private static final String TABLE_NAME = "parcels"; //table name
    //table columns
    private static final String COLUMN_ID  = "id";
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

    private static final String COLUMN_ADDRESS_ONE  = "addressLineOne";
    private static final String COLUMN_ADDRESS_TWO = "addressLineTwo";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_POSTCODE = "postcode";
    private static final String COLUMN_COUNTRY = "country";

    public ParcelDatabaseController(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create parcels table
        String parcelQuery = "CREATE TABLE " + TABLE_NAME + "(" +
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
        int id = (int) db.insert(TABLE_NAME, null, values);

        //Close db
        db.close();

        return id;
    }

    public void deleteRow(int id){
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        //Delete row from table where id's match
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +"=\"" + id + "\";");

        db.close();

    }

    public Parcel getRow(int id) throws ParseException {
        //Get reference to database
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID +"=\"" + id + "\";";
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
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_CREATED_BY +"=\"" + customerId + "\";";
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
}
