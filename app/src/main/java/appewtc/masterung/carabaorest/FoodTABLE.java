package appewtc.masterung.carabaorest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 4/27/15 AD.
 */
public class FoodTABLE {

    //Explicit
    private MyOpenHelper objMyOpenHelper;
    private SQLiteDatabase writeDatabase, readDatabase;

    public static final String TABLE_FOOD = "foodTABLE";
    public static final String COLUMN_ID_FOOD = "_id";
    public static final String COLUMN_FOOD = "Food";
    public static final String COLUMN_PRICE = "Price";

    public FoodTABLE(Context context) {

        objMyOpenHelper = new MyOpenHelper(context);
        writeDatabase = objMyOpenHelper.getWritableDatabase();
        readDatabase = objMyOpenHelper.getReadableDatabase();

    }   // Constructor

    //Read All Price
    public String[] readAllPrice() {

        String strResultPrice[] = null;
        Cursor objCursor = readDatabase.query(TABLE_FOOD,
                new String[]{COLUMN_ID_FOOD, COLUMN_PRICE},
                null, null, null, null, null);

        if (objCursor != null) {
            objCursor.moveToFirst();
            strResultPrice = new String[objCursor.getCount()];
            for (int i = 0; i < objCursor.getCount(); i++) {
                strResultPrice[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_PRICE));
                objCursor.moveToNext();
            }
        }
        objCursor.close();
        return strResultPrice;
    }

    //Read All Food
    public String[] readAllFood() {

        String strResultFood[] = null;
        Cursor objCursor = readDatabase.query(TABLE_FOOD,
                new String[]{COLUMN_ID_FOOD, COLUMN_FOOD},
                null, null, null, null, null);

        if (objCursor != null) {
            objCursor.moveToFirst();
            strResultFood = new String[objCursor.getCount()];
            for (int i = 0; i < objCursor.getCount(); i++) {
                strResultFood[i] = objCursor.getString(objCursor.getColumnIndex(COLUMN_FOOD));
                objCursor.moveToNext();
            }   // for
        }   // if
        objCursor.close();
        return strResultFood;
    }


    public long addFood(String strFood, String strPrice) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_FOOD, strFood);
        objContentValues.put(COLUMN_PRICE, strPrice);
        return writeDatabase.insert(TABLE_FOOD, null, objContentValues);
    }

}   // Main Class
