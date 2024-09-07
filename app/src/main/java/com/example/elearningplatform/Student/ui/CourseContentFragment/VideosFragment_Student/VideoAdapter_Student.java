package com.example.elearningplatform.Student.ui.CourseContentFragment.VideosFragment_Student;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.GlobalVariables;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Teacher.VideosRecyclerView.VideosAdapter;
import com.example.elearningplatform.Video;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoAdapter_Student extends RecyclerView.Adapter<VideoAdapter_Student.VideoViewHolder> {

    private final List<Video> videoList;
    private final Context context;

    public VideoAdapter_Student(List<Video> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoAdapter_Student.VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_video_item, parent, false);
        return new VideoAdapter_Student.VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapter_Student.VideoViewHolder holder, int position) {
        Video video = videoList.get(position);
        holder.titleTextView.setText(video.getTitle());

        int contentid = video.getId();
        String email = GlobalVariables.getInstance().getEmail();

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);

        // Check if content is read
        Call<String> checkCall = apiService.CheckReadContent(contentid, email);
        checkCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    String resp = response.body();
                    if (resp != null && resp.equals("Read")) {
                        holder.checkimage.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkimage.setVisibility(View.GONE);
                    }
                } else {
                    holder.checkimage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                holder.checkimage.setVisibility(View.GONE);
                Log.e("API Error", "CheckReadContent failed", t);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getUrl()));
            context.startActivity(intent);

            // Mark content as read
            Call<String> readCall = apiService.ReadContent(contentid, email);
            readCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        // You might want to update the checkimage visibility again after marking it as read
                        holder.checkimage.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkimage.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("API Error", "ReadContent failed", t);
                }
            });
        });

        Log.e("Video Id", String.valueOf(contentid));
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView checkimage;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.videoTitle);
            checkimage = itemView.findViewById(R.id.tickImage);
        }
    }
}
