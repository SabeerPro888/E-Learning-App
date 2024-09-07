package com.example.elearningplatform.Student.ui.CourseContentFragment.NotesFragment_Student;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.Courses;
import com.example.elearningplatform.GlobalVariables;
import com.example.elearningplatform.Notes;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Teacher.PdfViewerActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesAdapter_Student extends RecyclerView.Adapter<NotesAdapter_Student.NoteViewHolder_Student> {

    private List<Notes> noteList;
    private Context context;


    public NotesAdapter_Student(List<Notes> noteList, Context context) {
        this.noteList = noteList;
        this.context = context;

    }

    @Override
    @NonNull
    public NoteViewHolder_Student onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_item, parent, false);
        return new NoteViewHolder_Student(view);



    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder_Student holder, int position) {
        Notes note = noteList.get(position);
        holder.fileNameTextView.setText(note.getFileName());

        int contentid = note.getId();
        String email= GlobalVariables.getInstance().getEmail();

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<String> call = apiService.CheckReadContent(contentid,email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    String resp= response.body().toString();
                    if(resp.equals("Read")){
                        holder.checkimage.setVisibility(View.VISIBLE);
                    }else{
                        holder.checkimage.setVisibility(View.GONE);

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

        Log.e("Note Id",String.valueOf(contentid));

        holder.itemView.setOnClickListener(v -> {





            Call<String> call2 = apiService.ReadContent(contentid,email);
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if(response.isSuccessful()){
                        String resp=response.body().toString();



                        //path=LinearAlgebra_1
                        String filePath = RetrofitClient.BASE_URL + "ELearning/Content/" + note.getPath() + "/Notes/" +note.getWeek()+ "/";
                        String fileUrl = filePath + note.getFileName();

                        // Create an intent to open the file in PdfViewerActivity
                        Intent intent = new Intent(context, PdfViewerActivity.class);
                        intent.putExtra("fileUrl", fileUrl);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                        // Check file extension to determine if the file is supported
                        if (fileUrl.endsWith(".pdf") || fileUrl.endsWith(".doc") || fileUrl.endsWith(".docx")) {

                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Unsupported file format", Toast.LENGTH_SHORT).show();
                        }




                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });




        });
    }






    @Override
    public int getItemCount() {
        return noteList.size();
    }





    class NoteViewHolder_Student extends RecyclerView.ViewHolder {
        TextView fileNameTextView;
        ImageView checkimage;

        NoteViewHolder_Student(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            checkimage=itemView.findViewById(R.id.tickImage);
        }
    }

    // Optional: If you want to keep the openFile method
    private void openFile(Uri fileUri, String fileName) {
        String mimeType = getMimeType(fileName);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, mimeType);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Open file with"));
    }

    // Correct the getMimeType method
    private String getMimeType(String filePath) {
        if (filePath.endsWith(".pdf")) {
            return "application/pdf";
        } else if (filePath.endsWith(".doc") || filePath.endsWith(".docx")) {
            return "application/msword";
        } else {
            return "*/*";
        }
    }
}
