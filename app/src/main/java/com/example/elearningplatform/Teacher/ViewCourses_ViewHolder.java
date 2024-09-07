package com.example.elearningplatform.Teacher;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.R;

public class ViewCourses_ViewHolder extends RecyclerView.ViewHolder {

    TextView Title;
    ImageView image;
    TextView Description;

    public ViewCourses_ViewHolder(@NonNull View itemView) {
        super(itemView);

        this.Title=(TextView)itemView.findViewById(R.id.txtCourseTitle);
        this.Description=(TextView)itemView.findViewById(R.id.txtDescription);
        this.image=(ImageView)itemView.findViewById(R.id.ImageViewCourse);




    }
}
