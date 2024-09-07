package com.example.elearningplatform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elearningplatform.Teacher.TeacherDashboardActivity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText EmailEditText=findViewById(R.id.txtusername);
        EditText PasswordEditText=findViewById(R.id.txtpass);

        btnLogin=findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String E=EmailEditText.getText().toString();
                String P=PasswordEditText.getText().toString();

                APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                Call<String> call = apiService.Login(E, P);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            String responseData = response.body();
                            // Process responseData here based on the received response message
                            if (responseData.equals("Teacher")) {
                                Toast.makeText(getApplicationContext(), "Teach logged in", Toast.LENGTH_SHORT).show();

                                GlobalVariables.getInstance().setEmail(E);
                                Intent intent=new Intent(getApplicationContext(), TeacherDashboardActivity.class);
                                startActivity(intent);


                            } else if (responseData.equals("Student")) {
                                GlobalVariables.getInstance().setEmail(E);
                                Intent intent=new Intent(getApplicationContext(), BottomNavigationActivity.class);
                                startActivity(intent);


                                Toast.makeText(getApplicationContext(), "Student logged in", Toast.LENGTH_SHORT).show();
                            } else {
                                // Handle Invalid Email and Password response
                            }
                        } else {
                            try {
                                String error = response.errorBody().string();
                                Log.e("Retrofit", "Error: " + error); // Log the error
                            } catch (IOException e) {
                                Log.e("Retrofit", "Error while getting error body: " + e.getMessage()); // Log the IOException
                                throw new RuntimeException(e);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        String errorMessage = t.getMessage(); // Get the exception message
                        if (errorMessage != null) {
                            Toast.makeText(getApplicationContext(), "Failed"+errorMessage, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Unknown error", Toast.LENGTH_SHORT).show();
                        }                    }
                });



            }
        });
    }
}