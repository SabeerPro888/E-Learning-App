package com.example.elearningplatform.Student.ui.CourseContentFragment.QuizFargment_Student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.elearningplatform.APIService;
import com.example.elearningplatform.GlobalVariables;
import com.example.elearningplatform.Quiz_New;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Student.ui.CourseContentFragment.QuizQuestionsActivity;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizAdapter_Student extends RecyclerView.Adapter<QuizAdapter_Student.QuizViewHolder> {

    private List<Quiz_New> quizList;
    private int cid;
    private String weekno;

    private boolean check;
    Context context;

    public QuizAdapter_Student(List<Quiz_New> quizList, Context context, int cid, String weekno) {
        this.quizList = quizList;
        this.context = context;
        this.cid = cid;
        this.weekno = weekno;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Quiz_New quiz = quizList.get(position);
        holder.quizTitle.setText(quiz.getQuiz_Title());



        // Check if the student has completed all the content for the week
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email = GlobalVariables.getInstance().getEmail();
        String formattedWeekno = weekno.replace(" ", "");

        Call<String> call = apiService.CheckIfAllContentRead(cid, formattedWeekno, email);

        Log.d("Response MEssage content check all",weekno);
        Log.d("Response MEssage content check all",String.valueOf(cid));


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String resp = response.body();
                    Log.d("Response MEssage content check all",resp);
                    if (resp.equals("All content has been read.")) {
                        check=true;
                    } else {
                        check=false;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("QuizAdapter_Student", "API call failed: " + t.getMessage());
            }
        });

        // Handle item clicks
        // Handle item clicks
        holder.itemView.setOnClickListener(v -> {
            if (!check) {
                Log.d("QuizAdapter_Student", "Item is disabled, showing dialog.");
                // Show dialog if the item is disabled
                showContentNotCompletedDialog();

            } else {
                // Navigate to the quiz questions activity if the item is enabled
                Intent intent = new Intent(context, QuizQuestionsActivity.class);
                intent.putExtra("quiz_id", String.valueOf(quiz.getQid()));
                Log.e("Quiz ID", String.valueOf(quiz.getQid()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView quizTitle;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            quizTitle = itemView.findViewById(R.id.quizTitle);
        }
    }

    // Function to show a dialog when the quiz is locked
    // Function to show a dialog when the quiz is locked
    private void showContentNotCompletedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Quiz Locked");
        builder.setMessage("You should go through all the contents of the week before attempting the quiz.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
