package com.example.elearningplatform.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.elearningplatform.R;

import java.util.ArrayList;

public class WeekSelectionLession_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_selection_lession);


        Intent intent = getIntent();
        String Cid = intent.getStringExtra("Course_Id");

        Log.e("Course Id", Cid);


        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Course Week");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ListView weekListView = findViewById(R.id.weekListView);


        ArrayList<String> weeks = new ArrayList<>();

        // Adding 16 weeks to the ArrayList
        for (int i = 1; i <= 16; i++) {
            weeks.add("Week " + i);
        }

        // Adapter to display weeks in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weeks);
        weekListView.setAdapter(adapter);


        weekListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected week (e.g., "Week 1")
                String selectedWeek = weeks.get(position);

                // Create an intent to go to the next activity
                Intent quizIntent = new Intent(WeekSelectionLession_Activity.this, CourseContent_Activity.class);

                // Pass the Course ID and selected week to the next activity
                quizIntent.putExtra("Course_Id", Cid);
                quizIntent.putExtra("Selected_Week", selectedWeek);


                // Start the next activity
                startActivity(quizIntent);
            }
        });
    }



}