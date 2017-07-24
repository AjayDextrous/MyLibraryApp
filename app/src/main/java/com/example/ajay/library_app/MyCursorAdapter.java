package com.example.ajay.library_app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by ajay-5674 on 17/07/17.
 */

public class MyCursorAdapter extends CursorAdapter {

    private LayoutInflater cursorInflater;

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return cursorInflater.inflate(R.layout.row_layout, parent, false);
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor) {
        Log.d("Cursor :", cursor.getString(1));
        TextView title = (TextView) v.findViewById(R.id.listTitle);
        title.setText(cursor.getString(1));
        TextView author = (TextView) v.findViewById(R.id.listAuthor);
        author.setText(cursor.getString(2));
        TextView copies = (TextView) v.findViewById(R.id.listCopies);
        copies.setText(cursor.getString(5));

    }

}
