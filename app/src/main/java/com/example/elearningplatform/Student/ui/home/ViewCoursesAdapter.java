package com.example.elearningplatform.Student.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.CourseDetailsActivity;
import com.example.elearningplatform.Courses;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Student.ui.CourseDetails_Student;
import com.example.elearningplatform.Teacher.ViewCourses_ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewCoursesAdapter extends RecyclerView.Adapter<ViewCoursesAdapter.CoursesViewHolder> {

    private ArrayList<Courses> ItemsList;
    private Context context;

    private List<Courses> itemListFiltered;

    public ViewCoursesAdapter(Context context, ArrayList<Courses> ItemsList) {
        this.context = context;
        this.ItemsList = ItemsList;
        this.itemListFiltered = new ArrayList<>(ItemsList);    }
    @NonNull
    @Override
    public ViewCoursesAdapter.CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_teachercourses, parent, false);
        return new CoursesViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewCoursesAdapter.CoursesViewHolder holder, int position) {
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
                Intent intent = new Intent(context, CourseDetails_Student.class);
                intent.putExtra("Course_Details", currentItem);
                Log.e("Course DEtails",currentItem.getCourse_title());

                Log.e("Course DEtails",currentItem.getCourse_Description());

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return itemListFiltered.size();
    }

    public class CoursesViewHolder extends RecyclerView.ViewHolder{

        TextView Title;
        ImageView image;
        TextView Description;
        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);

            this.Title=(TextView)itemView.findViewById(R.id.txtCourseTitle);
            this.Description=(TextView)itemView.findViewById(R.id.txtDescription);
            this.image=(ImageView)itemView.findViewById(R.id.ImageViewCourse);
        }
    }
}
