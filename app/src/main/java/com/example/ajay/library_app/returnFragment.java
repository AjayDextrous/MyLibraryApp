package com.example.ajay.library_app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ajay-5674 on 13/07/17.
 */

public class returnFragment extends ListFragment {
    int mNum;
    DBHandler db;
    UserDBHandler udb;
    Cursor cursor;
    libraryBook book;
    int bookId;
    String user;
    int ret_nos;


    static returnFragment newInstance(int num,String u){
        returnFragment f = new returnFragment();
        Bundle args = new Bundle();
        args.putInt("num",num);
        args.putString("user",u);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mNum = getArguments()!=null? getArguments().getInt("num"):1;
        user = getArguments().getString("user");
        udb = new UserDBHandler(getContext());
        db = new DBHandler(getContext());
        cursor = udb.getCursor(user);
        ret_nos = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        if(udb.getBookCount(user)>=mNum+1)
            cursor.moveToPosition(mNum);
        else
            cursor.moveToPosition(mNum-1);

        book = udb.getBook(cursor.getInt(0),user);

        final View v = inflater.inflate(R.layout.fragment_return,container,false);

        TextView title = (TextView) v.findViewById(R.id.textTitle);
        title.setText(cursor.getString(1));
        TextView author = (TextView) v.findViewById(R.id.textAuth);
        author.setText(cursor.getString(2));
        TextView publisher = (TextView) v.findViewById(R.id.textPub);
        publisher.setText(cursor.getString(3));
        TextView copies = (TextView)v.findViewById(R.id.textNos);
        copies.setText("Copies : "+cursor.getString(4));
        final TextView returnnos = (TextView) v.findViewById(R.id.returnnos);
        returnnos.setText(Integer.toString(ret_nos));

        Button addButton = (Button) v.findViewById(R.id.buttonAdd);
        Button subButton = (Button) v.findViewById(R.id.buttonSub);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ret_nos<book.getNos()){
                    ret_nos++;
                    returnnos.setText(Integer.toString(ret_nos));
                }
                else{
                    Toast.makeText(getContext(),"You have no more copies to return!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ret_nos>1){
                    ret_nos--;
                    returnnos.setText(Integer.toString(ret_nos));
                }
                else{
                    Toast.makeText(getContext(),"you cant return less than 1 book!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button returnButton = (Button) v.findViewById(R.id.buttonReturn);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //book.setNos(ret_nos);
                libraryBook dbook = new libraryBook(book);
                if(db.getBook(book.getId())==null){
                    dbook.setNos(ret_nos);
                    db.addBook(dbook);
                }
                else {
                    dbook = db.getBook(book.getId());
                    dbook.setNos(dbook.getNos()+ret_nos);
                    db.updateBook(dbook);
                }
                book.setNos(book.getNos()-ret_nos);
                if(book.getNos()==0){
                    udb.deleteBook(book.getId(),user);
                }
                else{
                    udb.updateBook(book,user);
                }

                if(udb.getBookCount(user)==0){
                    Intent intent = new Intent(getContext(),UserLibraryActivity.class);
                    intent.putExtra("USER_NAME",user);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getContext(),swipeReturnActivity.class);
                    intent.putExtra("USER_NAME",user);
                    intent.putExtra("number",mNum);
                    startActivity(intent);
                }

            }
        });

        return v;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        cursor.close();
        db.close();
        udb.close();
    }

    public libraryBook getBook(){
        return book;
    }
}
