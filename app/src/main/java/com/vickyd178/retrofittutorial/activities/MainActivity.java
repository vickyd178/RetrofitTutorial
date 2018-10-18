package com.vickyd178.retrofittutorial.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vickyd178.retrofittutorial.R;
import com.vickyd178.retrofittutorial.api.RetrofitClient;
import com.vickyd178.retrofittutorial.models.DefaultResponse;
import com.vickyd178.retrofittutorial.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText inputEmail,inputPassword,inputName,inputSchool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputName = findViewById(R.id.inputName);
        inputSchool = findViewById(R.id.inputSchool);

        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.tvSignIn).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void  userSignUp(){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
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
        if (password.isEmpty()){
            inputPassword.setError("Password Required");
            inputPassword.requestFocus();
            return;
        }
        if (password.length()<6){
            inputPassword.setError("Password should be atleast 6 characters long");
            inputPassword.requestFocus();
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

        Call<DefaultResponse> call = RetrofitClient
                .getmInstance()
                .getApi()
                .createser(email,password,name,school);

        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                DefaultResponse dr = response.body();
                if (response.code()==201){

                    Toast.makeText(MainActivity.this, dr.getMsg(), Toast.LENGTH_SHORT).show();

                }else if (response.code()==422)
                {

                    Toast.makeText(MainActivity.this,"User already exist", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


//        Call<ResponseBody> call = RetrofitClient
//                .getmInstance()
//                .getApi()
//                .createser(email,password,name,school);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//
//                String s = null;
//
//                try {
//                if (response.code()==201) {
//                        s = response.body().string();
//                }else {
//                        s = response.errorBody().string();
//                    }
//                }catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if (s!=null){
//                    try {
//                        JSONObject object = new JSONObject(s);
////                        boolean error = object.getBoolean("error");
////                        if (error){
////
////                        }
//
//                        Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_LONG).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignUp:
                userSignUp();
                break;
            case R.id.tvSignIn:
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }
    }
}
