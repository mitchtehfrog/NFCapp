package edu.elon.cs.nfcbulletin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NdefMessage;
import android.nfc.NfcManager;
import android.nfc.Tag;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ScanNFCActivity extends Activity {

    private NfcAdapter nfcAdapter;
    private Context context;
    private ArrayList<String> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_nfc);
        ImageView img= (ImageView) findViewById(R.id.nfcAppImage);
        img.setImageResource(R.drawable.nfcapp);
        context = getApplicationContext();
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);

        if (!isNfcOn()) {
            Toast.makeText(this, "NFC is off, Please enable NFC", Toast.LENGTH_LONG)
                    .show();
        }
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            //FIXME add a processIntent method with proper handling of text
           // processIntent(getIntent());
        }
    }

    @Override
    public void onResume() {
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


    public boolean isNfcOn() {
        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        nfcAdapter = manager.getDefaultAdapter();
        if (!nfcAdapter.isEnabled()) {
           // Toast.makeText(this, "NFC is off, Please enable NFC", Toast.LENGTH_LONG)
                   // .show();
            //finish();
            return false;
        }
        return true;
    }
    @Override
    protected void onNewIntent(Intent intent){
        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)){


            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if(parcelables != null) {
                messages = parseStringArrayListFromNdefMessage((NdefMessage) parcelables[0]);
            }
            if(parcelables != null && parcelables.length > 0 && !messages.get(0).equals("")){

                    Toast.makeText(this, "found some notes!", Toast.LENGTH_SHORT).show();

                Intent newIntent = new Intent(this, ViewTagContentActivity.class);
                newIntent.putStringArrayListExtra("messages", messages);
                newIntent.putExtra("notes-int", 1);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                newIntent.putExtra("tag", tag);
                startActivity(newIntent);
            }else{
                Toast.makeText(this, "No notes found!", Toast.LENGTH_SHORT).show();
                ArrayList<String> blankArrayList = new ArrayList<>();
                blankArrayList.add("");
                Intent noNotesIntent = new Intent(this, ViewTagContentActivity.class);
                noNotesIntent.putStringArrayListExtra("no-notes", blankArrayList);
                noNotesIntent.putExtra("notes-int", 0);
                startActivity(noNotesIntent);
            }

        }

    }

//    private void readTextFromMessage(NdefMessage ndefMessage){
//
//        NdefRecord[] ndefRecords = ndefMessage.getRecords();
//
//        if(ndefRecords != null && ndefRecords.length > 0){
//
//            //FIXME this is the code which should incrementally go through the records,
//            //FIXME so make sure that you use a relevantPosition and put them into the ListView
//            NdefRecord ndefRecord = ndefRecords[0];
//
//            String tagContent = getTextFromNdefRecord(ndefRecord);
//        }else
//        {
//            Toast.makeText(this, "No notes found!",Toast.LENGTH_SHORT).show();
//        }
//    }
    //FIXME possibly put all these methods which read text into the Message class
    //FIXME not even sure if I need these
//    public String getTextFromNdefRecord(NdefRecord ndefRecord){
//        String tagContent = null;
//        try{
//            byte[] payload = ndefRecord.getPayload();
//            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-8";
//            int languageSize = payload[0] & 0063;
//            tagContent = new String(payload, languageSize + 1,
//                    payload.length - languageSize - 1, textEncoding);
//
//        }catch (UnsupportedEncodingException e){
//            Log.e("getTextFromNdefRecord", e.getMessage(), e);
//        }
//        return tagContent;
//    }


    //FIXME was only made static for addNoteClick in ViewTagContentActivity
    public static ArrayList<String> parseStringArrayListFromNdefMessage(NdefMessage ndefMessage){
        String tagContent;
        ArrayList<String> messageContent = new ArrayList<>();

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        try {
            for (NdefRecord ndefRecord : ndefRecords) {
                    byte[] payload = ndefRecord.getPayload();
                    //  for (int i = 0; i < payload.length; i++) {
                    String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-8";
                    int languageSize = payload[0] & 0063;
                    tagContent = new String(payload, languageSize + 1, payload.length - languageSize - 1,
                            textEncoding);
                    messageContent.add(tagContent);


              // }
            }
        }catch (UnsupportedEncodingException e){
                Log.e("getTextFromNdefRecord", e.getMessage(), e);
            }
            return messageContent;
    }



    //FIXME add a method to format the tag if it has been previously erased, right now it returns an error

    //FIXME processIntent method (possibly) goes here, refer to downloaded pdf on desktop

}
