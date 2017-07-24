package com.example.ajay.library_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class swipeEditActivity extends AppCompatActivity {
    libraryBook currentBook;
    int number;
    String user;
    editAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_edit);

        Bundle extras = getIntent().getExtras();
        number = extras.getInt("number");
        user = extras.getString("USER_NAME");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        myToolbar.setTitle("Edit Copies");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new editAdapter(getSupportFragmentManager(), this);
        mPager = (ViewPager) findViewById(R.id.pager2);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(number);

        DBHandler db = new DBHandler(this);

        currentBook = new libraryBook();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_trash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_trash:
                int count = mPager.getCurrentItem();


                final DBHandler db = new DBHandler(this);
                Cursor cursor = db.getCursor();
                cursor.moveToPosition(count);
                currentBook = db.getBook(cursor.getInt(0));
                Log.d("Current Item: ", currentBook.getName());
                AlertDialog.Builder builder;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Delete Book from Library")
                        .setMessage("Are you sure you want to remove this book from your library?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteBook(currentBook);
                                Intent intent = new Intent(getBaseContext(), AdminLibraryActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                cursor.close();
                db.close();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminLibraryActivity.class);
        startActivity(intent);
        finish();
    }
}
