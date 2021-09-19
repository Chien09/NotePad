package com.example.multinotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    private EditText InputNoteTitle;
    private EditText InputNoteDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        InputNoteTitle = findViewById(R.id.InputNoteTitle);
        InputNoteDescription = findViewById(R.id.InputNoteDescription);

        //Allow EditText to be scrollable
        InputNoteDescription.setMovementMethod(new ScrollingMovementMethod());

        //Get data from MainActivity for OnClick on Note on the Recycler for editing the note
        Intent intent = getIntent();
        if(intent.hasExtra("EditNoteTitle")){
            InputNoteTitle.setText(intent.getStringExtra("EditNoteTitle"));
        }
        if(intent.hasExtra("EditNoteDescription")){
            InputNoteDescription.setText(intent.getStringExtra("EditNoteDescription"));
        }

    }

    //Inflate custom Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucustom2, menu);
        return true;
    }

    //For Menu Option Button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //Passing back UserInputs Data back to MainActivity
        Intent intent = new Intent();
        intent.putExtra("UserInputTitle", InputNoteTitle.getText().toString());
        intent.putExtra("UserInputDescription", InputNoteDescription.getText().toString());
        setResult(RESULT_OK, intent); //RESULT_OK = -1
        finish();

        return true;
    }


    @Override
    public void onBackPressed() {

        //Create Dialog pop up
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Note");
        builder.setMessage("Want to save the Note?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Passing back UserInputs Data back to MainActivity
                Intent intent = new Intent();
                intent.putExtra("UserInputTitle", InputNoteTitle.getText().toString());
                intent.putExtra("UserInputDescription", InputNoteDescription.getText().toString());
                setResult(RESULT_OK, intent); //RESULT_OK = -1
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();//Exit
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        //super.onBackPressed();  this will call back immediately after showing dialog, SO DON'T include
    }
}