package com.example.elearningplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elearningplatform.Teacher.WeekSelectionLession_Activity;
import com.example.elearningplatform.Teacher.WeekSelectionQuiz_Activity;
import com.squareup.picasso.Picasso;

public class CourseDetailsActivity extends AppCompatActivity {

    Courses Courses;
    ImageView img;
    TextView title;
    TextView description;
    Button btnCreateQuiz;

    ImageView btnEditLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        img=findViewById(R.id.ImageViewCourse);
        title=findViewById(R.id.txtCourseTitle);
        description=findViewById(R.id.txtDescription);
        btnCreateQuiz=findViewById(R.id.btnCreateQuiz);
        btnEditLesson=findViewById(R.id.btnEditLesson);


        Courses = (Courses) getIntent().getSerializableExtra("Course_Details");

        if(Courses!=null){
            String imagePath = RetrofitClient.BASE_URL + "/Elearning/Content/" + Courses.getCourse_image();
            Picasso.get().load(imagePath).into(img);
            title.setText(Courses.getCourse_title());
            description.setText(Courses.getCourse_Description());
            Log.e("Description",Courses.getCourse_Description());
        }

        TextView toolbarTitle = findViewById(R.id.toolbar_title);

// Set the title based on the activity
        toolbarTitle.setText("Course Details");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WeekSelectionQuiz_Activity.class);
                intent.putExtra("Course_Id",String.valueOf(Courses.getCid()));

                Log.e("Course ID in Details Page",String.valueOf(Courses.getCid()));
                startActivity(intent);
            }
        });

        btnEditLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), WeekSelectionLession_Activity.class);
                intent.putExtra("Course_Id",String.valueOf(Courses.getCid()));

                startActivity(intent);
            }
        });

    }
}