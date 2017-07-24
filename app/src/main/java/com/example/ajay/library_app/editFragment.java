package com.example.ajay.library_app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ajay-5674 on 11/07/17.
 */

public class editFragment extends ListFragment {
    int mNum;
    DBHandler db;
    Cursor cursor;
    libraryBook book;
    int bookId;


    static editFragment newInstance(int num) {
        editFragment f = new editFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        db = new DBHandler(getContext());
        cursor = db.getCursor();
    }

    public libraryBook getBook() {
        return book;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("FragView ", Integer.toString(mNum));
        cursor.moveToPosition(mNum);
        bookId = cursor.getInt(0);
        book = db.getBook(bookId);

        final View v = inflater.inflate(R.layout.fragment_edit, container, false);


        TextView title = (TextView) v.findViewById(R.id.textViewTitle);
        title.setText(cursor.getString(1));

        TextView author = (TextView) v.findViewById(R.id.textViewAuthor);
        author.setText(cursor.getString(2));
        TextView publisher = (TextView) v.findViewById(R.id.textViewPublisher);
        publisher.setText(cursor.getString(3));
        TextView copies = (TextView) v.findViewById(R.id.textViewCopies);
        copies.setText("In Hand: " + cursor.getString(4));
        TextView total = (TextView) v.findViewById(R.id.textViewTotal);
        total.setText("Total :" + cursor.getString(5));

        Button subButton = (Button) v.findViewById(R.id.buttonSub);
        Button addButton = (Button) v.findViewById(R.id.buttonAdd);
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (book.getNos() != 0) {
                    book.setNos(book.getNos() - 1);
                    book.setTotal(book.getTotal() - 1);
                    TextView copies = (TextView) v.findViewById(R.id.textViewCopies);
                    copies.setText("In Hand: " + book.getNos());
                    TextView total = (TextView) v.findViewById(R.id.textViewTotal);
                    total.setText("Total :" + book.getTotal());
                    db.updateBook(book);
                } else {
                    Toast.makeText(getContext(), "There are no more copies in hand!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book.setNos(book.getNos() + 1);
                book.setTotal(book.getTotal() + 1);
                TextView copies = (TextView) v.findViewById(R.id.textViewCopies);
                copies.setText("In Hand: " + book.getNos());
                TextView total = (TextView) v.findViewById(R.id.textViewTotal);
                total.setText("Total :" + book.getTotal());
                db.updateBook(book);
            }
        });

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            /* stuff*/
        Log.d("onActCreated :", "Activity Created");

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }


}
