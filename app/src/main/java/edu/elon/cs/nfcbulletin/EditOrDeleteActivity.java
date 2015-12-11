package edu.elon.cs.nfcbulletin;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;

public class EditOrDeleteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_or_delete);
        Intent intent = getIntent();
    }

    public void onEditClick(View view){
        int relevantPosition = ViewTagContentActivity.relevantPosition;
        String s = ViewTagContentActivity.stringArrayList.get(relevantPosition);
        Intent intent = new Intent(this, EnterTextActivity.class);
        intent.putExtra("int-edit", 1);
        intent.putExtra("relevantPosition", relevantPosition);
        intent.putExtra("editmessage", s);
        intent.putStringArrayListExtra("messages", ViewTagContentActivity.stringArrayList);
        finish();
        startActivity(intent);
    }

    public void onDeleteClick(View view){
        ViewTagContentActivity.stringArrayList.remove(ViewTagContentActivity.relevantPosition);
        ArrayList<String> strings = ViewTagContentActivity.stringArrayList;
        Intent intent = new Intent(this, WriteNoteActivity.class);
        NdefRecord[] ndefRecords = new NdefRecord[strings.size()];
       if(strings.size() > 0){
           for(int i = 0; i < strings.size(); i++){
               NdefRecord ndefRecord = NdefRecord.createTextRecord(null, strings.get(i));
               ndefRecords[i] = ndefRecord;
           }
       }else{
           ndefRecords = new NdefRecord[1];
           ndefRecords[0] = NdefRecord.createTextRecord(null, "");

       }
        NdefMessage ndefMessage = new NdefMessage(ndefRecords);
        intent.putExtra("message", ndefMessage);
        finish();
        startActivity(intent);


        //FIXME start an activity to finish deletion of tag data

        finish();
    }

}
