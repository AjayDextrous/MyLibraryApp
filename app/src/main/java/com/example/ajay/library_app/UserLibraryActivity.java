package com.example.ajay.library_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class UserLibraryActivity extends AppCompatActivity {

    ListView bookList;
    private static String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_library);

        Bundle Extras = getIntent().getExtras();
        user = Extras.getString("USER_NAME");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(user + "'s Books");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Context context = this;

        UserDBHandler db = new UserDBHandler(this);
        final TextView totalView = (TextView) findViewById(R.id.bookTotal);


        if (db.getBookCount(user) != 0) {
            final Cursor cursor = db.getCursor(user);
            int book_nos = db.getBookCount(user);
            int bookTotal = 0;
            for (int i = 0; i < book_nos; i++) {
                cursor.moveToPosition(i);
                bookTotal += cursor.getInt(4);
            }

            totalView.setText("You've borrowed " + Integer.toString(bookTotal) + " book(s)");
            String[] fromField = new String[]{db.getColName(), db.getColAuth(), db.getColNos()};
            int[] toViewIDs = new int[]{R.id.listTitle, R.id.listAuthor, R.id.listCopies};
            SimpleCursorAdapter myAdapter;
            myAdapter = new SimpleCursorAdapter(this, R.layout.row_layout, cursor, fromField, toViewIDs, 0);
            bookList = (ListView) findViewById(R.id.android_list_user);
            bookList.setAdapter(myAdapter);
            bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    cursor.moveToPosition(i);
                    Intent intent = new Intent(view.getContext(), swipeReturnActivity.class);
                    intent.putExtra("number", i);
                    intent.putExtra("USER_NAME", user);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            bookList = (ListView) findViewById(R.id.android_list_user);
            TextView emptyText = (TextView) findViewById(R.id.empty);
            bookList.setEmptyView(emptyText);
        }

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookList.setSelection(0);
            }
        });
        bookList.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;
            boolean mIsScrollingUp = false;
            boolean isHidden = true;

            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Animation hide = AnimationUtils.loadAnimation(context, R.anim.total_slider_hide);
                hide.setFillAfter(true);
                Animation reveal = AnimationUtils.loadAnimation(context, R.anim.total_slider_reveal);
                reveal.setFillAfter(true);

                if (firstVisibleItem != 0 && isHidden) {
                    fab.startAnimation(reveal);
                    isHidden = false;
                } else if (firstVisibleItem == 0 && !isHidden) {
                    fab.startAnimation(hide);
                    isHidden = true;
                }

                mLastFirstVisibleItem = firstVisibleItem;

            }

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic_3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_userlib:
                Intent intent = new Intent(this, UserViewLibraryActivity.class);
                intent.putExtra("USER_NAME", user);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
