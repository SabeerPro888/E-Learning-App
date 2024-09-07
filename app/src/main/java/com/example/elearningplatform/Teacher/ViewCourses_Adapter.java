package com.example.elearningplatform.Teacher;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.CourseDetailsActivity;
import com.example.elearningplatform.Courses;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewCourses_Adapter extends RecyclerView.Adapter<ViewCourses_ViewHolder> {
    private ArrayList<Courses> ItemsList;
    private Context context;

    private List<Courses> itemListFiltered;

    public ViewCourses_Adapter(Context context, ArrayList<Courses> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.itemListFiltered = new ArrayList<>(ItemsList);    }

    @NonNull
    @Override
    public ViewCourses_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_teachercourses, parent, false);
        return new ViewCourses_ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewCourses_ViewHolder holder, int position) {

        Courses currentItem = itemListFiltered.get(position); // Change here

        holder.Title.setText(currentItem.getCourse_title());
        holder.Description.setText(currentItem.getCourse_Description());

        String imagePath = RetrofitClient.BASE_URL + "/Elearning/Content/" + currentItem.getCourse_image();
        Log.e("Course image", currentItem.getCourse_image());
        Log.e("Course path", imagePath);

        Picasso.get().load(imagePath).into(holder.image);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click here
                // Start new activity and pass item details
                Intent intent = new Intent(context, CourseDetailsActivity.class);
                intent.putExtra("Course_Details", currentItem);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);


            }


        });


    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }
}
