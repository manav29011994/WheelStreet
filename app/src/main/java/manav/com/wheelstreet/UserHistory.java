package manav.com.wheelstreet;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manav on 12/05/18.
 */

public class UserHistory {

    private static final String TAG = UserHistory.class.getSimpleName();
    private static UserHistory historyDB = null;

    //Table for user  details
    public static final String USER_DETAILS = "userDetails";


    private SQLiteDatabase db;

    //columnsName

    public static final String NAME = "name";
    private static final String URL = "url";
    private static final String GENDER = "gender";
    private static final String EMAIL="email";
    private static final String NUMBER="number";



    public UserHistory() {
        db = FbDatabase.getDB();
    }

    public static UserHistory getInstance() {
        if (historyDB == null) {
            historyDB = new UserHistory();
        }
        return historyDB;
    }

    public static void createTables(SQLiteDatabase sql)
    {
        String songHistory = " CREATE TABLE IF NOT EXISTS " + USER_DETAILS + " ( " + NAME + " varchar ,"
                + GENDER + " varchar,"
                + URL+ " varchar,"
                + NUMBER+ " varchar,"
                + EMAIL + " varchar )";
        sql.execSQL(songHistory);

    }

    public void SaveUserDetail(UserDetailModel data) {
        if (data == null) {
            return;
        }
        db.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, data.getName());
        contentValues.put(GENDER, data.getGender());
        contentValues.put(URL, data.getImageUrl());
        contentValues.put(EMAIL, data.getMailId());
        contentValues.put(NUMBER,data.getContact());
        db.insert(USER_DETAILS,null,contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public UserDetailModel getUserDetail() {
        UserDetailModel data = new UserDetailModel();
        Cursor c = db.query(USER_DETAILS, null, null,
                null, null, null, null);
        try {
            while (c.moveToNext()) {
                data.setName(c.getString(c.getColumnIndex(NAME)));
                data.setGender(c.getString(c.getColumnIndex(GENDER)));
                data.setImageUrl(c.getString(c.getColumnIndex(URL)));
                data.setMailId(c.getString(c.getColumnIndex(EMAIL)));
                data.setContact(c.getString(c.getColumnIndex(NUMBER)));
            }
        } catch (Throwable V) {
            V.printStackTrace();
        } finally {
            c.close();
        }
        return data;
    }

}
