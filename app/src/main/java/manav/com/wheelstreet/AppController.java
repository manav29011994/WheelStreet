package manav.com.wheelstreet;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by manav on 12/5/18.
 */

public class AppController {

    private static AppController appController;
    private RequestQueue requestQueue;
    private static Context mctx;


    private AppController(Context context)
    {
        mctx=context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            requestQueue= Volley.newRequestQueue(mctx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized AppController getInstance(Context context)
    {
        if(appController==null)
        {
            appController=new AppController(context);
        }
        return appController;
    }
    public <T> void addtoRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }
}
