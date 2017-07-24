package com.example.ajay.library_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    ListView bookList;
    MyCursorAdapter myAdapter;
    static DBLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_library);
        //loader = new DBLoader(this);
        //dloader.loadInBackground();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("My Library");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().initLoader(1, null, this);//.forceLoad();

        final Context context = this;
        // myAdapter = new MyCursorAdapter(TestActivity.this, null, 0);
        DBHandler db = new DBHandler(this);

        final Cursor cursor = db.getCursor();
        int totalBooks = 0;

        totalBooks = db.getBookCount();
        //String[] fromField = new String[]{db.getColName(), db.getColAuth(), db.getColTot(), db.getColID()};
        // int[] toViewIDs = new int[]{R.id.listTitle, R.id.listAuthor, R.id.listCopies};
        bookList = (ListView) findViewById(R.id.android_list);
        // bookList.setAdapter(myAdapter);
        myAdapter = new MyCursorAdapter(this, null, 0);
        bookList.setAdapter(myAdapter);
        View header = (View) getLayoutInflater().inflate(R.layout.row_layout_title, null);
        TextView totals = header.findViewById(R.id.mainText);
        totals.setText("Total Books: " + Integer.toString(totalBooks));
        bookList.addHeaderView(header);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i != 0) {
                    Log.d("i ", Integer.toString(i));
                    cursor.moveToPosition(i - 1);

                    libraryBook book = new libraryBook(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));
                    cursor.close();
                    Intent intent = new Intent(view.getContext(), swipeEditActivity.class);
                    intent.putExtra("number", i - 1);
                    startActivity(intent);
                    finish();
                }


            }
        });
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
                final ListView lw = (ListView) findViewById(R.id.android_list);

                if (scrollState == 0)


                    if (view.getId() == lw.getId()) {
                        final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
                        if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                            mIsScrollingUp = false;

                        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                            mIsScrollingUp = true;
                        }

                        mLastFirstVisibleItem = currentFirstVisibleItem;
                    }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_basic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, NewBookScreenActivity.class);
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


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
       /*CursorLoader loader = new CursorLoader(this, null, null, null, null, null){
            @Override
            public Cursor loadInBackground(){
                DBHandler db = new DBHandler(getContext());
                return db.getCursor();
            }
       };*/
        Log.d("Loader ", "onCreateLoader");
        loader = new DBLoader(this);
        // loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("Loader ", "onLoadFinished");
        this.loader = (DBLoader) loader;
        myAdapter.changeCursor(data);
        if (data == null) Log.d("Cursor ", " is null");
        // myAdapter.swapCursor(data);
        // myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myAdapter.swapCursor(null);

    }
}




