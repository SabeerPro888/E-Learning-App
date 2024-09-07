package com.example.elearningplatform.Teacher.NotesRecyclerView;

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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearningplatform.APIService;
import com.example.elearningplatform.Notes;
import com.example.elearningplatform.R;
import com.example.elearningplatform.RetrofitClient;
import com.example.elearningplatform.Teacher.PdfViewerActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {


    private List<Notes> noteList;
    private Context context;


    public NotesAdapter(List<Notes> noteList,Context context) {
        this.noteList = noteList;
        this.context = context;

    }

    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notes_item, parent, false);
        return new NoteViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {
        Notes note = noteList.get(position);
        holder.fileNameTextView.setText(note.getFileName());
        holder.checkimage.setVisibility(View.GONE);

//        holder.itemView.setOnClickListener(v -> {
//
//            String filepath= RetrofitClient.BASE_URL + "ELearning/Content/"+note.getPath()+"/Notes/"+note.getWeek()+"/";
//            downloadAndOpenFile( filepath, note.getFileName());
//
//            Log.e("File Path",filepath+note.getFileName());
//
//
////            openFile(filepath);
//        });


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PdfViewerActivity.class);
            String filepath= RetrofitClient.BASE_URL + "ELearning/Content/"+note.getPath()+"/Notes/"+note.getWeek()+"/";

            intent.putExtra("fileUrl", filepath+note.getFileName());

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);



        });
    }




    private String getMimeType(String filePath) {
        if (filePath.endsWith(".pdf")) {
            return "application/pdf";
        } else if (filePath.endsWith(".doc") || filePath.endsWith(".docx")) {
            return "application/msword";
        } else {
            return "*/*";
        }

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }


    class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView;

        ImageView checkimage;


        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            checkimage=itemView.findViewById(R.id.tickImage);

        }
    }

    private void downloadAndOpenFile(String fileUrl, String fileName) {
        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<ResponseBody> call = apiService.downloadFile(fileUrl+fileName);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Save the file to local storage
                        File file = new File(context.getFilesDir(), fileName);
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            fos.write(response.body().bytes());
                        }

                        // Open the file with FileProvider
                        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
                        openFile(uri,fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Failed to save file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Failed to download file", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void openFile(Uri fileUri,String fileName) {

        String mimeType = getMimeType(fileName+fileName);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(fileUri, "application/pdf"); // Change MIME type as needed
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(intent, "Open file with"));
    }
}

