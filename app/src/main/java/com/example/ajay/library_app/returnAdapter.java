package com.example.ajay.library_app;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by ajay-5674 on 13/07/17.
 */

public class returnAdapter extends FragmentStatePagerAdapter {
    Context context;
    int num_items;
    String user;
    public returnAdapter(FragmentManager fm, Context c) {
        super(fm);
        context = c;




    }

    @Override
    public Fragment getItem(int position) {
        return returnFragment.newInstance(position,user);
    }

    @Override
    public int getCount() {
        return num_items;
    }

    public void setUser(String u){
        user = u;
        UserDBHandler db = new UserDBHandler(context);
        num_items = db.getBookCount(user);
        db.close();
    }
}

