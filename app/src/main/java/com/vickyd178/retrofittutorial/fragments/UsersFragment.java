package com.vickyd178.retrofittutorial.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vickyd178.retrofittutorial.R;
import com.vickyd178.retrofittutorial.adapteres.UsersAdapter;
import com.vickyd178.retrofittutorial.api.RetrofitClient;
import com.vickyd178.retrofittutorial.models.User;
import com.vickyd178.retrofittutorial.models.UsersResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    UsersAdapter adapter;
    private List<User> userList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_users,container,false);
        
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Call<UsersResponse> call = RetrofitClient.getmInstance().getApi().getUsers();

        call.enqueue(new Callback<UsersResponse>() {
            @Override
            public void onResponse(Call<UsersResponse> call, Response<UsersResponse> response) {

                userList = response.body().getUsers() ;
                adapter = new UsersAdapter(getActivity(),userList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<UsersResponse> call, Throwable t) {

            }
        });


    }
}
