package com.example.multinotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{

    //All created Notes will be stored here
    private final ArrayList<Note> NotesList = new ArrayList<Note>();

    //REQUEST Code for sending Data from MainActivity to/from EditActivity
    private static final int Edit_CODEREQUEST = 123;

    //LOG for debug on logcat
    private static final String TAG = "MainActivity";

    //Recycler & Adapter
    private RecyclerView recyclerView;
    private Note_Adapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the RecyclerView
        recyclerView = findViewById(R.id.NotesRecycler);

        //Set Adapter
        noteAdapter = new Note_Adapter(NotesList, this);

        //Set Adapter onto RecyclerView
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        readJSONFile();

        this.setTitle(String.format("Multi Notes (%o)", NotesList.size())); //setting the Menu Title
    }

    //Inflate custom menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucustom, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        writeJSONFile();
    }

    //For Menu Option Buttons
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.Info:
                //Start the activity
                Intent AboutIntent = new Intent(this, AboutActivity.class);
                startActivity(AboutIntent);
                break;
            case R.id.CreateNote:
                //Start the activity and send REQUEST_CODE for data passing back
                Intent AddIntent = new Intent(this, EditActivity.class);
                startActivityForResult(AddIntent, Edit_CODEREQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Retrieve Intent Data passed from EditActivity methods onOptionsItemSelected() & onBackPressed()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Edit_CODEREQUEST){
            if(resultCode == RESULT_OK){ //RESULT_OK = -1
                String RetrieveNoteTitle = data.getStringExtra("UserInputTitle");
                String RetrieveNoteDescription = data.getStringExtra("UserInputDescription");

                SimpleDateFormat CustomDateFormat = new SimpleDateFormat("EEE MMM d, hh:mm aaa"); //Custom Date format
                String CurrentDateTime = CustomDateFormat.format(new Date());

                if(RetrieveNoteTitle.isEmpty()){ //if NoteTitle Empty the Note will not be saved
                    Toast.makeText(this, "Note is not saved, because no TITLE", Toast.LENGTH_LONG).show();
                    return; //exit right away from this method
                }

                //Create the Note object to be saved onto the NotesList
                Note note = new Note(RetrieveNoteTitle, RetrieveNoteDescription, CurrentDateTime);

                //Indexing add so its like most recent added to the top of the list, instead of using compareTo
                NotesList.add(0, note);

                noteAdapter.notifyDataSetChanged(); //Update the data for RecyclerView to display VERY IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                this.setTitle(String.format("Multi Notes (%o)", NotesList.size())); //Update the menu title on NotesList count
            }
            else{ //Result code other than OK/-1
                Log.d(TAG, "onActivityResult: Result Code: " + resultCode);
            }
        }
        else{ //not the same requestCode sent back (means it could be data from another activity instead)
            Log.d(TAG, "onActivityResult: Request Code: " + requestCode);
        }
    }

    //Tap to edit the note, moving to EditActivity passing along data
    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildLayoutPosition(v);
        Note noteSelected = NotesList.get(position);

        //Passing Current Note data to EditActivity
        Intent EditIntent = new Intent(this, EditActivity.class);
        EditIntent.putExtra("EditNoteTitle", noteSelected.getNoteTitle());
        EditIntent.putExtra("EditNoteDescription", noteSelected.getNoteDescription());

        //Removing the Note to be Edit, which will be ReSaved by EditActivity onOptionsItemSelected or onBackPressed
        NotesList.remove(position);

        //Updating to prevent delete Note not appear if the Edit note is not saved, it will CRASH if not .notifyDataSetChanged();
        noteAdapter.notifyDataSetChanged();

        this.setTitle(String.format("Multi Notes (%o)", NotesList.size())); //Updating the Menu Title

        startActivityForResult(EditIntent, Edit_CODEREQUEST);
    }

    //Long Press
    @Override
    public boolean onLongClick(View v) {

        final int position = recyclerView.getChildLayoutPosition(v); //position of Note
        Note noteSelected = NotesList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage(String.format("Want to delete %s note? ", noteSelected.getNoteTitle()));
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                NotesList.remove(position);
                noteAdapter.notifyDataSetChanged();   //Update the data for RecyclerView to display VERY IMPORTANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

                setTitle(String.format("Multi Notes (%o)", NotesList.size())); //updating the Menu Title
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return; //Not finish(); it will exit out of the app
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }


    //Read or Load JSON file
    private void readJSONFile(){
        try {
            InputStream FIS = getApplicationContext().openFileInput("NotesFile.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(FIS, StandardCharsets.UTF_8));

            //Saving text read into Builder Object
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            FIS.close();

            JSONArray JsArray = new JSONArray(sb.toString()); //create JSON Array
            for(int i = 0; i < JsArray.length(); i++){
                JSONObject JsObject = JsArray.getJSONObject(i); //create tmp JSON Object
                String NoteTitle = JsObject.getString("JSNoteTitle");
                String NoteDescription = JsObject.getString("JSNoteDescription");
                String NoteDateTime = JsObject.getString("JSNoteDateTime");

                Note note = new Note(NoteTitle, NoteDescription, NoteDateTime);
                NotesList.add(note);
            }

        } catch (FileNotFoundException e) { //if file not found
            e.printStackTrace();
        } catch (IOException e) { //if read line null
            e.printStackTrace();
        } catch (JSONException e) { //JSONArray
            e.printStackTrace();
        }

        Log.d(TAG, "readJSONFile: Read/Loaded from JSONFile");

    }

    //Write or Save JSON file
    private void writeJSONFile(){
        try{
            FileOutputStream FOS = getApplicationContext().openFileOutput("NotesFile.json", Context.MODE_PRIVATE);

            JSONArray JsArray = new JSONArray();

            for(Note NoteObject : NotesList){
                try{
                    JSONObject JSNoteObject = new JSONObject();
                    JSNoteObject.put("JSNoteTitle", NoteObject.getNoteTitle());
                    JSNoteObject.put("JSNoteDescription", NoteObject.getNoteDescription());
                    JSNoteObject.put("JSNoteDateTime", NoteObject.getNoteDateTime());

                    JsArray.put(JSNoteObject);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            //Writing to the file when finished
            String JSONFileContent = JsArray.toString();
            FOS.write(JSONFileContent.getBytes()); //needs to be in bytes
            FOS.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) { //for write
            e.printStackTrace();
        }

        Log.d(TAG, "writeJSONFile: Saved/Wrote into JSONFile");
    }
}