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
import com.vickyd178.retrofittutorial.models.LoginResponse;
import com.vickyd178.retrofittutorial.storage.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText inputEmail,inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        findViewById(R.id.btnSignIn).setOnClickListener(this);
        findViewById(R.id.tvSignUp).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userLogin(){
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

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
        Call<LoginResponse> call = RetrofitClient
                .getmInstance()
                .getApi()
                .userlogin(email,password);


        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();

                if (response.code()==401){

                    Toast.makeText(LoginActivity.this,"Unauthorized access please contact admin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!loginResponse.isError()){
                    SharedPrefManager.getInstance(LoginActivity.this)
                            .saveUser(loginResponse.getUser());

                    Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();

                }else{

                    Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSignIn:
                userLogin();
                break;
            case R.id.tvSignIn:
                finish();
                break;
        }
    }
}
