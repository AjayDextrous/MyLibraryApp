package com.example.ajay.library_app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by ajay-5674 on 19/07/17.
 */

public class MyCursorCheckboxAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private boolean[] checkedArray;
    Checker checker;
    int checkCount;
    boolean isCheckMode;

    public boolean[] getSelection() {
        return checkedArray;
    }

    public int getCheckCount() {
      //  Log.d("checkCount :", Integer.toString(checkCount));
        return checkCount;
    }

    public void setCheckMode(boolean checkMode) {
       // Log.d("setCheckMode :", checkMode ? " true" : " false");
        this.isCheckMode = checkMode;
        notifyDataSetChanged();

        if (!checkMode) {
            checker.isAnyChecked(false);
            checkCount = 0;
            for(int i=0;i<checkedArray.length;i++){
                checkedArray[i]=false;
            }

        }
    }

    public boolean getCheckMode() {
        return isCheckMode;
    }

    public interface Checker {
        public void isAnyChecked(boolean isChecked);
        public void onCheckBoxClick();
    }


    public MyCursorCheckboxAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        cursorInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        checkedArray = new boolean[c.getCount()];
        checkCount = 0;
        isCheckMode = false;
        try {
            checker = ((Checker) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity Must Implement Checker");
        }
        checker.isAnyChecked(false);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return cursorInflater.inflate(R.layout.row_layout_checkbox, parent, false);
    }

    @Override
    public void bindView(View v, Context context, Cursor cursor) {

        TextView title = (TextView) v.findViewById(R.id.listTitle);
        title.setText(cursor.getString(1));
        TextView author = (TextView) v.findViewById(R.id.listAuthor);
        author.setText(cursor.getString(2));
        TextView copies = (TextView) v.findViewById(R.id.listCopies);
        copies.setText(cursor.getString(4));
        final int position = cursor.getPosition();

        final CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);

        if (isCheckMode) {
            checkBox.setVisibility(View.VISIBLE);
            Log.d("BV:"+title.getText(),"visible");
        } else {
            checkBox.setVisibility(View.GONE);
            Log.d("BV:"+title.getText(),"gone");
        }

        if (!isCheckMode) {
            checkBox.setChecked(false);
            Log.d("BV:"+title.getText(),"unChecked");
        }
        else {
            checkBox.setChecked(checkedArray[position]);
        }


        if (cursor.getInt(4) == 0) {
            checkBox.setEnabled(false);
            Log.d("BV:"+title.getText(),"Disabled");
        }
        else{
            checkBox.setEnabled(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();
                if (isChecked) {
                    checkCount++;
                    if (checkCount == 1)
                        checker.isAnyChecked(true);
                    checkedArray[position] = true;

                } else {
                    checkCount--;
                    if (checkCount == 0) {
                        checker.isAnyChecked(false);
                        checkedArray[position] = false;
                    }
                }
                checker.onCheckBoxClick();
            }
        });
        Log.d("BV:","    ");



    }
}
