package com.example.ajay.library_app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

/**
 * Created by ajay-5674 on 11/07/17.
 */

public class editAdapter extends FragmentStatePagerAdapter {
    private int num_items;
    private Context context;

    public editAdapter(FragmentManager fm, Context c) {
        super(fm);
        context = c;
        DBHandler db = new DBHandler(context);
        num_items = db.getBookCount();
        db.close();
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("editAdapter ", Integer.toString(position));
        return editFragment.newInstance(position);
    }

    @Override
    public int getCount() {

        return num_items;
    }
}
