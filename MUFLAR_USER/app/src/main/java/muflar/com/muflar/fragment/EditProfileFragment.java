package muflar.com.muflar.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import java.util.List;

import muflar.com.muflar.R;
import muflar.com.muflar.helper.OnBackPressedListener;
import muflar.com.muflar.http.APIClient;
import muflar.com.muflar.http.APIInterface;
import muflar.com.muflar.model.UpdateProfileInfo;
import muflar.com.muflar.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileFragment extends Fragment implements OnBackPressedListener {
    LinearLayout linearLayout_otp,linearLayout_evp;
    Button button_otp,button_evp, button_save;
    FragmentManager  fragmentManager;

    EditText etFirstName;
    EditText etLastName;
    EditText etCardNo;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etPhone;
    EditText etEmail;
    EditText etAge;
    EditText etProfession;
    EditText etMarital;
    EditText etOtp;
    EditText etEvp;

    APIInterface apiInterface;

    String SHARED_PREFERENCES_Muflar = "Muflar";
    SharedPreferences sharedPref; // sharedpreference var
    SharedPreferences.Editor editor;

    String userId;
    Number id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_history, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();

        linearLayout_otp=(LinearLayout)view.findViewById(R.id.otp);
        linearLayout_evp=(LinearLayout)view.findViewById(R.id.evp);
        button_otp=(Button)view.findViewById(R.id.send_otp);
        button_evp=(Button)view.findViewById(R.id.send_evp);
        button_save=view.findViewById(R.id.bt_save);

        etFirstName = view.findViewById(R.id.et_firstname);
        etLastName = view.findViewById(R.id.et_lastname);
        etCardNo = view.findViewById(R.id.et_cardno);
        etPassword = view.findViewById(R.id.et_password);
        etConfirmPassword = view.findViewById(R.id.et_confirmpassword);
        etPhone = view.findViewById(R.id.et_phone);
        etEmail = view.findViewById(R.id.et_email);
        etAge = view.findViewById(R.id.et_age);
        etProfession = view.findViewById(R.id.et_profession);
        etMarital = view.findViewById(R.id.et_marital);
        etOtp = view.findViewById(R.id.et_otp);
        etEvp = view.findViewById(R.id.et_evp);

        button_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_otp.setVisibility(View.VISIBLE);
            }
        });
        button_evp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout_evp.setVisibility(View.VISIBLE);
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });

        apiInterface = APIClient.getClient().create(APIInterface.class);

        sharedPref =  getActivity().getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_Muflar, Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        showUserProfile();

        return view;
    }


    @Override
    public void onBackPressed() {
        fragmentManager.beginTransaction().replace(R.id.frame_layout,new Home_Fragment()).commit();
    }


    private void showUserProfile(){
        Gson gson = new Gson();            // for retrive
        String json = sharedPref.getString("user", "");
        User _user = gson.fromJson(json, User.class);

        Call<User> call = apiInterface.getUserProfile(String.valueOf(_user.id));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();

                Gson gson = new Gson();
                String json = gson.toJson(body);
                editor.putString("user", json);
                editor.commit();

                if (body == null) return;
                if (body.userID != null) userId = body.userID;
                if (body.id != null) id = body.id;
                if (body.firstName != null) etFirstName.setText(body.firstName);
                if (body.lastName != null) etLastName.setText((body.lastName));
                if (body.isEVP != null) etEvp.setText(body.isEVP.toString());
                if (body.isOTP != null) etOtp.setText(body.isOTP.toString());
                if (body.cardNumber != null) etCardNo.setText(body.cardNumber.toString());
                if (body.password != null) etPassword.setText(body.password);
                if (body.password != null) etConfirmPassword.setText(body.password);
                if (body.age != null) etAge.setText(body.age);
                if (body.maritalStatus != null) etMarital.setText(body.maritalStatus);
                if (body.profession != null) etProfession.setText(body.profession);
                if (body.email != null) etEmail.setText(body.email);
                if (body.mobile != null) etPhone.setText(body.mobile);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();

                return;
            }
        });
    }

    private void updateUserProfile() {

        UpdateProfileInfo user = new UpdateProfileInfo();
        user.userID = userId;
        user.firstName = etFirstName.getText().toString();
        user.lastName = etLastName.getText().toString();
        user.cardNumber = etCardNo.getText().toString();
        user.age = etAge.getText().toString();
        user.email = etEmail.getText().toString();
        user.mobile = etPhone.getText().toString();
        user.profession = etProfession.getText().toString();
        user.maritalStatus = etMarital.getText().toString();
        user.addressLine1 = "";
        user.addressLine2 = "";
        user.addressLine3 = "";

        Call<User> call = apiInterface.updateUserProfile(String.valueOf(id), user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User body = response.body();

                Gson gson = new Gson();
                String json = gson.toJson(body);
                editor.putString("user", json);
                editor.commit();

                Toast.makeText(getActivity(), "Your profile updated successfully!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Your profile'updating is falied", Toast.LENGTH_LONG).show();
                new MaterialDialog.Builder(getContext())
                        .title("Error")
                        .content("Connection Failed.")
                        .positiveText("Ok")
                        .show();

                return;
            }
        });
    }
}
