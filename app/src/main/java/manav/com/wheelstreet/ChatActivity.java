package manav.com.wheelstreet;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by manav on 12/5/18.
 */

public class ChatActivity extends AppCompatActivity {
    private LinearLayoutManager layoutManager;
    private RecyclerView mMessagesRecyclerView;
    private MessagesDetailAdapter adapter;
    private ImageView imgSend;
    private EditText messageEditText;
    ArrayList<QuestionModel> model=new ArrayList<QuestionModel>();
    ArrayList<QuestionModel> adapterdata=new ArrayList<QuestionModel>();


    int global=0;
    private static final String URL_TO_READ="https://api.wheelstreet.com/v1/test/questions";
    private String errormsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main_layout);
        mMessagesRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        messageEditText = (EditText) findViewById(R.id.messageEditTxt);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        mMessagesRecyclerView.setLayoutManager(layoutManager);
        if(!isNetworkAvailable())
        {
            Toast.makeText(this,"network not available Kindly connect to network",Toast.LENGTH_LONG).show();
        }
        else {
            getData();
        }

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(messageEditText.getText())) {
                    Toast.makeText(ChatActivity.this, "Answer can not be empty", Toast.LENGTH_SHORT);
                } else {

                       if(validate(messageEditText.getText().toString(),model.get(global).getDataType())){
                        addOfflineMessage(messageEditText.getText().toString());
                      }
                        messageEditText.setText("");
                }
            }
        });
    }

    private void getData() {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(URL_TO_READ,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response!=null && response.has("data")){
                            JSONArray jsonArray=response.getJSONArray("data");
                            parse(jsonArray);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

       /* JsonArrayRequest Request = new JsonArrayRequest(URL_TO_READ, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                //Dismissing progress dialog

                //calling method to parse json array
                parse(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*/

        //adding request to request queue
        AppController.getInstance(this).addtoRequestQueue(jsObjRequest);

    }

    private void parse(JSONArray response) {

        for (int i = 0; i < response.length(); i++) {

            //creating data for adapter
            QuestionModel add=new QuestionModel();
            JSONObject json=null;
            try {
                json=response.getJSONObject(i);
                add.setId(json.getString("id"));
                add.setQuestion(json.getString("question"));
                add.setDataType(json.getString("dataType"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.add(add);
        }
        adapterdata.add(model.get(global));
        adapter=new MessagesDetailAdapter(this, adapterdata);
        mMessagesRecyclerView.setAdapter(adapter);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void addOfflineMessage(String message) {
        QuestionModel offlineMessageObject = new QuestionModel();
        offlineMessageObject.setQuestion(message);
        global++;
        if(model.size()==global){
            adapterdata.add(0, offlineMessageObject);
            Intent intent =new Intent(ChatActivity.this,ChatReviewActivity.class);
            intent.putExtra("chatData",adapterdata);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(model.size()>global){
            adapterdata.add(0,model.get(global));
        }
        if(model.size()>global) {
            adapterdata.add(1, offlineMessageObject);
        }
        adapter.notifyDataSetChanged();

    }

    private boolean validate(String message, String dataType) {
        if(dataType.equals("boolean") &&  (!(message.equalsIgnoreCase("true") ||message.equalsIgnoreCase("false")))){
            errormsg="kindly enter boolean value true/false";
            return  false;
        }
        return true;
    }
}
