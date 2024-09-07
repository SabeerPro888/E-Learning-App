package com.example.elearningplatform.Teacher;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.elearningplatform.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfViewerActivity extends AppCompatActivity {

    private String fileUrl;
    private String fileType; // Variable to hold the file type (PDF or DOCX)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        // Get the file URL from the intent
        Intent intent = getIntent();
        fileUrl = intent.getStringExtra("fileUrl");

        if (fileUrl != null) {
            // Determine file type based on URL extension
            if (fileUrl.toLowerCase().endsWith(".pdf")) {
                fileType = "application/pdf";
            } else if (fileUrl.toLowerCase().endsWith(".docx")) {
                fileType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            } else {
                Log.e("PdfViewerActivity", "Unsupported file type");
                return;
            }

            // Start AsyncTask to download and view file
            new DownloadFileTask().execute(fileUrl);
        } else {
            Log.e("PdfViewerActivity", "No file URL provided");
        }
    }

    private class DownloadFileTask extends AsyncTask<String, Void, File> {

        protected File doInBackground(String... params) {
            String fileUrl = params[0];
            File file = null;

            try {
                // Create URL and connection
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Get Content-Type from header
                String contentType = connection.getContentType();
                Log.d("PdfViewerActivity", "Content-Type: " + contentType);

                // Determine file extension based on Content-Type
                String extension = getExtensionFromMimeType(contentType);

                // Create file in local storage with the correct extension
                File tempFile = new File(getExternalFilesDir(null), "downloaded_file" + extension);
                FileOutputStream fos = new FileOutputStream(tempFile);

                // Read from input stream and write to file
                InputStream is = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
                is.close();
                file = tempFile;
            } catch (Exception e) {
                Log.e("PdfViewerActivity", "Error downloading file: " + e.getMessage());
            }

            return file;
        }

        private String getExtensionFromMimeType(String mimeType) {
            if (mimeType.equalsIgnoreCase("application/pdf")) {
                return ".pdf";
            } else if (mimeType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                return ".docx";
            } else if (mimeType.equalsIgnoreCase("application/msword")) {
                return ".doc";
            }
            // Add more mappings if necessary
            return "";
        }

        @Override
        protected void onPostExecute(File file) {
            if (file != null && file.exists()) {
                Log.d("PdfViewerActivity", "File found: " + file.getAbsolutePath());

                Uri fileUri = FileProvider.getUriForFile(
                        PdfViewerActivity.this,
                        getApplicationContext().getPackageName() + ".provider",
                        file
                );

                // Verify the fileUri and file attributes
                Log.d("PdfViewerActivity", "File Uri: " + fileUri.toString());
                Log.d("PdfViewerActivity", "File path: " + file.getPath());
                Log.d("PdfViewerActivity", "File readable: " + file.canRead());

                String mimeType = getMimeType(file.getName());

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(fileUri, mimeType);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    Log.d("PdfViewerActivity", "Document viewer found, launching intent");
                    startActivity(intent);
                } else {
                    Log.e("PdfViewerActivity", "No document viewer found");
                    Toast.makeText(PdfViewerActivity.this, "No application found to open this file type", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("PdfViewerActivity", "File not found or cannot be accessed");
            }
        }

        private String getMimeType(String fileName) {
            String mimeType = "*/*"; // Default MIME type
            if (fileName != null) {
                Log.d("PdfViewerActivity", "File name for MIME type detection: " + fileName);
                String extension = "";

                // Check if the file name contains a dot, which indicates an extension
                int dotIndex = fileName.lastIndexOf('.');
                if (dotIndex > 0) {
                    extension = fileName.substring(dotIndex + 1).toLowerCase();
                    Log.d("PdfViewerActivity", "Extracted file extension: " + extension);
                } else {
                    Log.e("PdfViewerActivity", "No extension found in file name: " + fileName);
                }

                switch (extension) {
                    case "pdf":
                        mimeType = "application/pdf";
                        break;
                    case "doc":
                        mimeType = "application/msword";
                        break;
                    case "docx":
                        mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                        break;
                    default:
                        Log.e("PdfViewerActivity", "Unsupported file extension: " + extension);
                        mimeType = "*/*";
                        break;
                }
            } else {
                Log.e("PdfViewerActivity", "File name is null, cannot determine MIME type.");
            }

            Log.d("PdfViewerActivity", "Determined MIME type: " + mimeType);
            return mimeType;
        }



    }
}
