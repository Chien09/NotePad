package com.example.multinotes;

public class Note {
    private String NoteTitle;
    private String NoteDescription;
    private String NoteDateTime;

    //Constructor
    Note(String NoteTitle, String NoteDescription, String NoteDateTime){
        this.NoteTitle = NoteTitle;
        this.NoteDescription = NoteDescription;
        this.NoteDateTime = NoteDateTime;
    }

    public String getNoteTitle(){
        return this.NoteTitle;
    }

    public String getNoteDescription(){
        return this.NoteDescription;
    }

    public String getNoteDateTime(){
        return this.NoteDateTime;
    }

}
