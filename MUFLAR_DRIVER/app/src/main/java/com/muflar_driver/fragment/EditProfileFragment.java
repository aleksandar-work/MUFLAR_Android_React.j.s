package com.muflar_driver.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import java.util.List;
import com.muflar_driver.R;
import com.muflar_driver.helper.OnBackPressedListener;
import com.muflar_driver.http.APIClient;
import com.muflar_driver.http.APIInterface;
import com.muflar_driver.model.LoginResponse;
import com.muflar_driver.model.UpdateProfileInfo;
import com.muflar_driver.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditProfileFragment extends Fragment implements OnBackPressedListener {
    Button  button_save;
    FragmentManager  fragmentManager;

    EditText etFirstName;
    EditText etLastName;
    EditText etBusRoute;
    EditText etBusID;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etPhone;
    EditText etEmail;
    EditText etAddress;
    EditText etPinCode;
    EditText etAccountNumber;
    EditText etIFCSCode;

    APIInterface apiInterface;

    String SHARED_PREFERENCES_Muflar = "Muflar";
    SharedPreferences sharedPref; // sharedpreference var
    SharedPreferences.Editor editor;

    String userId;
    String busId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        fragmentManager=getActivity().getSupportFragmentManager();

        button_save=view.findViewById(R.id.bt_save);

        etFirstName = view.findViewById(R.id.et_firstname);
        etLastName = view.findViewById(R.id.et_lastname);
//        etPassword = view.findViewById(R.id.et_password);
//        etConfirmPassword = view.findViewById(R.id.et_confirm_password);
        etPhone = view.findViewById(R.id.et_phone);
        etEmail = view.findViewById(R.id.et_email);
        etBusID = view.findViewById(R.id.et_bus_id);
//        etBusRoute = view.findViewById(R.id.et_bus_route);
        etAddress = view.findViewById(R.id.et_address);
        etPinCode = view.findViewById(R.id.et_pincode);
        etAccountNumber = view.findViewById(R.id.et_ac_no);
        etIFCSCode = view.findViewById(R.id.et_ifcs_code);

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
        User user = gson.fromJson(json, User.class);

        Call<LoginResponse> call = apiInterface.getUserProfile(String.valueOf(user.bus_ID));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse body = response.body();

                Gson gson = new Gson();
                String json = gson.toJson(body);
                editor.putString("user", json);
                editor.commit();

                if (body == null) return;
                if (body.id != null) userId = String.valueOf(body.id);
                if (body.bus_ID != null) busId = String.valueOf(body.bus_ID);
                if (body.firstName != null) etFirstName.setText(body.firstName);
                if (body.lastName != null) etLastName.setText((body.lastName));
                if (body.bus_ID != null) etBusID.setText(body.bus_ID.toString());
                if (body.mobile != null) etPhone.setText(body.mobile.toString());
                if (body.pincode != null) etPinCode.setText(body.pincode.toString());
                if (body.accountNumber != null) etAccountNumber.setText(body.accountNumber.toString());
                if (body.email != null) etEmail.setText(body.email);
                if (body.addressLine1 != null) etAddress.setText(body.addressLine1);
                if (body.IFSC != null) etIFCSCode.setText(body.IFSC);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
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
        user.id = Integer.valueOf(userId);
        user.firstName = etFirstName.getText().toString();
        user.lastName = etLastName.getText().toString();
        user.email = etEmail.getText().toString();
        user.mobile = etPhone.getText().toString();
        user.bus_ID = busId;
        user.addressLine1 = etAddress.getText().toString();
        user.accountNumber = etAccountNumber.getText().toString();
        user.IFSC = etIFCSCode.getText().toString();
        user.addressLine1 = etAddress.getText().toString();
        user.addressLine2 = etAddress.getText().toString();
        user.addressLine2 = etAddress.getText().toString();
        user.pincode = etPinCode.getText().toString();
        user.state = "";
        user.city = "";
        user.pincode = etPinCode.getText().toString();

        Call<User> call = apiInterface.updateUserProfile(userId, user);
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
