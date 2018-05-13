package manav.com.wheelstreet;

/**
 * Created by manav on 12/5/18.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

public class UserDetailFragment extends Fragment {

    EditText Name,Mail,Number;
    ImageView Avtar;
    TextView submit;
    RadioGroup radioGroup;
    String gender="1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_detail, container, false);
        Name=(EditText) view.findViewById(R.id.name);
        Mail=(EditText) view.findViewById(R.id.mail);
        Number=(EditText) view.findViewById(R.id.number);
        Avtar=(ImageView)view.findViewById(R.id.avatar);
        submit=(TextView)view.findViewById(R.id.submit);
        radioGroup=(RadioGroup)view.findViewById(R.id.radioSex);


        UserDetailModel userDetailModel=UserHistory.getInstance().getUserDetail();
        if(userDetailModel!=null){
            Name.setText(userDetailModel.getName());
            Mail.setText(userDetailModel.getMailId());
            Number.setText(userDetailModel.getContact()
            );
            if(userDetailModel.getGender()!=null && userDetailModel.getGender().equalsIgnoreCase("1"))
            ((RadioButton)view.findViewById(R.id.male)).setChecked(true);
            else if(userDetailModel.getGender()!=null && userDetailModel.getGender().equalsIgnoreCase("0")){
                ((RadioButton)view.findViewById(R.id.Female)).setChecked(true);
            }
            if(userDetailModel.getImageUrl()!=null){
                Picasso.with(getActivity()).load(userDetailModel.getImageUrl()).into(Avtar);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.male) {
                    gender = "1";
                } else if (checkedId == R.id.Female) {
                    gender = "0";
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    UserDetailModel userDetailModel = new UserDetailModel();
                    userDetailModel.setName(Name.getText().toString());
                    userDetailModel.setMailId(Mail.getText().toString());
                    userDetailModel.setContact(Number.getText().toString());
                    userDetailModel.setGender(gender);
                    userDetailModel.setImageUrl(UserHistory.getInstance().getUserDetail().getImageUrl()); // using same url alwys
                    UserHistory.getInstance().SaveUserDetail(userDetailModel);

                    Toast.makeText(getActivity(),"Details Saved Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(getActivity(),ChatActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private boolean validate() {

        int error = 0;
        String errorTxt ="error";

        String mobilePattern = "^([7-8-9]{1}[0-9]{9})+$";
        if (error == 0) {
            if (Number.getText().toString().trim().length() == 0 ) {
                errorTxt = "Mobile number Cannot be empty";
                error++;
            }
            else if (Number.getText().toString().trim().length()< 10 || (!Number.getText().toString().matches(mobilePattern))) {
                errorTxt = "Enter valid 10 digit mobile number";
                error++;
            }
        }

        if(Mail.getText().toString().length() > 0 && error == 0){

            if( !Patterns.EMAIL_ADDRESS.matcher(Mail.getText().toString()).matches()) {

                errorTxt = "Invalid Email";
                error++;
            }
        }

        if (error == 0) {
            if (Name.getText().toString().trim().length() == 0) {
                errorTxt = "Full Name Mandatory";
                error++;
            }

        }


        if (error > 0) {
            error = 0;
            Toast.makeText(getActivity(),errorTxt,Toast.LENGTH_SHORT).show();
            return false;

        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
