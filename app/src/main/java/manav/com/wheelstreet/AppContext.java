package manav.com.wheelstreet;

import android.app.Application;

public class AppContext extends Application {
    private static String TAG = AppContext.class.getSimpleName();
    private static AppContext appInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance=this;
    }

    public static AppContext getAppContext() {
        return appInstance;
    }
}