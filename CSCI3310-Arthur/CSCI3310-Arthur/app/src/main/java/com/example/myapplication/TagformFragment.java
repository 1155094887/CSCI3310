package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BooleanSupplier;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TagformFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TagformFragment extends DialogFragment {

    int count = 0;
    public static TagformFragment newInistance(String time,String name,String uri,ArrayList<String> history) {
        TagformFragment f = new TagformFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("time", time);
        args.putString("name",name);
        args.putString("uri",uri);
        args.putStringArrayList("history",history);
        f.setArguments(args);


        return f;
    }
    @NonNull
    @Override
    //generate dialog instance
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String time_list = getArguments().getString("time").split(" ")[0];
        int year = Integer.parseInt(time_list.split(":")[0]);
        int month = Integer.parseInt(time_list.split(":")[1]);
        int day = Integer.parseInt(time_list.split(":")[2]);


        String name = getArguments().getString("name");

        String uri = getArguments().getString("uri");
        String csv_read = Environment.getExternalStorageDirectory().getAbsolutePath();
        File picture_info_read = new File(csv_read.concat("/"+ Environment.DIRECTORY_DOCUMENTS), "detail.csv");
        ArrayList<String> stringArray_read = new ArrayList<String>();
        ArrayList<String> histories = getArguments().getStringArrayList("history");
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
        String show_name = "";
        String show_time = "";
        String show_location = "";
        String show_people = "";
        for (String row : stringArray_read){
            if(row.equals(",")|| stringArray_read.indexOf(row) == 0){
                continue;
            }
            if (row.contains(uri)){
                show_name = row.split(",")[0].substring(1,row.split(",")[0].length()-1);
                show_time = row.split(",")[1].substring(1,row.split(",")[1].length()-1);
                show_location = row.split(",")[2].substring(1,row.split(",")[2].length()-1);
                show_people = row.split(",")[4].substring(1,row.split(",")[4].length()-1);
            }
        }
        View tag_view = inflater.inflate(R.layout.fragment_tagform, null);
        EditText editName = (EditText) tag_view.findViewById(R.id.editName);
        EditText editView = (EditText) tag_view.findViewById(R.id.editField);
        AutoCompleteTextView editPerson = tag_view.findViewById(R.id.edit_person);
        Spinner dropdown = tag_view.findViewById(R.id.spinnerplace);
        Button add = tag_view.findViewById(R.id.addButton);
        for(int i = 1; i < show_people.split(";").length;i++){
            count += 1;
            LinearLayout layout = (LinearLayout) tag_view.findViewById(R.id.linear_layout);
            AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getContext());
            layout.addView(autoCompleteTextView);
            autoCompleteTextView.setGravity(Gravity.TOP);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) autoCompleteTextView.getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.setMargins(20,20,0,0);

            autoCompleteTextView.setLayoutParams(layoutParams);
            autoCompleteTextView.setTag("edit_person"+String.valueOf(i));
            try{
                autoCompleteTextView.setText(show_people.split(";")[i]);
            }catch(ArrayIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }



        if(!show_name.equals("")){
            editName.setText(show_name);
        }
        else if(name != null){
            editName.setText(name);
        }
        if(!show_time.equals("")){
            editView.setText(show_time);
        }else if(time_list != null){
            editView.setText(String.join("-",time_list.split(":")));
        }
//        editView.setClickable(false);
        editView.setFocusable(false);
        DatePickerDialog date_view = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
                editView.setText(dateTime);
            }

        }, year, month-1, day);


        //create a list of items for the spinner.
        String[] items = getActivity().getApplication().getResources().getStringArray(R.array.district_name);
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        String[] districts = getActivity().getApplication().getResources().getStringArray(R.array.position);
        if( Arrays.asList(items).contains(show_location)){

            dropdown.setSelection(Arrays.asList(items).indexOf(show_location));
        }

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                TextView txtView = (TextView)selectedItemView;
                String district_name = (String) txtView.getText();
//                Toast.makeText(getActivity(),"selected: district: "+district_name +districts[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        tag_view.findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                date_view.show();
            }
        });

        editPerson.setText(show_people.split(";")[0]);
//        editPerson.setLines(3);

        String[] al = {"a","b","c"};

        editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));

        editPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editPerson.showDropDown();
                String selection = (String) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), selection,
                        Toast.LENGTH_SHORT);
//                delButton.setAlpha(.8f);
            }
        });

        editPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                editPerson.showDropDown();
            }
        });

        String finalShow_people = show_people;
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count += 1;
                LinearLayout layout = (LinearLayout) tag_view.findViewById(R.id.linear_layout);
                AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getContext());
                layout.addView(autoCompleteTextView);
                autoCompleteTextView.setGravity(Gravity.TOP);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) autoCompleteTextView.getLayoutParams();
                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
                layoutParams.setMargins(20,20,0,0);

                autoCompleteTextView.setLayoutParams(layoutParams);
                autoCompleteTextView.setTag("edit_person"+String.valueOf(count));



            }
        });
//        editPerson.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));
//                for(String history : histories){
//                    Toast.makeText(getActivity(),"before "+history,Toast.LENGTH_SHORT).show();
//                    editPerson.setHint(history);
//                }
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                editPerson.showDropDown();
//                editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));
//                for(String history : histories){
//                    Toast.makeText(getActivity(),"on "+history,Toast.LENGTH_SHORT).show();
//                    editPerson.setHint(history);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//                editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));
//                for(String history : histories){
//                    Toast.makeText(getActivity(),"after "+history,Toast.LENGTH_SHORT).show();
//                    editPerson.setHint(history);
//                }
//            }
//        });
//        editPerson.setOnFocusChangeListener(new View.OnFocusChangeListener(){
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if(b){
//
//                    for(String history : histories){
//                        Toast.makeText(getActivity(),history,Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(getActivity(),"hehe xd",Toast.LENGTH_SHORT).show();;
//                }
//
//            }
//
//
//        });
//        editPerson.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                for(String history : histories){
//                    Toast.makeText(getActivity(),history,Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        // set dialog with on ok listener
        return new AlertDialog.Builder(requireContext())
                .setMessage("Tag Form")
//                .setView(R.id.editField)
                .setView(tag_view)
                .setPositiveButton("ok", (dialog, which) -> {
                    String name_out = editName.getText().toString();
                    String time_out = editView.getText().toString();
                    String place_out = dropdown.getSelectedItem().toString();
                    String lat_long_out = districts[dropdown.getSelectedItemPosition()];
                    String person_out = editPerson.getText().toString();
                    for(int i = 1; i <= count;i++){
                        person_out += ";"+((TextView)tag_view.findViewWithTag("edit_person"+String.valueOf(i))).getText().toString();
                    }
//                    person_out = person_out.substring(0,person_out.length()-1);

                    String uri_out = uri;
                    Toast.makeText(getActivity(),person_out,Toast.LENGTH_SHORT).show();
                    String data_out = "'"+name_out + "','" + time_out + "','"+ place_out + "','" + lat_long_out + "','"+person_out+"','"+uri_out+"'";
                    String csv = Environment.getExternalStorageDirectory().getAbsolutePath();

//                    Toast.makeText(getActivity(),csv,Toast.LENGTH_SHORT).show();
//                    CSVWriter writer = new CSVWriter(new FileWriter(csv));
//                    File root = Environment.getExternalStorageDirectory();
                    File picture_info = new File(csv.concat("/"+Environment.DIRECTORY_DOCUMENTS), "detail.csv");
                    picture_info.setWritable(true);

                    try {

//                        Toast.makeText(getActivity(), Boolean.toString(picture_info.exists()),Toast.LENGTH_SHORT).show();
                        if(picture_info.exists()){
                            BufferedReader file = new BufferedReader(new FileReader(picture_info));
                            StringBuffer inputBuffer = new StringBuffer();
                            ArrayList<String> stringArray = new ArrayList<String>();
                            String line;
                            boolean replace = false;
                            while ((line = file.readLine()) != null) {

                                if(line.contains(uri_out)){
                                    replace = true;
                                    inputBuffer.append(data_out);
                                    stringArray.add(data_out);

                                }else{

                                    inputBuffer.append(line);
                                    stringArray.add(line);
                                }
                                inputBuffer.append('\n');
//                                stringArray.add("\n");
                            }
                            file.close();
//                            Toast.makeText(getActivity(),Boolean.toString(replace),Toast.LENGTH_SHORT).show();;
                            if(!replace){
                                inputBuffer.append(data_out);
                                stringArray.add(data_out);
                            }
//                            Toast.makeText(getActivity(),stringArray,Toast.LENGTH_SHORT).show();
                            BufferedWriter writer = new BufferedWriter(new FileWriter(picture_info));
                            writer.write("Name,Time,Place,Latitude,Person,uri");
                            writer.write("\n");
                            for(String string :stringArray){

                                if(string.equals("\n") || string.equals("Name,Time,Place,Latitude,Person,uri") || string.equals("")){
                                    continue;
                                }else{

                                    writer.write(string);
                                    writer.write("\n");


                                }

                            }
//                            writer.write(data_out);
//                            writer.newLine();
                            writer.flush();
                            writer.close();

                        }else{
                            BufferedWriter writer = new BufferedWriter(new FileWriter(picture_info, true));
                            writer.write("Name,Time,Place,Latitude,Person,uri");
                            writer.newLine();
                            writer.write(data_out);
                            writer.newLine();
                            writer.flush();
                            writer.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


//                    editPerson.setAdapter();

                    ((MainActivity)getActivity()).setDate_in_EditText(person_out);
                } )
                .create();
    }

    public static String TAG = "PurchaseConfirmationDialog";
}