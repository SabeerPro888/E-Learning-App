package com.example.elearningplatform.Teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.elearningplatform.R;

public class CourseContent_Activity extends AppCompatActivity {

    int cid;
    String week;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content);

        Intent intent=getIntent();

        String Cid = intent.getStringExtra("Course_Id");
        cid = Integer.parseInt(Cid);
        week=intent.getStringExtra("Selected_Week");


        Button notesButton = findViewById(R.id.notesButton);
        Button videosButton = findViewById(R.id.videosButton);
        Button quizzesButton = findViewById(R.id.quizzesButton);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Content");


        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(NotesFragment.newInstance(cid,week));
        }

        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(NotesFragment.newInstance(cid,week));
                Log.e("Week in CourseContent Activity",week);

            }
        });

        videosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(VideosFragment.newInstance(cid,week));
            }
        });

        quizzesButton.setVisibility(View.GONE);

        quizzesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new QuizzesFragment());
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame, fragment);
        fragmentTransaction.commit();
    }
}