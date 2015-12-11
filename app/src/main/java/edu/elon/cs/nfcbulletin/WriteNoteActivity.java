package edu.elon.cs.nfcbulletin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class WriteNoteActivity extends Activity {

    private Tag tag;
    NdefMessage newNdefMessage;
    private NfcAdapter nfcAdapter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        Intent intent = getIntent();
        newNdefMessage = intent.getParcelableExtra("message");


    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage){

        try{
            if(tag == null){
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_SHORT).show();
            }
            Ndef ndef = Ndef.get(tag);

            if(ndef == null){
                formatTag(tag, ndefMessage);
            }
            else{
                ndef.connect();
                if(!ndef.isWritable()){
                    Toast.makeText(this, "Tag is not writable", Toast.LENGTH_SHORT).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();
            }
        }catch (Exception e){
            if(e.getMessage() != null) {
                Log.e("writeNdefMessage", e.getMessage());
            }
        }

    }

    private void formatTag(Tag tag, NdefMessage ndefMessage){
        try {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not formattable", Toast.LENGTH_SHORT).show();
            }
                ndefFormatable.connect();
                ndefFormatable.format(ndefMessage);
                ndefFormatable.close();
                Toast.makeText(this, "Tag formatted", Toast.LENGTH_SHORT).show();
            }
        catch (Exception e){
            Log.e("formatTag", e.getMessage());

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        PendingIntent intent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this,intent,
                null, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (NfcAdapter.getDefaultAdapter(this) != null)
            NfcAdapter.getDefaultAdapter(this).disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);


            //if(parcelables != null) {
              //  NdefMessage ndefMessage = (NdefMessage) parcelables[0];
               //formatTag(tag, ndefMessage);
            //}

            int size = newNdefMessage.toByteArray().length;
            Ndef ndef = Ndef.get(tag);
            if(ndef.getMaxSize() < size){
                Toast.makeText(this, "not enough space to write to tag",Toast.LENGTH_SHORT).show();
                return;
            }
            writeNdefMessage(tag, newNdefMessage);

            Toast.makeText(this, "Tag written!", Toast.LENGTH_LONG).show();

            Intent newIntent = new Intent(this, ViewTagContentActivity.class);
            ArrayList<String> stringArrayList = ScanNFCActivity.parseStringArrayListFromNdefMessage(newNdefMessage);
            if(stringArrayList.get(0).equals("") && stringArrayList.size() == 2){
                stringArrayList.remove(0);
            }
            newIntent.putStringArrayListExtra("messages", stringArrayList);
            newIntent.putExtra("notes-int", 1);
            newIntent.putExtra("tag", tag);
            finish();
            startActivity(newIntent);



//            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            NdefMessage ndefMessage = (NdefMessage) parcelables[0];
//            if (parcelables != null && parcelables.length > 0) {
//                Toast.makeText(this, "found some notes!", Toast.LENGTH_SHORT).show();
//                ArrayList<String> messages = parseStringArrayListFromNdefMessage((NdefMessage) parcelables[0]);
//                Intent newIntent = new Intent(this, ViewTagContentActivity.class);
//                newIntent.putStringArrayListExtra("messages", messages);
//                newIntent.putExtra("notes-int", 1);
//                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//                newIntent.putExtra("tag", tag);
//                startActivity(newIntent);
//            } else {
//                Toast.makeText(this, "No notes found!", Toast.LENGTH_SHORT).show();
//                ArrayList<String> blankArrayList = new ArrayList<>();
//                blankArrayList.add("No notes found!");
//                Intent noNotesIntent = new Intent(this, ViewTagContentActivity.class);
//                noNotesIntent.putStringArrayListExtra("no-notes", blankArrayList);
//                noNotesIntent.putExtra("notes-int", 0);
//                startActivity(noNotesIntent);
//            }

        }
    }
}
