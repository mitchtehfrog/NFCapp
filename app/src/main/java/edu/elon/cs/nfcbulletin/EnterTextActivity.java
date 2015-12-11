package edu.elon.cs.nfcbulletin;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EnterTextActivity extends Activity {

    private ArrayList<String> stringArrayList;
    private String editMessage;
    private int relevantPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_enter_text);
        Intent intent = getIntent();
            stringArrayList = intent.getStringArrayListExtra("messages");

        if(intent.getIntExtra("int-edit", 0) == 1){
            editMessage = intent.getStringExtra("editmessage");
        }
        if(intent.hasExtra("relevantPosition")){
            relevantPosition = intent.getIntExtra("relevantPosition", -1);
        }
        EditText et = (EditText) findViewById(R.id.edittext);
        if(editMessage != null){
            et.setText(editMessage);
        }

    }


    public NdefMessage getNewNdefMessage(){
        EditText et = (EditText) findViewById(R.id.edittext);
        if(editMessage == null) {
            String newString = et.getText().toString();
            stringArrayList.add(newString);
        }
        else{
            String newString = et.getText().toString();
            stringArrayList.remove(relevantPosition);
            stringArrayList.add(newString);
        }

            NdefRecord[] ndefRecords = new NdefRecord[stringArrayList.size()];
            for (int i = 0; i < stringArrayList.size(); i++) {
                NdefRecord ndefRecord = NdefRecord.createTextRecord(null, stringArrayList.get(i));
                ndefRecords[i] = ndefRecord;
            }


        NdefMessage ndefMessage = new NdefMessage(ndefRecords);
        return ndefMessage;
    }


    public void sendWriteNote(View view){
        NdefMessage ndefMessage = getNewNdefMessage();

        Intent intent = new Intent(this, WriteNoteActivity.class);
        intent.putExtra("message", ndefMessage);
        finish();
        startActivity(intent);
    }



}
