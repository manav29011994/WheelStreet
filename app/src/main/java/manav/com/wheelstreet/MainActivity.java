package manav.com.wheelstreet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton button;
    CallbackManager callbackManager;
    private String userName;
    private String userMail;
    private String userPic;
    private static final String ID="id";
    private static final String NAME="name";
    private static final String EMAIL="email";
    private static final String GENDER="gender";
    private static final String PICTURE="picture.width(150).height(150)";
    private static final String DATA="data";
    private static final String URL="url";
    private static final String PIC="picture";
    private static final String PUBLIC_PROFILE ="public_profile";

    private String userGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.fb_login_screen);
        button=(LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                UserDetailModel model=new UserDetailModel();
                                try {
                                    if(!TextUtils.isEmpty(object.getString(NAME)))
                                        userName = object.getString(NAME);
                                    if(!TextUtils.isEmpty(object.getString(EMAIL)))
                                        userMail = object.getString(EMAIL);
                                    if(object.getJSONObject(PIC)!=null && object.getJSONObject(PIC).getJSONObject(DATA)!=null && !TextUtils.isEmpty(object.getJSONObject(PIC).getJSONObject(DATA).getString(URL)))
                                        userPic=object.getJSONObject(PIC).getJSONObject(DATA).getString(URL);
                                        model.setName(userName);
                                        model.setGender(userGender);
                                        model.setMailId(userMail);
                                        model.setImageUrl(userPic);

                                    //saving in DB
                                    UserHistory.getInstance().SaveUserDetail(model);

                                    Intent intent=new Intent(MainActivity.this,UserDeatailActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("logged",true);
                                    editor.commit();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,picture.width(150).height(150)");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
             Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkAvailable()) {
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList(PUBLIC_PROFILE, EMAIL));
                }else{
                    Toast.makeText(MainActivity.this,"internet connection not available",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callbackManager=null;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
