package com.vickyd178.retrofittutorial.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vickyd178.retrofittutorial.R;
import com.vickyd178.retrofittutorial.storage.SharedPrefManager;

public class HomeFragment extends Fragment {

    private TextView tvEmail,tvName,tvSchool;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvName = view.findViewById(R.id.tvName);
        tvSchool = view.findViewById(R.id.tvSchool);

        tvEmail.setText(SharedPrefManager.getInstance(getActivity()).getUser().getEmail());
        tvName.setText(SharedPrefManager.getInstance(getActivity()).getUser().getName());
        tvSchool.setText(SharedPrefManager.getInstance(getActivity()).getUser().getSchool());
    }
}
