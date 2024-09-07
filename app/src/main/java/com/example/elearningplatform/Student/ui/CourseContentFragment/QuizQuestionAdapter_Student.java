package com.example.elearningplatform.Student.ui.CourseContentFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.Quiz_Question;
import com.example.elearningplatform.R;

import java.util.List;

public class QuizQuestionAdapter_Student extends RecyclerView.Adapter<QuizQuestionAdapter_Student.QuizQuestionViewHolder_Student> {

    private List<Quiz_Question> questionsList;

    public QuizQuestionAdapter_Student(List<Quiz_Question> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public QuizQuestionViewHolder_Student onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_quiz_question, parent, false);
        return new QuizQuestionViewHolder_Student(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizQuestionViewHolder_Student holder, int position) {
        Quiz_Question question = questionsList.get(position);
        holder.questionTextView.setText(question.getQuestion());
        holder.option1RadioButton.setText(question.getOp1());
        holder.option2RadioButton.setText(question.getOp2());
        holder.option3RadioButton.setText(question.getOp3());
        holder.option4RadioButton.setText(question.getOp4());

        holder.optionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = group.findViewById(checkedId);
                if (selectedRadioButton != null) {
                    String selectedAnswer = selectedRadioButton.getText().toString();
                    question.setUserAnswer(selectedAnswer);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class QuizQuestionViewHolder_Student extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioGroup optionsRadioGroup;
        RadioButton option1RadioButton;
        RadioButton option2RadioButton;
        RadioButton option3RadioButton;
        RadioButton option4RadioButton;

        public QuizQuestionViewHolder_Student(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            optionsRadioGroup = itemView.findViewById(R.id.optionsRadioGroup);
            option1RadioButton = itemView.findViewById(R.id.option1RadioButton);
            option2RadioButton = itemView.findViewById(R.id.option2RadioButton);
            option3RadioButton = itemView.findViewById(R.id.option3RadioButton);
            option4RadioButton = itemView.findViewById(R.id.option4RadioButton);
        }
    }
}
