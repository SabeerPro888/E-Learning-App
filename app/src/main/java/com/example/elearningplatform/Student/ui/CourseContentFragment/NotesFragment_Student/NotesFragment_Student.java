package com.example.elearningplatform.Student.ui.CourseContentFragment.NotesFragment_Student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.Notes;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Teacher.NotesFragment;
import com.example.elearningplatform.Teacher.NotesRecyclerView.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment_Student#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment_Student extends Fragment {

    private RecyclerView recyclerView;
    private NotesAdapter_Student adapter;
    private List<Notes> noteList;

    private int cid;

    private String weekno;


    public NotesFragment_Student() {
        // Required empty public constructor
    }

    public static NotesFragment_Student newInstance(int cid,String week) {

        NotesFragment_Student fragment = new NotesFragment_Student();
            Bundle args = new Bundle();
            args.putInt("cid", cid);
            args.putString("week",week);
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
        View view = inflater.inflate(R.layout.fragment_notes__student, container, false);
        Log.e("Fragment check","in Student Fragment");
        String formattedWeekno = weekno.replace(" ", "");


        recyclerView = view.findViewById(R.id.recyclerviewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noteList = new ArrayList<>();
        adapter = new NotesAdapter_Student(noteList,getContext());
        recyclerView.setAdapter(adapter);

        loadNotes();

    return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list when the fragment is resumed
        loadNotes();
    }
    private void loadNotes() {
        // Assume you have initialized Retrofit
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        String formattedWeekno = weekno.replace(" ", "");


        Log.e("Week in NotesFragment",formattedWeekno);
        Call<List<Notes>> call = apiService.getNotes(cid,formattedWeekno); // Use appropriate teacherId




        call.enqueue(new Callback<List<Notes>>() {
            @Override
            public void onResponse(Call<List<Notes>> call, Response<List<Notes>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    noteList.clear();
                    noteList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.e("Api check","Response is successfull");
                }
            }

            @Override
            public void onFailure(Call<List<Notes>> call, Throwable t) {
                Log.e("Api check",t.getMessage());

            }
        });


    }
}