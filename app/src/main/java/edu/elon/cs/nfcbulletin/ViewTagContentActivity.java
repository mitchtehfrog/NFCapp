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

import java.util.ArrayList;
import java.util.List;

public class ViewTagContentActivity extends Activity {

    private ListView listView;
    protected static List<Message> messageArrayList;
    protected static int relevantPosition;
    protected static ArrayList<String> stringArrayList;
    //FIXME was only made a data member for addNoteClick
    private Intent intent;

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

    //FIXME this method doesn't work.
//    private NdefMessage createNdefMessage(String content){
//        NdefRecord ndefRecord = NdefRecord.createTextRecord("d", content);
//        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[](ndefRecord));
//    }



    public void onAddNoteClick(View view){
        Intent intent = new Intent(this, EnterTextActivity.class);
        //FIXME add some code to NOT put the "No notes found!" message
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
