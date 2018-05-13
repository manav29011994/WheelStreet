package manav.com.wheelstreet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by manav on 12/5/18.
 */

public class FbDatabase extends SQLiteOpenHelper{

        public static FbDatabase instance;

        private FbDatabase(Context context) {
            super(context, "fbdata.db", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            UserHistory.createTables(sqLiteDatabase);

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

        //Returns the instance of OlaPlayDB class
        public static FbDatabase getDBInstance() {
            if (instance == null) {
                instance = new FbDatabase(AppContext.getAppContext());
            }
            return instance;
        }

        //Creating the instance of Sqlite Database.
        public static SQLiteDatabase getDB() {
            return getDBInstance().getWritableDatabase();
        }
    }
