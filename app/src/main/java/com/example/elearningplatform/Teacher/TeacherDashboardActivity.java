package com.example.elearningplatform.Teacher;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.Courses;
import com.example.elearningplatform.GlobalVariables;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeacherDashboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ViewCourses_Adapter objAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        TextView toolbarTitle = findViewById(R.id.toolbar_title);

// Set the title based on the activity
        toolbarTitle.setText("Your Courses");

        ImageButton backButton = findViewById(R.id.back_button);
       backButton.setVisibility(View.GONE);


        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email= GlobalVariables.getInstance().getEmail();

        Call<List<Courses>> call = apiService.getTeacherCourses(email);
        call.enqueue(new Callback<List<Courses>>() {
            @Override
            public void onResponse(Call<List<Courses>> call, Response<List<Courses>> response) {
                if (response.isSuccessful()) {
                    List<Courses> Items = (List<Courses>) response.body();
                    Log.e("Raw JSON", new Gson().toJson(response.body()));
                    ArrayList<Courses> ItemsArrayList = new ArrayList<>(Items);


                    for (Courses b : Items) {
                        Log.e("API Call", "Response Message: " + b.getCourse_title());
                        Log.e("API Call", "image name Message: " + b.getCourse_Description());
                    }
                    recyclerView = findViewById(R.id.TeacherCoursesRecyclerView);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);



                    objAdapter = new ViewCourses_Adapter(getApplicationContext(), ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);
                }

            }



            @Override
            public void onFailure(Call<List<Courses>> call, Throwable t) {
                Log.e("API Call", "Error " +t.getMessage());

            }
        });
    }
}