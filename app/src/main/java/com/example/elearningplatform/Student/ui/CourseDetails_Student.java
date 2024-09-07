package com.example.elearningplatform.Student.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.Courses;
import com.example.elearningplatform.GlobalVariables;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Teacher.WeekSelectionLession_Activity;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseDetails_Student extends AppCompatActivity {

    ImageView img;
    TextView title;
    TextView description;
    Button btnCreateQuiz;
    Button btnLessonPlan;
    ImageView btnEditLesson;

    int cid;

    Boolean Enrollment=false;


    Button btnEnroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details_student);


        Courses Courses;



        btnEnroll=findViewById(R.id.btnEnroll);


        img=findViewById(R.id.ImageViewCourse);
        title=findViewById(R.id.txtCourseTitle);
        description=findViewById(R.id.txtDescription);
        btnCreateQuiz=findViewById(R.id.btnCreateQuiz);
        btnLessonPlan=findViewById(R.id.btnLessonPlan);
        Courses = (Courses) getIntent().getSerializableExtra("Course_Details");

        cid=Courses.getCid();

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        Call<String> call=apiService.checkEnrollment(GlobalVariables.getInstance().getEmail(), cid);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String resp=response.body();
                    if(resp.equals("Enrolled")){
                        btnEnroll.setText("Enrolled");

                       Enrollment=true;


                            btnEnroll.setEnabled(false);  // Disable the enroll button




                    }else{
                        btnEnroll.setText("Enroll");
                        Enrollment=false;

                        btnEnroll.setEnabled(true);
                    }

                    Log.e("API Check","Sucessful Response");

                    Log.e("response body",resp);


                }
                else{
                    Log.e("API Check","Unsuccessfull Response");
                    Log.e("response body",response.body().toString());


                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("On Failure",t.getMessage());

            }
        });





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

        btnLessonPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(Enrollment==true){
                   Intent intent=new Intent(getApplicationContext(), WeekSelection_Student.class);
                   intent.putExtra("Course_Id",String.valueOf(Courses.getCid()));

                   startActivity(intent);
               }else{
                   showEnrollmentDialog();
               }
            }
        });

        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

                Call<String> call=apiService.EnrollInCourse(GlobalVariables.getInstance().getEmail(), cid);

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            if(response.body().equals("Enrolled")){
                                btnEnroll.setText("Enrolled");
                                Enrollment=true;
                                Log.e("API Check","Sucessful Response");
                            }
                        }else{
                            Log.e("API Check","Unsuccessfull Response");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e("API On Failure",t.getMessage());

                    }
                });
            }
        });




    }


    private void showEnrollmentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enrollment Required");
        builder.setMessage("You must enroll in this course first to access the lesson plan.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Optionally, you can add a "Enroll Now" button
//        builder.setNegativeButton("Enroll Now", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Handle the enrollment process here
//                dialog.dismiss();
//            }
//        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}