package com.example.barea.notelist;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.barea.notelist.Model.Note;
import com.example.barea.notelist.Utils.PrefUtils;
import com.example.barea.notelist.view.NoteAdapter;
import com.example.barea.notelist.view.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private List<Note> notes = new ArrayList<>();
    private NoteAdapter noteAdapter;
    ManageNote  manageNote;


    @BindView(R.id.coordinator_layout) CoordinatorLayout coordinatorLayout;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        manageNote = new ManageNote(getApplicationContext());
        noteAdapter = new NoteAdapter(this,notes);
        RecyclerView.LayoutManager linerLyou = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linerLyou);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(noteAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new RecyclerTouchListener.onClickListener() {
            @Override
            public void onClick(View v, int position) {

            }

            @Override
            public void onLongClick(View v, int position) {
                Note note = notes.get(position);
                Integer id = note.getId();
                Intent i = new Intent(MainActivity.this, DesplyTask.class);
                i.putExtra("noteId",String.valueOf(id));
                i.putExtra("Note", note.getNote());
                MainActivity.this.startActivity(i);
            }
        }));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, DesplyTask.class);
                MainActivity.this.startActivity(i);
            }
        });

        if(TextUtils.isEmpty(PrefUtils.getApiKey(this))){
            manageNote.CreateUser(getApplicationContext());
        }
        else {
            load();
        }
        }

    @Override
    public void onResume(){
        super.onResume();
        load();
    }

        public void load(){
            manageNote.compositeDisposable.add(
                   manageNote.apiService.fetchAllNotes()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .map(new Function<List<Note>, List<Note>>() {
                                @Override
                                public List<Note> apply(List<Note> lnotes) throws Exception {
                                    // TODO - note about sort
                                    Collections.sort(lnotes, new Comparator<Note>() {
                                        @Override
                                        public int compare(Note n1, Note n2) {
                                            return n2.getId() - n1.getId();
                                        }
                                    });
                                    return lnotes;
                                }
                            })
                            .subscribeWith(new DisposableSingleObserver<List<Note>>() {
                                @Override
                                public void onSuccess(List<Note> lnotes) {

                                    notes.clear();
                                    notes.addAll(lnotes);
                                    noteAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(),
                                            e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }));
        }
    }



