package com.roomdb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roomdb.MainActivity;
import com.roomdb.R;
import com.roomdb.db.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    private Context context;
    private List<User> userList;
    private MainActivity mainActivity;

    public UserAdapter(Context context, List<User> userList, MainActivity mainActivity) {
        this.context = context;
        this.userList = userList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder,final int position) {
        final User user = userList.get(position);

        holder.userId.setText(user.getUserId());
        holder.password.setText(user.getPassword());

        holder.itemView.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                mainActivity.addAndEditContacts(true, user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView userId;
        public TextView password;


        public MyViewHolder(View view) {
            super(view);
            userId = view.findViewById(R.id.userId);
            password = view.findViewById(R.id.password);
        }
    }

}
