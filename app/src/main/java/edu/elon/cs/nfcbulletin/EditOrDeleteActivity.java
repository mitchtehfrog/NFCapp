package edu.elon.cs.nfcbulletin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class EditOrDeleteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_delete);
        Intent intent = getIntent();
    }

    public void onEditClick(View view){
        Intent intent = new Intent(this, EnterTextActivity.class);
        startActivity(intent);
    }

    public void onDeleteClick(View view){
        ViewTagContentActivity.messageArrayList.remove(ViewTagContentActivity.relevantPosition);

        //FIXME start an activity to finish deletion of tag data

        finish();
    }

}
