package com.kovlev.a2dirstat.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kovlev.a2dirstat.R;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Fragment displaying the details of a single file
 */
public class DetailsFragment extends Fragment {

    private TextView tvName, tvSize, tvModified;

    private File file;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd hh:mm:ss");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null){
            // A string argument was passed by the creating activity to indicate which file to display information about
            if(getArguments() != null) {
                file = new File(getArguments().getString("item", ""));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, parent, false);
    }

    // From https://stackoverflow.com/questions/6265298/action-view-intent-for-a-file-with-unknown-mimetype/6381479
    // And unnecessarily complicated method for opening a file with any extensions
    public void openFile(File url) {

        Uri uri = Uri.fromFile(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        tvName = view.findViewById(R.id.tvName);
        tvSize = view.findViewById(R.id.tvSize);
        tvModified = view.findViewById(R.id.tvModified);

        if (file != null) {
            // Showing information about the file
            tvName.setText(file.getAbsolutePath());
            tvSize.setText(NumberFormat.getIntegerInstance().format(file.length()) + " " + getString(R.string.bytes));
            tvModified.setText(sdf.format(new Date(file.lastModified())));
        }

        view.findViewById(R.id.btnOpen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (file != null) {
                    openFile(file);
                }
            }
        });

    }
}
