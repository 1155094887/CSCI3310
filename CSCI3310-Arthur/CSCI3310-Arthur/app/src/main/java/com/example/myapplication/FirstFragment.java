package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.databinding.FragmentFirstBinding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    GridView gallery;
    ImageAdaptor mImageAdaptor;
    static int[] logo = {
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground
    };

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        String csv_read = Environment.getExternalStorageDirectory().getAbsolutePath();
        File picture_info_read = new File(csv_read.concat("/"+ Environment.DIRECTORY_DOCUMENTS), "detail.csv");
        ArrayList<String> stringArray_read = new ArrayList<String>();

        BufferedReader file_read = null;
        try {
            file_read = new BufferedReader(new FileReader(picture_info_read));

            String line;
            boolean replace = false;
            while ((line = file_read.readLine()) != null) {
                stringArray_read.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"Error getting data from external storage",Toast.LENGTH_SHORT).show();
        }
        Uri[] uri = new Uri[stringArray_read.size()];
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        for (String row : stringArray_read){
            if(row.equals(",")|| stringArray_read.indexOf(row) == 0){
                continue;
            }
            uri[stringArray_read.indexOf(row)] = Uri.parse(row.split(",")[5].substring(1,row.split(",")[5].length()-1));
        }

        View view = inflater.inflate(R.layout.fragment_first, container, false);
        gallery = view.findViewById(R.id.gridview_show);
//        TextView temp = view.findViewById(R.id.txt_fragment);
//        temp.setText("heha");
        mImageAdaptor = new ImageAdaptor(getActivity(), logo, uri,false);
        gallery.setAdapter(mImageAdaptor);
        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
         String csv_read = Environment.getExternalStorageDirectory().getAbsolutePath();
        File picture_info_read = new File(csv_read.concat("/"+ Environment.DIRECTORY_DOCUMENTS), "detail.csv");
        ArrayList<String> stringArray_read = new ArrayList<String>();

        BufferedReader file_read = null;
        try {
            file_read = new BufferedReader(new FileReader(picture_info_read));

            String line;
            boolean replace = false;
            while ((line = file_read.readLine()) != null) {
                stringArray_read.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),"Error getting data from external storage",Toast.LENGTH_SHORT).show();
        }
        Uri[] uri = new Uri[stringArray_read.size()];

        for (String row : stringArray_read){
            if(row.equals(",")|| stringArray_read.indexOf(row) == 0){
                continue;
            }
            uri[stringArray_read.indexOf(row)] = Uri.parse(row.split(",")[5].substring(1,row.split(",")[5].length()-1));
        }


        GridView gallery = (GridView) getActivity().findViewById(R.id.gridview_show);
//        TextView temp = view.findViewById(R.id.txt_fragment);
//        temp.setText("heha");
        mImageAdaptor = new ImageAdaptor(getActivity(), logo, uri,false);
        gallery.setAdapter(mImageAdaptor);



    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}