package com.example.salun.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    // references to layout components on the screen
    private ListView chatWindow;
    private EditText messageBox;
    private Button sendMessageButton;
    private ArrayList<String> messages;
    private boolean isTablet;
    private FrameLayout extraFrame;
    private static final int REQUEST_CODE = 1;
    private ChatAdapter chatAdapter;
    protected MessageFragment mFragment;
    Bundle fBundle; // bundle for sending to fragment;

    // references to database and database helper
    private SQLiteDatabase db;
    private ChatDatabaseHelper dbHelper;

    private Cursor c;

    // activity name
    private static final String ACTIVITY_NAME = "ChatWindow";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // initialize values for the layout components on the screen
        chatWindow = (ListView)findViewById(R.id.chatWindow);
        messageBox = (EditText)findViewById(R.id.textMessageBox);
        sendMessageButton = (Button)findViewById(R.id.sendTextMessageButton);
        messages = new ArrayList<>();
        extraFrame = findViewById(R.id.chatWindowFrame);
        fBundle = new Bundle();

        // check if you are on a phone or tablet (i.e. if the chatWindowFrame is loaded and not null)
        if (extraFrame == null) {
            isTablet = false; // must be using a phone
        }
        else {
            isTablet = true; // must be using a tablet
        }

        // set up the adapter for the ListView
        chatAdapter = new ChatAdapter(this);
        chatWindow.setAdapter(chatAdapter);

        // get a handle to the database
        dbHelper = new ChatDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // load messages from the database
        c = loadMessagesToCursor();
        updateMessagesList(c);

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // put the new text message in "messages" ArrayList
                String newTextMessage = messageBox.getText().toString();
                messages.add(newTextMessage);

                // insert the message into the Messages.db database
                ContentValues cValues = new ContentValues();
                cValues.put(ChatDatabaseHelper.KEY_MESSAGE, newTextMessage);
                db.insert(ChatDatabaseHelper.TABLE_NAME, null, cValues);

                // update the display and clear text box
                chatAdapter.notifyDataSetChanged();
                messageBox.setText("");
            }
        });

        chatWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // create the new MessageFragment
                mFragment = new MessageFragment();

                fBundle = new Bundle(); // create new Bundle to hold message details
                fBundle.putLong(ChatDatabaseHelper.KEY_ID, l); // add details to bundle
                fBundle.putString(ChatDatabaseHelper.KEY_MESSAGE, c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                fBundle.putBoolean("isTablet", isTablet);

                // if on a tablet
                if (isTablet) {

                    mFragment.setArguments(fBundle); // attach Bundle to the fragment as arguments

                    // add the fragment to the frame in this activity
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.chatWindowFrame, mFragment);
                    getFragmentManager().popBackStack(); // pops last fragment off the stack
                    ft.addToBackStack(null); // if user presses "back", the fragment disappears
                    ft.commit();
                }

                // if on a phone, start a new activity and send the Bundle to that activity
                else {
                    Intent startMessageDetails = new Intent(getApplicationContext(), MessageDetails.class);
                    startMessageDetails.putExtras(fBundle);
                    startActivityForResult(startMessageDetails, REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == MessageFragment.RESULT_CODE) {
            deleteMessage(data.getExtras().getLong(ChatDatabaseHelper.KEY_ID));
        }
    }

    /*
    Deletes a message with a particular ID, then makes a call to the db to update the arraylist
     */
    public void deleteMessage(long id) {
        db.delete(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.KEY_ID + " = " + (int)id,null);
        c = loadMessagesToCursor();
        updateMessagesList(c);
        chatAdapter.notifyDataSetChanged();
    }

    /*
    SELECT _id, message FROM MESSAGES
     */
    public Cursor loadMessagesToCursor() {
        return db.query(false, ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},null,null,null,null,null,null);
    }

    /*
    Refreshes the messages ArrayList from a cursor
     */
    public void updateMessagesList(Cursor c) {
        messages.clear();
        Log.i(ACTIVITY_NAME, "Cursor's column count: " + c.getColumnCount());
        for (int i = 0; i < c.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, String.format("Column %d: %s ", i, c.getColumnName(i)));
        }

        // for each row returned by query, populate the message ArrayList
        c.moveToFirst();
        while (!c.isAfterLast()) {
            Log.i(ACTIVITY_NAME, "SQL Message: "+ c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE))); // show message in log
            messages.add(c.getString(c.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE))); // add message to messages ArrayList
            c.moveToNext();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (dbHelper != null) {
            dbHelper.close();
        }

        Log.i(ACTIVITY_NAME, "onDestroy() was called to close db and dbHelper");
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        LayoutInflater inflater;

        public ChatAdapter(Context c) {
            super(c, 0);
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public long getItemId(int position) {
            c = loadMessagesToCursor();
            c.moveToPosition(position);
            return c.getInt(c.getColumnIndex(ChatDatabaseHelper.KEY_ID));
        }

        @Override
        public String getItem(int position) {
            return messages.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent ) {
            inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if (position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing,null);

            TextView message = (TextView) result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }
}