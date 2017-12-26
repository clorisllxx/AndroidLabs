package com.example.salun.androidlabs;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.Locale;


public class MessageFragment extends Fragment {

    public static final int RESULT_CODE = 10;
    TextView tvMessage;
    TextView tvMessageID;
    Button deleteButton;
    Bundle bundle;
    String message;
    long messageID;
    boolean isTablet;

    public MessageFragment() {
        this.isTablet = false;
    }

    public void setIsTablet(boolean isTablet) {
        this.isTablet = isTablet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.message_details_fragment, null);

        // get handles to each element in Fragment
        tvMessage = view.findViewById(R.id.messageTextView);
        tvMessageID = view.findViewById(R.id.messageIdTextView);
        deleteButton = view.findViewById(R.id.deleteMessageButton);

        // if this is a new fragment (i.e. not one that has been re-built after rotating)
        if (savedInstanceState == null) {
            bundle = getArguments(); // get arguments from ChatWindow Activity or from the MessageDetails activity
        }
        // if this is a fragment that was re-built (i.e. after rotation)
        else {
            bundle = savedInstanceState;
        }

        message = bundle.getString(ChatDatabaseHelper.KEY_MESSAGE);
        messageID = bundle.getLong(ChatDatabaseHelper.KEY_ID);
        isTablet = bundle.getBoolean("isTablet");

        // add the data from the bundle to the textviews
        tvMessage.setText(message);
        tvMessageID.setText(String.format(Locale.CANADA,"%d", messageID));

        // attach event handler to the button
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTablet) {
                    ((ChatWindow)getActivity()).deleteMessage(bundle.getLong(ChatDatabaseHelper.KEY_ID));
                    getFragmentManager().popBackStack();
                }
                else {
                    getActivity().setResult(RESULT_CODE, getActivity().getIntent());
                    getActivity().finish();
                }

            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ChatDatabaseHelper.KEY_MESSAGE,message);
        outState.putLong(ChatDatabaseHelper.KEY_ID,messageID);
        outState.putBoolean("isTablet", isTablet);
    }
}
