package com.example.battleships;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class winScreen extends AppCompatActivity {

    Button button; // initialises the button.
    DatabaseHelper mDatabaseHelper;
    private Button btnAdd;
    private EditText editName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.win_screen);
        btnAdd = (Button) findViewById(R.id.btnAdd);



        TextView v = (TextView)findViewById(R.id.scoreValue);
        final int totalScore = getIntent().getIntExtra("TOTAL_SCORE", 100);
        v.setText(String.valueOf(totalScore));
        button = (Button) findViewById(R.id.returnButtonW); // links the button to the end turn button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(winScreen.this, menu.class); // initialises the intent.
                finish(); // ends the activity.
                startActivity(intent); // starts the intent.
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editName.getText().toString() + " " + totalScore;
                if(editName.length() != 0){
                    AddData(newEntry);
                    editName.setText("");
                } else {
                    toastMessage("You must put something in he text field!");
                }
            }
        });
    }

    public void AddData(String newEntry){
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if(insertData)
        {
            toastMessage("Data Successfully Inserted!");
        } else
        {
            toastMessage("Something went wrong");
        }
    }

    private  void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
