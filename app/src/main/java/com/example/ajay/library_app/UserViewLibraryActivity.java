package com.example.ajay.library_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserViewLibraryActivity extends AppCompatActivity implements MyCursorCheckboxAdapter.Checker {


    ListView bookList;
    private static String user;

    /*
     * isSelectable indicates whether at least one checkbox is checked
     * uses:
     *      OnCreate : isSelectable = false;
     */

    private boolean isSelectable;

    MyCursorCheckboxAdapter myAdapter;
    Cursor cursor;
    DBHandler db;
    private static final int BOOK_LIMIT = 10;

    /*
    * inSelectMode indicates whether checkboxes are active and visible
    * uses:
    *       OnCreate : inSelectMode = false;
    */

    boolean inSelectMode;

    TextView totalSelectedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_library);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                user = null;
            } else {
                user = extras.getString("USER_NAME");
            }
        } else {
            user = (String) savedInstanceState.getSerializable("USER_NAME");
        }
        totalSelectedTextView = (TextView) findViewById(R.id.bookTotal);
        totalSelectedTextView.setVisibility(View.INVISIBLE);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Library");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isSelectable = false;
        inSelectMode = false;

        //totalSelected = (TextView)findViewById(R.id.bookTotal);


        db = new DBHandler(this);

        cursor = db.getCursor();
        String[] fromField = new String[]{db.getColName(), db.getColAuth(), db.getColNos()};
        int[] toViewIDs = new int[]{R.id.listTitle, R.id.listAuthor, R.id.listCopies};

        myAdapter = new MyCursorCheckboxAdapter(UserViewLibraryActivity.this, cursor, 0);
        bookList = (ListView) findViewById(R.id.android_list);
        bookList.setAdapter(myAdapter);

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (inSelectMode) {
                    CheckBox checkBox = view.findViewById(R.id.checkBox);
                    if (checkBox.isEnabled()) {
                        checkBox.performClick();
                         int books = myAdapter.getCheckCount();
                        totalSelectedTextView.setText("You've selected " + ((books == 0) ? "no" : Integer.toString(books)) + ((books == 1) ? " book" : " books"));
                        Log.d("Check count :",Integer.toString(myAdapter.getCheckCount()));
                    }

                } else {
                    cursor.moveToPosition(i);
                    libraryBook book = new libraryBook(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                            cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));

                    Intent intent = new Intent(view.getContext(), swipeBorrowActivity.class);
                    intent.putExtra("number", i);
                    intent.putExtra("USER_NAME", user);
                    startActivity(intent);
                    finish();
                }

            }
        });
        bookList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!inSelectMode) {
                    inSelectMode = true;
                    myAdapter.setCheckMode(true);

                    CheckBox checkBox = view.findViewById(R.id.checkBox);

                    if (checkBox.isEnabled()) {
                        checkBox.performClick();
                    }

                }
                int books = myAdapter.getCheckCount();
                totalSelectedTextView.setText("You've selected " + ((books == 0) ? "no" : Integer.toString(books)) + ((books == 1) ? " book" : " books"));
                totalSelectedTextView.setVisibility(View.VISIBLE);
                return true;
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

        final Context context = this;
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

    private void showTotal() {
        int books = myAdapter.getCheckCount();
        totalSelectedTextView.setVisibility(View.VISIBLE);
        totalSelectedTextView.setText("You've selected " + ((books == 0) ? "no" : Integer.toString(books)) + ((books == 1) ? " book" : " books"));
    }

    private void hideTotal() {
        totalSelectedTextView.setVisibility(View.GONE);
    }

    private void updateTotal() {
        int books = myAdapter.getCheckCount();
        // totalSelected.setVisibility(View.VISIBLE);
        totalSelectedTextView.setText("You've selected " + ((books == 0) ? "no" : Integer.toString(books)) + ((books == 1) ? " book" : " books"));

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tick, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem select = menu.findItem(R.id.action_select);
        if (myAdapter.getCheckCount() > 0) {
            select.setEnabled(true);
            select.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
        } else {
            select.setEnabled(false);
            select.getIcon().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        UserDBHandler udb = new UserDBHandler(this);
        Cursor cursor_u = udb.getCursor(user);
        int user_book_count = udb.getBookCount(user);
        int user_books = 0;
        for (int i = 0; i < user_book_count; i++) {
            cursor_u.moveToPosition(i);
            user_books += cursor_u.getInt(4);
        }
        cursor_u.close();
        Log.d("User book count :", Integer.toString(user_books));
        switch (item.getItemId()) {
            case R.id.action_select:
                boolean[] selectArray = myAdapter.getSelection();
                Log.d("Select Array length :", Integer.toString(selectArray.length));
                Log.d("books left :", Integer.toString(BOOK_LIMIT - user_books));

                if (myAdapter.getCheckCount() > BOOK_LIMIT - user_books)
                    Toast.makeText(this, "You can only borrow " + Integer.toString(BOOK_LIMIT - user_books) + " more book(s)", Toast.LENGTH_SHORT).show();
                else {
                    for (int i = 0; i < selectArray.length; i++) {
                        if (selectArray[i]) {
                            Log.d("Selected at :", Integer.toString(i));
                            cursor.moveToPosition(i);
                            libraryBook currentBook = db.getBook(cursor.getInt(0));
                            libraryBook userBook;
                            currentBook.setNos(currentBook.getNos() - 1);
                            db.updateBook(currentBook);
                            userBook = udb.getBook(currentBook.getId(), user);
                            if (userBook == null) {
                                currentBook.setNos(1);
                                udb.addBook(currentBook, user);
                            } else {
                                userBook.setNos(userBook.getNos() + 1);
                                udb.updateBook(userBook, user);
                            }

                        }
                    }
                }
                udb.close();
                Intent intent = new Intent(this, UserViewLibraryActivity.class);
                intent.putExtra("USER_NAME", user);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }

    @Override
    public void onBackPressed() {

        if (inSelectMode) {
            myAdapter.setCheckMode(false); // Resets checks and Notifies Data change
            inSelectMode = false;
            totalSelectedTextView.setVisibility(View.INVISIBLE);

        } else {
            Intent intent = new Intent(this, UserLibraryActivity.class);
            intent.putExtra("USER_NAME", user);
            startActivity(intent);
            finish();
        }

    }


    @Override
    public void isAnyChecked(boolean isChecked) {
        if (isChecked) {
            isSelectable = true;
            invalidateOptionsMenu();
        } else {
            isSelectable = false;
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onCheckBoxClick(){
        int books = myAdapter.getCheckCount();
        totalSelectedTextView.setText("You've selected " + ((books == 0) ? "no" : Integer.toString(books)) + ((books == 1) ? " book" : " books"));
    }
}




