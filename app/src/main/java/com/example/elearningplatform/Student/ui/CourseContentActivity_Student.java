package com.example.elearningplatform.Student.ui;

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
import com.example.elearningplatform.Student.ui.CourseContentFragment.NotesFragment_Student.NotesFragment_Student;
import com.example.elearningplatform.Student.ui.CourseContentFragment.QuizFargment_Student.QuizFragment_Student;
import com.example.elearningplatform.Student.ui.CourseContentFragment.VideosFragment_Student.VideosFragment_Student;


public class CourseContentActivity_Student extends AppCompatActivity {


    int cid;
    String week;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_content_student);

        Intent intent=getIntent();

        String Cid = intent.getStringExtra("Course_Id");
        cid = Integer.parseInt(Cid);
        week=intent.getStringExtra("Selected_Week");


        Button notesButton = findViewById(R.id.notesButtonStudent);
        Button videosButton = findViewById(R.id.videosButtonStudent);
        Button quizzesButton = findViewById(R.id.quizButtonStudent);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);

        toolbarTitle.setText("Content");


        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (savedInstanceState == null) {
            loadFragment(NotesFragment_Student.newInstance(cid,week));
        }

        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFragment(NotesFragment_Student.newInstance(cid,week));
                Log.e("Week in CourseContent Activity",week);

            }
        });

        videosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(VideosFragment_Student.newInstance(cid,week));
            }
        });

        quizzesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(QuizFragment_Student.newInstance(cid,week));
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentFrame_student, fragment);
        fragmentTransaction.commit();
    }

}