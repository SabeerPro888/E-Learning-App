package com.example.elearningplatform.Student.ui.CourseContentFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.BottomNavigationActivity;
import com.example.elearningplatform.GlobalVariables;
import com.example.elearningplatform.Quiz_New;
import com.example.elearningplatform.Quiz_Question;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizQuestionsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuizQuestionAdapter_Student adapter;
    private Button submitButton;
    private TextView quizTitle;
    private List<Quiz_Question> questionsList; // Moved here to class scope

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);
        quizTitle=findViewById(R.id.quizTitleTextView);


        recyclerView = findViewById(R.id.questionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        submitButton = findViewById(R.id.submitQuizButton);

        Intent intent = getIntent();
        String qid = intent.getStringExtra("quiz_id");

        Log.e("Quiz id in Quiz Questions Activity", qid);

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<Quiz_New> call = apiService.viewQuizById(Integer.parseInt(qid));

        call.enqueue(new Callback<Quiz_New>() {
            @Override
            public void onResponse(Call<Quiz_New> call, Response<Quiz_New> response) {
                if (response.isSuccessful()) {
                    Quiz_New quiz = response.body();
                    if (quiz != null) {
                        quizTitle.setText(quiz.getQuiz_Title());
                        questionsList = quiz.getQuestionsList(); // Assign to class-level variable
                        adapter = new QuizQuestionAdapter_Student(questionsList);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Quiz_New> call, Throwable t) {
                Log.e("API Error", "Failed to load quiz data", t);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("QuizQuestionsActivity", "Submit button clicked");
                if (questionsList != null) {
                    Log.d("QuizQuestionsActivity", "Questions list size: " + questionsList.size());
                    int score = 0;
                    for (Quiz_Question question : questionsList) {
                        Log.d("QuizQuestionsActivity", "Checking question: " + question.getQuestion());
                        if (question.getUserAnswer() != null && question.getUserAnswer().equals(question.getCorrect_ans())) {
                            score++;
                        }

                    }
                    Log.d("QuizQuestionsActivity", "sYour Score: " + score + "/" + questionsList.size());

                    APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
                    String email= GlobalVariables.getInstance().getEmail();
                    Call<Void> call = apiService.SubmitQuizStudent(Integer.parseInt(qid),questionsList.size(),score,email);
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()){
                                Intent intent=new Intent(QuizQuestionsActivity.this, BottomNavigationActivity.class);
                                startActivity(intent);
                            }else{
                                Log.e("Api check ","Unsuccessful response");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Api check ",t.getMessage());

                        }
                    });


                    Toast.makeText(QuizQuestionsActivity.this, "Your Score: " + score + "/" + questionsList.size(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(QuizQuestionsActivity.this, "Quiz data is not loaded yet.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
