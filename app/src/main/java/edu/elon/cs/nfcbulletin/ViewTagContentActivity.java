package edu.elon.cs.nfcbulletin;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tag_content);

       listView = (ListView) findViewById(R.id.messageList);

        //messageArrayList = new ArrayList<Message>();

       // ArrayAdapter<Message> arrayAdapter = new ArrayAdapter<Message>(this, android.R.layout.
        //simple_list_item_1, messageArrayList);

        Intent intent = getIntent();
        stringArrayList = intent.getStringArrayListExtra("messages");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.
                simple_list_item_1, stringArrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener(itemLongClickListener);




    }

    AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            relevantPosition = position;
            System.out.println("in longclick");
            return true;
        }
    };

    //FIXME scanning a tag should default to this activity with ListView, etc. Hitting the "pencil"
    //FIXME button on the bottom right goes to EnterTextActivity and sends results back to the data
    //FIXME member in this one
}
