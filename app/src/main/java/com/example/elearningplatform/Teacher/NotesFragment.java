package com.example.elearningplatform.Teacher;

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
import com.example.elearningplatform.Teacher.NotesRecyclerView.NotesAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {



    private RecyclerView recyclerView;
    private NotesAdapter adapter;
    private List<Notes> noteList;

    private int cid;

    private String weekno;


    public NotesFragment() {
        // Required empty public constructor
    }


    public static NotesFragment newInstance(int cid,String week) {
        NotesFragment fragment = new NotesFragment();
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
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.recyclerviewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noteList = new ArrayList<>();
        adapter = new NotesAdapter(noteList,getContext());
        recyclerView.setAdapter(adapter);

        loadNotes();


        return view;

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