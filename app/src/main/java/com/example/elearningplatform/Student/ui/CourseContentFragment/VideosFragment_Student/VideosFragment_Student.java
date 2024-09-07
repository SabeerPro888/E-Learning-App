package com.example.elearningplatform.Student.ui.CourseContentFragment.VideosFragment_Student;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Teacher.VideosRecyclerView.VideosAdapter;
import com.example.elearningplatform.Video;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideosFragment_Student#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment_Student extends Fragment {
    private RecyclerView recyclerView;
    private VideoAdapter_Student adapter;
    private List<Video> noteList;

    int cid;
    String weekno;


    public VideosFragment_Student() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VideosFragment_Student newInstance(int cid,String week) {
        VideosFragment_Student fragment = new VideosFragment_Student();
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
        View view = inflater.inflate(R.layout.fragment_videos__student, container, false);
        Log.e("Fragment check","in Student Fragment");

        recyclerView = view.findViewById(R.id.VideosRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noteList = new ArrayList<>();
        adapter = new VideoAdapter_Student(noteList,getContext());
        recyclerView.setAdapter(adapter);

        loadVideos();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the list when the fragment is resumed
        loadVideos();
    }


    private void loadVideos() {
        // Assume you have initialized Retrofit
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        String formattedWeekno = weekno.replace(" ", "");


        Log.e("Week in VideosFragment",formattedWeekno);
        Log.e("cid in VideosFragment",String.valueOf(cid));

        Call<List<Video>> call = apiService.getVideos(cid,formattedWeekno); // Use appropriate teacherId




        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    noteList.clear();
                    noteList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    Log.e("Api check","Response is successfull");
                }
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {
                Log.e("Api check",t.getMessage());

            }
        });


    }
}

