package com.example.salun.androidlabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class TestToolbar extends AppCompatActivity {

    FloatingActionButton fab;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);
        Toolbar toolbar = findViewById(R.id.testToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.testToolBarText);
        fab = findViewById(R.id.fab);
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(findViewById(R.id.snackbarLayout), R.string.floatActionButtonClickedText, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {
//        return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case (R.id.action1):
                String toastMessage = prefs.getString("ToastMessage","Option 1 Clicked");
                Snackbar.make(findViewById(R.id.snackbarLayout), toastMessage, Snackbar.LENGTH_SHORT).show();
                Log.d("Toolbar", "Option 1 selected");
                break;

            case (R.id.action2):
                AlertDialog.Builder action2Builder = new AlertDialog.Builder(this);
                action2Builder.setTitle(R.string.doYouWantToGoBackText);
                action2Builder.setPositiveButton(R.string.yesText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish(); // close activity
                    }
                });
                action2Builder.setNegativeButton(R.string.noText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
                AlertDialog dialog = action2Builder.create();
                dialog.show();

                Log.d("Toolbar", "Option 2 selected");
                break;

            case (R.id.action3):
                AlertDialog.Builder action3Builder = new AlertDialog.Builder(this);
                View view = getLayoutInflater().inflate(R.layout.snackbar_dialog,null);
                final EditText newToast = view.findViewById(R.id.newToastMessageEditText);
                action3Builder.setTitle(R.string.doYouWantToGoBackText)
                        .setView(view)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String newMessage = newToast.getText().toString();
                                SharedPreferences.Editor edit = prefs.edit();
                                edit.putString("ToastMessage",newMessage);
                                edit.commit();

                            }
                        });
                action3Builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
                AlertDialog action3Dialog = action3Builder.create();
                action3Dialog.show();
                Log.d("Toolbar", "Option 3 selected");
                break;

            case (R.id.about):
                Toast.makeText(getApplicationContext(),R.string.versionByQiulinText, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}