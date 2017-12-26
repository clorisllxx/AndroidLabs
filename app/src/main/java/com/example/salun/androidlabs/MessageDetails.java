package com.example.salun.androidlabs;


import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class MessageDetails extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        // create the fragment for this activity
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        MessageFragment mf = new MessageFragment();
        ft.add(R.id.messageDetailsActivity, mf);

        // send the Bundle from the ChatWindow to the Fragment
        mf.setArguments(getIntent().getExtras());
        ft.commit();

    }
}
