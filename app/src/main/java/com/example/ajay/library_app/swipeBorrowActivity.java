package com.example.ajay.library_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class swipeBorrowActivity extends AppCompatActivity {
    int user_book_count;
    int number;
    bookAdapter mAdapter;
    ViewPager mPager;
    static libraryBook currentBook, userBook;
    static String user;
    private static final int BOOK_LIMIT = 10;

    public int getCount() {
        return BOOK_LIMIT - user_book_count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Log.d("swipeAct :","onCreated");
        Bundle extras = getIntent().getExtras();
        number = extras.getInt("number");
        //  Log.d("swipeAct :","extra-cted");
        setContentView(R.layout.activity_swipe_borrow);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Select Book");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayListFragment.setNumber(number);
        user = extras.getString("USER_NAME");
        UserDBHandler udb = new UserDBHandler(this);
        final Cursor cursor_u = udb.getCursor(user);
        int book_nos = udb.getBookCount(user);
        user_book_count = 0;
        for (int i = 0; i < book_nos; i++) {
            cursor_u.moveToPosition(i);
            user_book_count += cursor_u.getInt(4);
        }
        cursor_u.close();
        udb.close();


        mAdapter = new bookAdapter(getSupportFragmentManager(), this);
        mAdapter.setBooksLeft(getCount());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);
        //Log.d("number :",Integer.toString(number));
        mPager.setCurrentItem(number);

        DBHandler db = new DBHandler(this);
        Log.d("LendBook-" + user + " : ", " Databases Started");
        currentBook = new libraryBook();
        userBook = new libraryBook();

        //currentBook = db.getBook(bookid);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empty, menu);
        return true;
    }


    public static class bookAdapter extends FragmentStatePagerAdapter {
        private Context context;
        int num_items;
        int books_left;

        public bookAdapter(FragmentManager fm, Context c) {
            super(fm);
            context = c;
            DBHandler db = new DBHandler(context);
            num_items = db.getBookCount();
            db.close();
        }

        public void setBooksLeft(int i) {
            books_left = i;
        }

        @Override
        public int getCount() {

            return num_items;
        }

        @Override
        public Fragment getItem(int position) {


            ArrayListFragment f = ArrayListFragment.newInstance(position);
            f.setLimit(books_left);
            return f;

        }

    }

    public static class ArrayListFragment extends ListFragment {
        int mNum;
        static int number;
        DBHandler db;
        Cursor cursor;
        int borrowCopies, copiesMax;
        int books_left;

        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
            f.borrowCopies = 1;


            return f;
        }

        public void setLimit(int i) {
            books_left = i;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : number;
            db = new DBHandler(getContext());
            cursor = db.getCursor();

        }

        public static void setNumber(int num) {
            number = num;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            cursor.moveToPosition(mNum);
            View v = inflater.inflate(R.layout.fragment_swipe, container, false);
            final TextView borrow = (TextView) v.findViewById(R.id.returnnos);
            borrow.setText(Integer.toString(borrowCopies));
            View tv = v.findViewById(R.id.textTitle);
            ((TextView) tv).setText(cursor.getString(1));
            tv = v.findViewById(R.id.textAuth);
            ((TextView) tv).setText(cursor.getString(2));
            tv = v.findViewById(R.id.textNos);
            ((TextView) tv).setText("Copies : " + cursor.getString(4));
            copiesMax = cursor.getInt(4);
            //tv = v.findViewById(R.id.textViewID);
            //((TextView)tv).setText(cursor.getString(0));
            Button borrowButton = v.findViewById(R.id.buttonBorrow2);
            borrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentBook = db.getBook(cursor.getInt(0));
                    Log.d("Current Book : ", currentBook.getName());
                    UserDBHandler udb = new UserDBHandler(getContext());


                    if (currentBook.getNos() == 0) {
                        Toast.makeText(getContext(), "No Book Available!",
                                Toast.LENGTH_LONG).show();
                    } else if (borrowCopies > books_left) {
                        Toast.makeText(getContext(), "You can't borrow any more books. Return some books to borrow more ", Toast.LENGTH_SHORT).show();
                    } else if (borrowCopies > copiesMax) {
                        Toast.makeText(getContext(), "This book is not available right now. Try again later", Toast.LENGTH_SHORT).show();
                    } else {
                        currentBook.setNos(currentBook.getNos() - borrowCopies);
                        db.updateBook(currentBook);
                        userBook = udb.getBook(currentBook.getId(), user);
                        if (userBook == null) {
                            currentBook.setNos(borrowCopies);
                            udb.addBook(currentBook, user);
                        } else {
                            userBook.setNos(userBook.getNos() + borrowCopies);
                            udb.updateBook(userBook, user);
                        }

                        Intent intent = new Intent(getContext(), swipeBorrowActivity.class);
                        intent.putExtra("USER_NAME", user);
                        intent.putExtra("number", mNum);
                        startActivity(intent);

                    }
                }
            });

            Button subButton = (Button) v.findViewById(R.id.buttonSub);
            Button addButton = (Button) v.findViewById(R.id.buttonAdd);
            subButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (borrowCopies != 1) {
                        borrowCopies--;
                        borrow.setText(Integer.toString(borrowCopies));
                    } else {
                        Toast.makeText(getContext(), "You can't borrow any less than that!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (borrowCopies < copiesMax && borrowCopies < books_left) {
                        borrowCopies++;
                        borrow.setText(Integer.toString(borrowCopies));
                    } else if (borrowCopies >= copiesMax) {
                        Toast.makeText(getContext(), "There are no more copies available", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "You can only borrow " + books_left + " more book(s). Try returning some books to borrow more", Toast.LENGTH_SHORT).show();

                    }
                }
            });


            libraryBook book = new libraryBook(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), Integer.parseInt(cursor.getString(4)));

            return v;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            cursor.close();
            db.close();
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, UserViewLibraryActivity.class);
        intent.putExtra("USER_NAME", user);
        startActivity(intent);
        finish();
    }


}





