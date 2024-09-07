package com.example.elearningplatform.Student.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.Courses;
import com.example.elearningplatform.GlobalVariables;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Teacher.ViewCourses_Adapter;
import com.example.elearningplatform.databinding.FragmentHomeBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    ViewCoursesAdapter objAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        String email= GlobalVariables.getInstance().getEmail();

        Call<List<Courses>> call = apiService.viewAllCourses();
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

                    recyclerView = binding.ViewAllCoursesRecyclerView;
                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);



                    objAdapter = new ViewCoursesAdapter(getContext(), ItemsArrayList);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
                    recyclerView.setAdapter(objAdapter);
                }

            }



            @Override
            public void onFailure(Call<List<Courses>> call, Throwable t) {
                Log.e("API Call", "Error " +t.getMessage());

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}