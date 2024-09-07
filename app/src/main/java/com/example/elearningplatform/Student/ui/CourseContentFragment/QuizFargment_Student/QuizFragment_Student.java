package com.example.elearningplatform.Student.ui.CourseContentFragment.QuizFargment_Student;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.Quiz_New;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Student.ui.CourseContentFragment.QuizQuestionsActivity;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment_Student extends Fragment {

    private RecyclerView recyclerView;
    private QuizAdapter_Student adapter;
    private List<Quiz_New> quizList;

    int cid;
    String weekno;

    public QuizFragment_Student() {
        // Required empty public constructor
    }

    public static QuizFragment_Student newInstance(int cid, String week) {
        QuizFragment_Student fragment = new QuizFragment_Student();
        Bundle args = new Bundle();
        args.putInt("cid", cid);
        args.putString("week", week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cid = getArguments().getInt("cid");
            weekno = getArguments().getString("week");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz__student, container, false);

        Log.e("Fragment check","in Student Fragment");

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.QuizRecyclerView); // Ensure this ID matches your layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize quiz list and adapter
        quizList = new ArrayList<>();
        adapter = new QuizAdapter_Student(quizList,getContext(),cid, weekno); // Initialize adapter with quiz list
        recyclerView.setAdapter(adapter);

        // Load quiz data from API
        loadQuizzesFromApi();




        return view;
    }

    private void loadQuizzesFromApi() {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<Quiz_New>> call = apiService.getQuizzes(cid, weekno);

        call.enqueue(new Callback<List<Quiz_New>>() {
            @Override
            public void onResponse(Call<List<Quiz_New>> call, Response<List<Quiz_New>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    quizList.clear();
                    quizList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Failed to load quizzes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Quiz_New>> call, Throwable t) {
                Log.e("QuizFragment_Student", "API call failed: " + t.getMessage());
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
