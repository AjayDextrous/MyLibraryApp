package com.example.ajay.library_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class NewBookScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Add New Book");
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        EditText nos = (EditText) findViewById(R.id.editNos);
        nos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_DONE) {

                    submitBook(textView.getRootView());
                    handled = true;
                }

                return handled;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_empty, menu);
        return true;
    }

    public void submitBook(View view) {
        TextView titleM = (TextView) findViewById(R.id.titleMandatory);
        TextView authorM = (TextView) findViewById(R.id.authorMandatory);
        TextView publisherM = (TextView) findViewById(R.id.publisherMandatory);
        TextView nosM = (TextView) findViewById(R.id.copiesMandatory);

        EditText Title = (EditText) findViewById(R.id.editTitle);
        EditText Author = (EditText) findViewById(R.id.editAuth);
        EditText Publisher = (EditText) findViewById(R.id.editPub);
        EditText nos = (EditText) findViewById(R.id.editNos);
        if (Title.getText().toString().isEmpty() || Author.getText().toString().isEmpty() || Publisher.getText().toString().isEmpty() || nos.getText().toString().isEmpty()) {
            if (Title.getText().toString().isEmpty())
                titleM.setVisibility(View.VISIBLE);
            else
                titleM.setVisibility(View.INVISIBLE);

            if (Author.getText().toString().isEmpty())
                authorM.setVisibility(View.VISIBLE);
            else
                authorM.setVisibility(View.INVISIBLE);

            if (Publisher.getText().toString().isEmpty())
                publisherM.setVisibility(View.VISIBLE);
            else
                publisherM.setVisibility(View.INVISIBLE);

            if (nos.getText().toString().isEmpty())
                nosM.setVisibility(View.VISIBLE);
            else
                nosM.setVisibility(View.INVISIBLE);
        } else {
            libraryBook book = new libraryBook(0, Title.getText().toString(), Author.getText().toString(), Publisher.getText().toString(),
                    Integer.parseInt(nos.getText().toString()));
            DBHandler db = new DBHandler(this);
            db.addBook(book);
            Intent intent = new Intent(this, AdminLibraryActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminLibraryActivity.class);
        startActivity(intent);
        finish();
    }
}
