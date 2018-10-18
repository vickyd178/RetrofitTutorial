package com.vickyd178.retrofittutorial.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vickyd178.retrofittutorial.R;
import com.vickyd178.retrofittutorial.activities.LoginActivity;
import com.vickyd178.retrofittutorial.activities.MainActivity;
import com.vickyd178.retrofittutorial.activities.ProfileActivity;
import com.vickyd178.retrofittutorial.api.RetrofitClient;
import com.vickyd178.retrofittutorial.models.DefaultResponse;
import com.vickyd178.retrofittutorial.models.LoginResponse;
import com.vickyd178.retrofittutorial.models.User;
import com.vickyd178.retrofittutorial.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private EditText inputEmail,inputName,inputSchool;
    private EditText inputCurrentPassword,inputNewPassword;
    private User user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputEmail = view.findViewById(R.id.inputEmail);
        inputName = view.findViewById(R.id.inputName);
        inputSchool = view.findViewById(R.id.inputSchool);
        inputCurrentPassword = view.findViewById(R.id.inputCurrentPassword);
        inputNewPassword = view.findViewById(R.id.inputNewPassword);

        view.findViewById(R.id.btnUpdate).setOnClickListener(this);
        view.findViewById(R.id.btnNewPassword).setOnClickListener(this);
        view.findViewById(R.id.btnLogout).setOnClickListener(this);
        view.findViewById(R.id.btnDeleteUser).setOnClickListener(this);
        user = SharedPrefManager.getInstance(getActivity()).getUser();

        inputEmail.setText(user.getEmail());
        inputName.setText(user.getName());
        inputSchool.setText(user.getSchool());
    }

    private void updateProfile(){
        String email = inputEmail.getText().toString().trim();
        String name = inputName.getText().toString().trim();
        String school = inputSchool.getText().toString().trim();

        if (email.isEmpty()){
            inputEmail.setError("Email Required");
            inputEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Enter valid email");
            inputEmail.requestFocus();
            return;
        }
        if (name.isEmpty()){
            inputName.setError("Name Required");
            inputName.requestFocus();
            return;
        }
        if (school.isEmpty()){
            inputSchool.setError("School Required");
            inputSchool.requestFocus();
            return;
        }

        User user = SharedPrefManager.getInstance(getActivity()).getUser();
        Call<LoginResponse> call = RetrofitClient.
                getmInstance().
                getApi().
                updateUser(
                        user.getId(),
                        email,
                        name,
                        school);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();

                if (!response.body().isError()){

                    SharedPrefManager.getInstance(getActivity()).saveUser(response.body().getUser());

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    public void updatePassword(){

        String currentPassword = inputCurrentPassword.getText().toString().trim();
        String newPassword = inputNewPassword.getText().toString().trim();
        if (currentPassword.isEmpty()){
            inputCurrentPassword.setError("Password Required");
            inputCurrentPassword.requestFocus();
            return;
        }
        if (currentPassword.length()<6){
            inputCurrentPassword.setError("Password should be atleast 6 characters long");
            inputCurrentPassword.requestFocus();
            return;
        }

        if (newPassword.isEmpty()){
            inputNewPassword.setError("Password Required");
            inputNewPassword.requestFocus();
            return;
        }
        if (newPassword.length()<6){
            inputNewPassword.setError("Password should be atleast 6 characters long");
            inputNewPassword.requestFocus();
            return;
        }


        User user = SharedPrefManager.getInstance(getActivity()).getUser();

        Call<DefaultResponse> call = RetrofitClient.
                getmInstance().
                getApi().
                updatePassword(
                        user.getId(),
                        currentPassword,
                        newPassword
                );

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {

            }
        });
    }

    private void logout(){
        SharedPrefManager.getInstance(getActivity()).clear();
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void deleteUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Are you sure?");
        builder.setMessage("This Action is irreversible...");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = SharedPrefManager.getInstance(getActivity()).getUser();
                Call<DefaultResponse> call = RetrofitClient.getmInstance().getApi().deleteUser(user.getId());

                call.enqueue(new Callback<DefaultResponse>() {
                    @Override
                    public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                        if (!response.body().isErr()){
                            SharedPrefManager.getInstance(getActivity()).clear();
                            Intent intent = new Intent(getActivity(),MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<DefaultResponse> call, Throwable t) {

                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnUpdate:
                updateProfile();
                break;
            case R.id.btnNewPassword:
                updatePassword();
                break;
            case R.id.btnLogout:
                logout();
                break;
            case R.id.btnDeleteUser:
                deleteUser();
                break;
        }
    }
}
