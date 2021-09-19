package com.example.multinotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Note_Adapter extends RecyclerView.Adapter<Note_ViewHolder> {

    private ArrayList<Note> notesList;
    private MainActivity mainAct;

    //Constructor
    Note_Adapter(ArrayList<Note> notesList, MainActivity mainAct){
        this.notesList = notesList;
        this.mainAct = mainAct;
    }


    //This will create each Note's ViewHolder depending on how many Notes object
    @NonNull
    @Override
    public Note_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Inflate layout to Recycler View
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_list_row_template, parent, false);

        //Setting the Long press and tap for items in the Recycler View
        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new Note_ViewHolder(itemView);
    }

    //This method is to bind the Note objects to the ViewHolder to set the Text Views
    @Override
    public void onBindViewHolder(@NonNull Note_ViewHolder holder, int position) {
        Note note = notesList.get(position);

        //Set Note Title
        holder.NoteTitleView.setText(note.getNoteTitle());

        //Set Note Description
        String tmpDescription = note.getNoteDescription();
        if(tmpDescription.length() > 80){ //text greater than 80
            tmpDescription = tmpDescription.substring(0,81) + "..."; //81 - 1
        }
        holder.NoteDescriptionView.setText(tmpDescription);

        //Set Note Date
        holder.NoteDateTimeView.setText(note.getNoteDateTime());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
