package com.example.elearningplatform.Teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.BottomNavigationActivity;
import com.example.elearningplatform.Quiz_New;
import com.example.elearningplatform.Quiz_Question;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create_Quiz_Activity extends AppCompatActivity {

    EditText QuizTitle;
    LinearLayout questionsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        QuizTitle = findViewById(R.id.quizTitleEditText);
        questionsContainer = findViewById(R.id.questionsContainer);

        Button addQuestionButton = findViewById(R.id.addQuestionButton);
        Button submitQuizButton = findViewById(R.id.submitQuizButton);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageButton backButton = findViewById(R.id.back_button);

        toolbarTitle.setText("Create Quiz");

        backButton.setOnClickListener(v -> onBackPressed());

        addQuestionButton.setOnClickListener(v -> {
            // Inflate the question_item.xml layout
            View questionView = getLayoutInflater().inflate(R.layout.question_item, null);

            // Add the new question to the questionsContainer
            questionsContainer.addView(questionView);
            ImageView deleteQuestionButton = questionView.findViewById(R.id.deleteQuestionButton);

            deleteQuestionButton.setOnClickListener(v1 -> questionsContainer.removeView(questionView));
        });

        submitQuizButton.setOnClickListener(v -> {
            // Prepare the Quiz_New object
            Quiz_New quiz = new Quiz_New();
            quiz.setQuiz_Title(QuizTitle.getText().toString().trim());

            int count = questionsContainer.getChildCount();
            List<Quiz_Question> questionsList = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                View questionView = questionsContainer.getChildAt(i);

                // Get the question text
                EditText questionEditText = questionView.findViewById(R.id.questionEditText);
                String questionText = questionEditText.getText().toString().trim();

                Quiz_Question quizQuestion = new Quiz_Question();
                quizQuestion.setQuestion(questionText);

                // Get the custom choices
                EditText choice1EditText = questionView.findViewById(R.id.choice1EditText);
                EditText choice2EditText = questionView.findViewById(R.id.choice2EditText);
                EditText choice3EditText = questionView.findViewById(R.id.choice3EditText);
                EditText choice4EditText = questionView.findViewById(R.id.choice4EditText);

                quizQuestion.setOp1(choice1EditText.getText().toString().trim());
                quizQuestion.setOp2(choice2EditText.getText().toString().trim());
                quizQuestion.setOp3(choice3EditText.getText().toString().trim());
                quizQuestion.setOp4(choice4EditText.getText().toString().trim());

                // Get the selected correct answer
                RadioGroup choicesRadioGroup = questionView.findViewById(R.id.RadioGroup);
                int selectedChoiceId = choicesRadioGroup.getCheckedRadioButtonId();

                if (selectedChoiceId != -1) {
                    String correctAnswer = null;

                    if (selectedChoiceId == R.id.choice1RadioButton) {
                        correctAnswer = choice1EditText.getText().toString().trim();
                    } else if (selectedChoiceId == R.id.choice2RadioButton) {
                        correctAnswer = choice2EditText.getText().toString().trim();
                    } else if (selectedChoiceId == R.id.choice3RadioButton) {
                        correctAnswer = choice3EditText.getText().toString().trim();
                    } else if (selectedChoiceId == R.id.choice4RadioButton) {
                        correctAnswer = choice4EditText.getText().toString().trim();
                    }

                    quizQuestion.setCorrect_ans(correctAnswer);
                } else {
                    Log.e("Create_Quiz_Activity", "No correct answer selected for question " + (i + 1));
                }

                questionsList.add(quizQuestion);
            }

            quiz.setQuestionsList(questionsList);
            quiz.setQuestionsNumber(questionsList.size());

            Intent intent=getIntent();
            String weekno = intent.getStringExtra("Selected_Week");

            quiz.setWeekno(weekno);

            // Log the quiz data for debugging
            Log.d("Quiz Data", "Quiz Title: " + quiz.getQuiz_Title());
            Log.d("Quiz Data", "Total Questions: " + quiz.getQuestionsNumber());

            for (int i = 0; i < questionsList.size(); i++) {
                Quiz_Question q = questionsList.get(i);
                Log.d("Quiz Data", "Question " + (i + 1) + ": " + q.getQuestion());
                Log.d("Quiz Data", "Option 1: " + q.getOp1());
                Log.d("Quiz Data", "Option 2: " + q.getOp2());
                Log.d("Quiz Data", "Option 3: " + q.getOp3());
                Log.d("Quiz Data", "Option 4: " + q.getOp4());
                Log.d("Quiz Data", "Correct Answer: " + q.getCorrect_ans());
            }

            // Validate quiz title
            if (quiz.getQuiz_Title().isEmpty()) {
                QuizTitle.setError("Quiz title is required");
                return;
            }

            // Get the course ID from the Intent and set it in the Quiz_New object
            String Cid = intent.getStringExtra("Course_Id");
            int cid = Integer.parseInt(Cid);
            quiz.setCid(cid);

            // Send the quiz data to the server
            APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
            Call<String> call = apiService.submitQuiz1(quiz);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.e("API Check", "Data Saved");

                        Toast.makeText(getApplicationContext(),"Quiz Uploaded",Toast.LENGTH_SHORT);

                        Intent intent =new Intent(getApplicationContext(), TeacherDashboardActivity.class);
                        startActivity(intent);

                    } else {
                        Log.e("API Check", "Unsuccessful response");
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("API Check", t.getMessage());
                }
            });
        });
    }
}
