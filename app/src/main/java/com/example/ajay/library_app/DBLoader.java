package com.example.ajay.library_app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

/**
 * Created by ajay-5674 on 18/07/17.
 */

public class DBLoader extends AsyncTaskLoader<Cursor> {

    private Cursor mCursor;
    Context context;
    public DBLoader(Context context) {
        super(context);
        this.context = context;
        //loadInBackground();
    }

    @Override
    public Cursor loadInBackground() {
        Log.d("DBLoader "," loadInBackground");
        DBHandler db = new DBHandler(context);
        Cursor cursor = db.getCursor();
        return cursor;
    }

    @Override
    public void deliverResult(Cursor cursor){
        Log.d("DBLoader "," deliverResult");
        if(isReset()){
            releaseResources(cursor);
            return;
        }

        Cursor oldCursor = mCursor;
        mCursor = cursor;

        if(isStarted()){
            super.deliverResult(cursor);
        }

        if(oldCursor!=null && oldCursor!=cursor){
            releaseResources(oldCursor);
        }
    }
    @Override
    public void onStartLoading(){
        if(mCursor != null)
            deliverResult(mCursor);
        if(takeContentChanged()||mCursor==null)
            forceLoad();
    }

    
    private void releaseResources(Cursor cursor) {
        cursor.close();
    }
}
