package manav.com.wheelstreet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manav on 13/5/18.
 */

public class ChatReviewActivity extends AppCompatActivity {
    LinearLayout linearLayout;
    private static final String URL_To_SEND=" https://api.wheelstreet.com/v1/test/answers";
    private JSONObject js;
    ArrayList<QuestionModel> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
         linearLayout=(LinearLayout)findViewById(R.id.container);
         data=new ArrayList<>();
        if(getIntent()!=null && getIntent().getSerializableExtra("chatData")!=null){
            data=(ArrayList<QuestionModel>)getIntent().getSerializableExtra("chatData");
        }

        for(int i=data.size()-1;i>=1;i--){
           View view= getLayoutInflater().inflate(R.layout.chat_review,null);
            ((TextView)view.findViewById(R.id.ans)).setText(data.get(i).getQuestion());
            linearLayout.addView(view);
        }
        View view= getLayoutInflater().inflate(R.layout.chat_review,null);
        ((TextView)view.findViewById(R.id.ans)).setText(data.get(0).getQuestion());
        linearLayout.addView(view);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                js=generateJsonbject();
 /*               JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                        Request.Method.POST,URL_To_SEND, js,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(ChatReviewActivity.this,"Data Posted Successfully",Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("ChatReviewActivity", "Error: " + error.getMessage());

                    }
                }) {

                    *//**
                     * Passing some request headers
                     * *//*
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };*/
            }
        });

    }

    private JSONObject generateJsonbject() {
        String gender="";
        JSONObject json = new JSONObject();
        try {

            json.put("id", getString(R.string.facebook_app_id));
            json.put("name",UserHistory.getInstance().getUserDetail().getName());
            json.put("fbUserName", UserHistory.getInstance().getUserDetail().getName());
            json.put("mobile",UserHistory.getInstance().getUserDetail().getContact());
            if(UserHistory.getInstance().getUserDetail().getGender().equals("1"))
               gender="male";
            else{
                gender="female";
            }
            json.put("gender",gender);
            json.put("email",UserHistory.getInstance().getUserDetail().getMailId());
            JSONArray jsonArray=new JSONArray();

            for(int i=data.size()-2;i>=1;i=i-2)
            {
                JSONObject jsonInternal=new JSONObject();
                jsonInternal.put("id",data.get(i-1).getId());
                jsonInternal.put("answer",data.get(i).getQuestion());
                jsonArray.put(jsonInternal);
            }

            JSONObject jsonInterna1l=new JSONObject();
            jsonInterna1l.put("id",data.get(data.size()-1).getId());
            jsonInterna1l.put("answer",data.get(0).getQuestion());
            jsonArray.put(jsonInterna1l);

            json.put("questions",jsonArray);

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
