package com.roomdb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.roomdb.adapter.UserAdapter;
import com.roomdb.db.User;
import com.roomdb.db.UserDataBase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private UserAdapter userAdapter;
    private List<User> userArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserDataBase userDatabase;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GET_ALL_USER = 0;
    private static final int DELETE = 1;
    private static final int UPDATE = 2;
    private UserAsynTask userAsynTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        userDatabase = UserDataBase.getInstance(this);

        userAdapter = new UserAdapter(this, userArrayList, MainActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);

        userAsynTask = new UserAsynTask();
        userAsynTask.execute(GET_ALL_USER);
    }

    public void addAndEditContacts(final boolean isUpdate, final User user, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_user_title);
        final EditText userId = view.findViewById(R.id.userId);
        final EditText password = view.findViewById(R.id.password);

        contactTitle.setText(!isUpdate ? "Add New Contact" : "Edit Contact");

        if (isUpdate && user != null) {
            userId.setText(user.getUserId());
            password.setText(user.getPassword());
        }

        alertDialogBuilderUserInput
                .setPositiveButton(isUpdate ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {

                                if (isUpdate) {

                                    deleteContact(user, position);
                                } else {

                                    dialogBox.cancel();

                                }

                            }
                        });


        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(userId.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter user id!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }


                if (isUpdate && user != null) {

                    updateContact(userId.getText().toString(), password.getText().toString(), position);
                } /*else {

                    createContact(newContact.getText().toString(), contactEmail.getText().toString());
                }*/
            }
        });
    }

    private void deleteContact(User user, int position) {

        userArrayList.remove(position);

        new UserAsynTask(user).execute(DELETE);

    }

    private void updateContact(String id, String password, int position) {

        User user = userArrayList.get(position);
        new UserAsynTask(user,id,password).execute(UPDATE,position);
    }


    private class UserAsynTask extends AsyncTask<Integer, Void, Void> {
        User user;
        int commands;
        String id,password;
        UserAsynTask() {
        }

        UserAsynTask(User user) {
            this.user = user;
        }

        UserAsynTask(User user,String id,String password){
            this.user = user;
            this.id = id;
            this.password = password;
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            commands = integers[0];
            switch (integers[0]) {
                case GET_ALL_USER:
                    userArrayList.addAll(userDatabase.userDao().getAllUser());
                    break;
                case DELETE:
                    int l = userDatabase.userDao().deleteUser(user);
                    Log.d(TAG, "delete user: "+l);
                    if (l==1)
                        Log.d(TAG, "doInBackground: User deleted");
                    else
                        Log.d(TAG, "doInBackground: user not found");
                    break;
                case UPDATE:
                    int ll = userDatabase.userDao().updateUser(new User(id,password));
                    Log.d(TAG, "update user : "+ll);
                    if (ll == 1) {
                        user.setUserId(id);
                        user.setPassword(password);
                        userArrayList.set(integers[1], user);
                    }else
                        Log.d(TAG, "doInBackground: User id not found");
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            userAdapter.notifyDataSetChanged();
        }
    }
}
