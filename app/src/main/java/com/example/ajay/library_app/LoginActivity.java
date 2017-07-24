package com.example.ajay.library_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    private static String user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    /**
     * Called when user logs in as Admin TODO:Change this code to an onclicklistener
     */
    public void logAdmin(View view) {
        Intent intent = new Intent(this, AdminLibraryActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Called when user logs in as User TODO:Change this code to an onclicklistener
     */
    public void logUser(View view) {
        final Intent intent = new Intent(this, UserLibraryActivity.class);
        final CharSequence users[] = new CharSequence[]{"User1", "User2", "User3", "User4"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select User");
        builder.setItems(users, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                user = users[which].toString();

                intent.putExtra("USER_NAME", user);

                startActivity(intent);
                finish();
            }
        });
        builder.show();


    }

    public void testCode(View view){
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
        finish();
    }
}
