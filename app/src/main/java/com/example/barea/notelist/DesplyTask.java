package com.example.barea.notelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DesplyTask extends AppCompatActivity {

    @BindView(R.id.add_or_edit)
    TextView addOredit;

    @BindView(R.id.content)
    EditText content;

    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproject);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        taskId = intent.getStringExtra("noteId");
        content.setText(intent.getStringExtra("Note"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_desply_task, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (taskId == null) {
            addOredit.setText("Add Task");
            MenuItem Update = menu.findItem(R.id.action_update);
            MenuItem Delete = menu.findItem(R.id.action_delete_task);
            Delete.setVisible(false);
            Update.setVisible(false);
        }
        else
        {
            addOredit.setText("Edit Task");
            MenuItem Cancle = menu.findItem(R.id.action_cancle);
            MenuItem Save = menu.findItem(R.id.action_save_task);
            Cancle.setVisible(false);
            Save.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public  boolean  onOptionsItemSelected(MenuItem item) {
        ManageNote m = new ManageNote(getApplicationContext());
        switch(item.getItemId()){

            case android.R.id.home:
                DesplyTask.this.finish();

            case R.id.action_cancle:
                DesplyTask.this.finish();

            case R.id.action_save_task:
                String note =String.valueOf(content.getText());
                if (note.matches("")){

                    DesplyTask.this.finish();
                }
                else {
                    m.CreateNote(String.valueOf(content.getText()));
                    DesplyTask.this.finish();
                }

            case R.id.action_delete_task:
                if(taskId != null) {
                    m.DeleteNote(Integer.valueOf(taskId));
                    DesplyTask.this.finish();
                }

            case R.id.action_update:
                if (taskId != null) {
                    m.UpdateNote(Integer.valueOf(taskId), String.valueOf(content.getText()));
                    DesplyTask.this.finish();
                }
        }
        return true;

    }
}

