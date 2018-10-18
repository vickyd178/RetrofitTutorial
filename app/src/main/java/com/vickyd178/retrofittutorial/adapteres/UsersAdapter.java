package com.vickyd178.retrofittutorial.adapteres;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vickyd178.retrofittutorial.R;
import com.vickyd178.retrofittutorial.models.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {


    private Context mCtx;
    private List<User> userList;


    public UsersAdapter(Context mCtx, List<User> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.users_list_design, viewGroup,false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int position) {
        User user = userList.get(position);
        usersViewHolder.tvEmail.setText(user.getEmail());
        usersViewHolder.tvName.setText(user.getName());
        usersViewHolder.tvSchool.setText(user.getSchool());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder{

        TextView tvName,tvEmail,tvSchool;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvName = itemView.findViewById(R.id.tvName);
            tvSchool = itemView.findViewById(R.id.tvSchool);
        }
    }
}
