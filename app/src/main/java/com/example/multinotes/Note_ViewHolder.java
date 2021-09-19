package com.example.multinotes;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class Note_ViewHolder extends RecyclerView.ViewHolder {

    TextView NoteTitleView;
    TextView NoteDescriptionView;
    TextView NoteDateTimeView;

    //Will be created for each Note in the NotesList for Display in the MainActivity using the note_list_row_template.xml
    public Note_ViewHolder(View itemView) {
        super(itemView);

        NoteTitleView = itemView.findViewById(R.id.NoteTitleTemplate);
        NoteDescriptionView = itemView.findViewById(R.id.NoteDescriptionTemplate);
        NoteDateTimeView = itemView.findViewById(R.id.NoteDateTimeTemplate);
    }
}
