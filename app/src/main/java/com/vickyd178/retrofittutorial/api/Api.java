package com.vickyd178.retrofittutorial.api;

import com.vickyd178.retrofittutorial.models.DefaultResponse;
import com.vickyd178.retrofittutorial.models.LoginResponse;
import com.vickyd178.retrofittutorial.models.User;
import com.vickyd178.retrofittutorial.models.UsersResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    @FormUrlEncoded
    @POST("createuser")
    Call<DefaultResponse> createser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("name") String name,
            @Field("school") String school
    );

    @FormUrlEncoded
    @POST("loginuser")
    Call<LoginResponse> userlogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("allusers")
    Call<UsersResponse> getUsers();

    @FormUrlEncoded
    @PUT("updateuser/{id}")
    Call<LoginResponse> updateUser(
            @Path("id") int id,
            @Field("email") String email,
            @Field("name") String name,
            @Field("school") String school

    );

    @FormUrlEncoded
    @PUT("updatepassword/{id}")
        Call<DefaultResponse> updatePassword(
                @Path("id") int id,
                @Field("currentpassword") String currentpassword,
                @Field("newpassword") String newpassword
    );

    @DELETE("deleteuser/{id}")
    Call<DefaultResponse> deleteUser(
            @Path("id") int id
    );
}
