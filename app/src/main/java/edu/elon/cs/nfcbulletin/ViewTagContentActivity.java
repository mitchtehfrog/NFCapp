package edu.elon.cs.nfcbulletin;

import android.app.Activity;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ViewTagContentActivity extends Activity {

    private ListView listView;
    protected static List<Message> messageArrayList;
    protected static int relevantPosition;
    protected static ArrayList<String> stringArrayList;
    //FIXME was only made a data member for addNoteClick
    private Intent intent;
    private int space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tag_content);

       listView = (ListView) findViewById(R.id.messageList);


        //messageArrayList = new ArrayList<Message>();

       // ArrayAdapter<Message> arrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.
        //simple_list_item_1, messageArrayList);

        intent = getIntent();
        if(intent.getIntExtra("notes-int", 0) == 1){
            stringArrayList = intent.getStringArrayListExtra("messages");
        }else{
            stringArrayList = intent.getStringArrayListExtra("no-notes");
        }

        try {
            Tag tag = intent.getParcelableExtra("tag");
            if(tag != null) {
                Ndef ndef = Ndef.get(tag);
                ndef.connect();
                NdefMessage ndefMessage = ndef.getNdefMessage();
                ndef.close();
                double spaceTotal = ndef.getMaxSize();
                double spaceTaken = ndefMessage.toByteArray().length;
                System.out.println(spaceTaken);
                System.out.println(spaceTotal);
                space = (int) ((spaceTaken / spaceTotal) * 100);
            }

        }catch (FormatException e){
            throw new RuntimeException(e);
        }catch (IOException eo){
            throw new RuntimeException(eo);
        }
        Toast.makeText(this, "Tag is " + "" + space + "%" + " full",Toast.LENGTH_LONG).show();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_list_item_1, stringArrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(itemLongClickListener);

    }

//    public void addNoteClick(View view){
//        Tag tag = intent.getParcelableExtra("tag");
//        Ndef ndefTag = Ndef.get(tag);
//        NdefMessage ndefMessage = ndefTag.getCachedNdefMessage();
//        ArrayList<String> arrayList = ScanNFCActivity.parseStringArrayListFromNdefMessage(ndefMessage);
//        Intent newIntent = new Intent(this, EnterTextActivity.class);
//        startActivity(intent);
//
//    }

//    public void computeSpace(Tag tag) throws IOException, FormatException{
//    try {
//        Tag tag = intent.getParcelableExtra("tag");
//        Ndef ndef = Ndef.get(tag);
//        NdefMessage ndefMessage = ndef.getNdefMessage();
//        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        double spaceTaken = ndef.getMaxSize();
//        double spaceTotal = ndefMessage.toByteArray().length;
//        int progressBarSeek = (int) (spaceTaken / spaceTotal);
//        progressBar.setProgress(progressBarSeek);
//    }catch (Exception e){
//        e.printStackTrace();
//    }
//    }

    //FIXME this method doesn't work.
//    private NdefMessage createNdefMessage(String content){
//        NdefRecord ndefRecord = NdefRecord.createTextRecord("d", content);
//        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[](ndefRecord));
//    }



    public void onAddNoteClick(View view){
        Intent intent = new Intent(this, EnterTextActivity.class);
        //FIXME add some code to NOT put the "No notes found!" message
       // if(stringArrayList.get(0).equals("No notes found!")) {
           // stringArrayList = new ArrayList<>();
         //   stringArrayList.add("");
      //  }
        intent.putStringArrayListExtra("messages", stringArrayList);
        startActivity(intent);
    }

    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            relevantPosition = position;
            Intent intent = new Intent(getApplicationContext(), EditOrDeleteActivity.class);
            startActivity(intent);
            return true;
        }
    };

    //FIXME scanning a tag should default to this activity with ListView, etc. Hitting the "pencil"
    //FIXME button on the bottom right goes to EnterTextActivity and sends results back to the data
    //FIXME member in this one
}
