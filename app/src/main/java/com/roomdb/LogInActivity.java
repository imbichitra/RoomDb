package com.roomdb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.roomdb.db.User;
import com.roomdb.db.UserDataBase;

public class LogInActivity extends AppCompatActivity {

    public static final String TAG = LogInActivity.class.getSimpleName();
    EditText editText,editText1;
    private UserDataBase userDataBase;
    private static final int LOGIN =0;
    private static final int SIGN_UP =1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        editText = findViewById(R.id.userId);
        editText1 = findViewById(R.id.password);

        userDataBase = UserDataBase.getInstance(this);

    }

    public void logIn(View view) {
        User us = new User(editText.getText().toString(),editText1.getText().toString());
        new LogInAsyncTask(us).execute(LOGIN);
    }

    public void signUp(View view) {
        User us = new User(editText.getText().toString(),editText1.getText().toString());
        new LogInAsyncTask(us).execute(SIGN_UP);
    }

    private class LogInAsyncTask extends AsyncTask<Integer,Void,Void>{
        User user;
        LogInAsyncTask(User user){
            this.user = user;
        }
        @Override
        protected Void doInBackground(Integer... integers) {
            if (integers[0] == LOGIN ) {
                User l = userDataBase.userDao().getUser(user.getUserId(),user.getPassword());

                if (l!=null){
                    startActivity(new Intent(LogInActivity.this,MainActivity.class));
                }else {
                    Log.d(TAG, "doInBackground: Not found");
                }

            }else {
                try {
                    Log.d(TAG, "doInBackground: " + userDataBase.userDao().insertUser(user));
                }catch (SQLiteConstraintException e){
                    Log.d(TAG, "doInBackground: "+e.getMessage());
                }

            }
            return null;
        }
    }
}
