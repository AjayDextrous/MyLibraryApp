package com.example.ajay.library_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class swipeReturnActivity extends AppCompatActivity {

    String user;
    int number;
    returnAdapter mAdapter;
    ViewPager mPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_return);

        Bundle extras = getIntent().getExtras();
        user = extras.getString("USER_NAME");
        number = extras.getInt("number");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar2);
        myToolbar.setTitle("Return Book");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAdapter = new returnAdapter(getSupportFragmentManager(), this);
        mAdapter.setUser(user);

        mPager = (ViewPager) findViewById(R.id.pager2);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(number);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, UserLibraryActivity.class);
        intent.putExtra("USER_NAME", user);
        startActivity(intent);
        finish();
    }
}
