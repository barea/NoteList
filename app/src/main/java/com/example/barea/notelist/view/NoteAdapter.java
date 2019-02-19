package com.example.barea.notelist.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.barea.notelist.Model.Note;
import com.example.barea.notelist.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    Context context;
    List<Note> notes;

    public class NoteViewHolder extends RecyclerView.ViewHolder{


        @BindView(R.id.note_descrption) TextView descrption;

        public NoteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public NoteAdapter(Context context, List<Note> notes){
        this.context = context;
        this.notes = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder noteViewHolder, int i) {
        Note note = notes.get(i);
        noteViewHolder.descrption.setText(note.getNote());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


}
