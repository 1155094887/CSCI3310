package fragments;
import com.example.csci3310.*;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.function.BooleanSupplier;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link showTagFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class showTagFragment extends DialogFragment {

    int count = 0;
//    ArrayList<String> urilist;
    public static showTagFragment newInistance(String time, String name, Uri uri, String history,boolean showInfo_or_sort,ArrayList<String> urilist) {
        showTagFragment f = new showTagFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
//        args.putString("time", time);
//        args.putString("name",name);
        args.putString("uri",uri.toString());
        args.putBoolean("showInfo",showInfo_or_sort);
//        args.putStringArrayList("history",history);
        if(!showInfo_or_sort){
            args.putStringArrayList("urilist",urilist);
        }
        f.setArguments(args);


        return f;
    }
    @NonNull
    @Override
    //generate dialog instance
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

//        String time_list = getArguments().getString("time").split(" ")[0];
//        int year = Integer.parseInt(time_list.split(":")[0]);
//        int month = Integer.parseInt(time_list.split(":")[1]);
//        int day = Integer.parseInt(time_list.split(":")[2]);


//        String name = getArguments().getString("name");

        String uri = getArguments().getString("uri");
        boolean showInfo = getArguments().getBoolean("showInfo");
        ArrayList<String> urilist = getArguments().getStringArrayList("urilist");
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
        View show_filter_sort = inflater.inflate(R.layout.sort_filter_layout,null);
        View show_tag = inflater.inflate(R.layout.fragment_showtag, null);
        ((TextView)show_tag.findViewById(R.id.nameTag)).setText(show_name);
        ((TextView)show_tag.findViewById(R.id.timeTag)).setText(show_time);
        ((TextView)show_tag.findViewById(R.id.locationTag)).setText(show_location);
        ((TextView)show_tag.findViewById(R.id.personTag)).setText(show_people);
        ((TextView)show_tag.findViewById(R.id.uriTag)).setText(uri);
//        View tag_view = inflater.inflate(R.layout.fragment_tagform, null);
//        EditText editName = (EditText) tag_view.findViewById(R.id.editName);
//        EditText editView = (EditText) tag_view.findViewById(R.id.editField);
//        AutoCompleteTextView editPerson = tag_view.findViewById(R.id.edit_person);
//        Spinner dropdown = tag_view.findViewById(R.id.spinnerplace);
//        Button add = tag_view.findViewById(R.id.addButton);
//        for(int i = 1; i < show_people.split(";").length;i++){
//            count += 1;
//            LinearLayout layout = (LinearLayout) tag_view.findViewById(R.id.linear_layout);
//            AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getContext());
//            layout.addView(autoCompleteTextView);
//            autoCompleteTextView.setGravity(Gravity.TOP);
//
//            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) autoCompleteTextView.getLayoutParams();
//            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//            layoutParams.setMargins(20,20,0,0);
//
//            autoCompleteTextView.setLayoutParams(layoutParams);
//            autoCompleteTextView.setTag("edit_person"+String.valueOf(i));
//            try{
//                autoCompleteTextView.setText(show_people.split(";")[i]);
//            }catch(ArrayIndexOutOfBoundsException e){
//                e.printStackTrace();
//            }
//        }
//
//
//
//        if(!show_name.equals("")){
//            editName.setText(show_name);
//        }
//        else if(name != null){
//            editName.setText(name);
//        }
//        if(!show_time.equals("")){
//            editView.setText(show_time);
//        }else if(time_list != null){
//            editView.setText(String.join("-",time_list.split(":")));
//        }
////        editView.setClickable(false);
//        editView.setFocusable(false);
//        DatePickerDialog date_view = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//                String dateTime = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
//                editView.setText(dateTime);
//            }
//
//        }, year, month-1, day);
//
//
//        //create a list of items for the spinner.
//        String[] items = getActivity().getApplication().getResources().getStringArray(R.array.district_name);
//        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//        //There are multiple variations of this, but this is the basic variant.
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
//        //set the spinners adapter to the previously created one.
//        dropdown.setAdapter(adapter);
//        String[] districts = getActivity().getApplication().getResources().getStringArray(R.array.position);
//        if( Arrays.asList(items).contains(show_location)){
//
//            dropdown.setSelection(Arrays.asList(items).indexOf(show_location));
//        }
//
//        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                // your code here
//
//                TextView txtView = (TextView)selectedItemView;
//                String district_name = (String) txtView.getText();
////                Toast.makeText(getActivity(),"selected: district: "+district_name +districts[position],Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // your code here
//            }
//
//        });
//        tag_view.findViewById(R.id.dateButton).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                date_view.show();
//            }
//        });
//
//        editPerson.setText(show_people.split(";")[0]);
////        editPerson.setLines(3);
//
//        String[] al = {"a","b","c"};
//
//        editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));
//
//        editPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                editPerson.showDropDown();
//                String selection = (String) parent.getItemAtPosition(position);
//                Toast.makeText(getActivity(), selection,
//                        Toast.LENGTH_SHORT);
////                delButton.setAlpha(.8f);
//            }
//        });
//
//        editPerson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View arg0) {
//                editPerson.showDropDown();
//            }
//        });
//
//        String finalShow_people = show_people;
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                count += 1;
//                LinearLayout layout = (LinearLayout) tag_view.findViewById(R.id.linear_layout);
//                AutoCompleteTextView autoCompleteTextView = new AutoCompleteTextView(getContext());
//                layout.addView(autoCompleteTextView);
//                autoCompleteTextView.setGravity(Gravity.TOP);
//
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) autoCompleteTextView.getLayoutParams();
//                layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//                layoutParams.setMargins(20,20,0,0);
//
//                autoCompleteTextView.setLayoutParams(layoutParams);
//                autoCompleteTextView.setTag("edit_person"+String.valueOf(count));
//
//
//
//            }
//        });
////        editPerson.addTextChangedListener(new TextWatcher() {
////            @Override
////            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));
////                for(String history : histories){
////                    Toast.makeText(getActivity(),"before "+history,Toast.LENGTH_SHORT).show();
////                    editPerson.setHint(history);
////                }
////            }
////
////            @Override
////            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
////                editPerson.showDropDown();
////                editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));
////                for(String history : histories){
////                    Toast.makeText(getActivity(),"on "+history,Toast.LENGTH_SHORT).show();
////                    editPerson.setHint(history);
////                }
////            }
////
////            @Override
////            public void afterTextChanged(Editable editable) {
////
////                editPerson.setAdapter(new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, histories));
////                for(String history : histories){
////                    Toast.makeText(getActivity(),"after "+history,Toast.LENGTH_SHORT).show();
////                    editPerson.setHint(history);
////                }
////            }
////        });
////        editPerson.setOnFocusChangeListener(new View.OnFocusChangeListener(){
////            @Override
////            public void onFocusChange(View view, boolean b) {
////                if(b){
////
////                    for(String history : histories){
////                        Toast.makeText(getActivity(),history,Toast.LENGTH_SHORT).show();
////                    }
////                }else{
////                    Toast.makeText(getActivity(),"hehe xd",Toast.LENGTH_SHORT).show();;
////                }
////
////            }
////
////
////        });
////        editPerson.setOnClickListener(new View.OnClickListener(){
////            @Override
////            public void onClick(View view) {
////                for(String history : histories){
////                    Toast.makeText(getActivity(),history,Toast.LENGTH_SHORT).show();
////                }
////
////            }
////        });
        Spinner sort = show_filter_sort.findViewById(R.id.spinnerSort);
        String[] items = {"time","place","people","none"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        sort.setAdapter(adapter);
        sort.setSelection(3);
        //sorting algo
        ViewGroup rootView = (ViewGroup)inflater
                .inflate(R.layout.layout_detailgallery, null);
        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                TextView txtView = (TextView)selectedItemView;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:mm:dd");
                String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                File picture_info = new File(csv.concat("/"+ Environment.DIRECTORY_DOCUMENTS), "detail.csv");
                ArrayList<String> stringArray = new ArrayList<String>();
                BufferedReader file = null;
                try {
                    file = new BufferedReader(new FileReader(picture_info));

                    String line;
                    boolean replace = false;
                    while ((line = file.readLine()) != null) {
                        stringArray.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error getting data from external storage",Toast.LENGTH_SHORT).show();
                }
                FragmentTransaction transaction;
//                BlankFragment BlankFragment;
                Bundle bundle;
                ArrayList<String> time_list = new ArrayList<String>();
                ArrayList<String> selected_row = new ArrayList<String>();
                ArrayList<String> locations = new ArrayList<String>();
                ArrayList<String> people_list = new ArrayList<String>();

                if(urilist == null){
                    return;
                }
                for(String uri : urilist){
                    String name = uri.split("/")[(uri.split("/")).length-1];
                    for(String string : stringArray){
                        if(stringArray.indexOf(string) == 0 || string.equals("\n")){
                            continue;
                        }
//                        Toast.makeText(getApplicationContext(),name+" "+string.split(",")[0],Toast.LENGTH_SHORT).show();
//                                String lower_string = string.split(",")[2].toLowerCase();
                        if(string.split(",")[5].contains(uri)){
                            selected_row.add(string);
                            time_list.add(String.join(":",string.split(",")[1].substring(1,string.split(",")[1].length()-1).split("-")));
                            locations.add(string.split(",")[2].toLowerCase());
//                            Toast.makeText(getApplicationContext(),string.split(",")[2].toLowerCase(),Toast.LENGTH_SHORT).show();
                            people_list.add(string.split(",")[4].toLowerCase());
//                            uri_gen_list.add(string.split(",")[5].substring(1,string.split(",")[5].length()-1));
                        }
                    }
                }
                switch (txtView.getText().toString()){
                    case "time":
                        if(urilist == null){
                            return;
                        }else{
                            int year = 0;
                            int month = 0;
                            int day = 0;
                            String previous_time = "";
                            String time = "";

                            for(int i = 0; i < urilist.size()-1; i++){
                                int min_idx = i;
//                                if(urilist[i] == null){
//                                    continue;
//                                }

//                                try (InputStream inputStream = getContentResolver().openInputStream(Uri.parse(urilist[i]))) {
//                                    ExifInterface exif = new ExifInterface(inputStream);
//                                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                                    time = exif.getAttribute(ExifInterface.TAG_DATETIME).split(" ")[0];
//                                    String lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//                                    TextView textview = (TextView)findViewById(R.id.txt_hello);
////                                    textview.setText(time + lat);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                                for (int j = i+1; j < urilist.size(); j++) {
                                    String temp_time = "";
//                                    try (InputStream inputStream = getContentResolver().openInputStream(Uri.parse(urilist[j]))) {
//                                        ExifInterface exif = new ExifInterface(inputStream);
//                                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                                        temp_time = exif.getAttribute(ExifInterface.TAG_DATETIME).split(" ")[0];
//                                        String lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//                                        TextView textview = (TextView) findViewById(R.id.txt_hello);
////                                        textview.setText(time + lat);
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
                                    try{
                                        try{
                                            if (sdf.parse(time_list.get(i)).getTime()-sdf.parse(time_list.get(j)).getTime() > 0){
                                                min_idx = j;
                                            }
                                            continue;
                                        }catch (IndexOutOfBoundsException e){
                                            e.printStackTrace();
                                        }
//                                        if (sdf.parse(time).getTime()-sdf.parse(temp_time).getTime() > 0){
//                                            min_idx = j;
//                                        }
                                    }catch(ParseException e){
                                        e.printStackTrace();
                                    }

//                                    if (arr[j] < arr[min_idx])
//                                        min_idx = j;
                                }
//                                if(i > 0 && time != null){
//                                    try {
//
//
//                                        Date d1 = sdf.parse(time);
//                                        Date d2 = sdf.parse(previous_time);
//                                        long elapsed = d2.getTime() - d1.getTime();
//                                        if(elapsed > 0){
//                                            urilist[]
//                                        }
//                                    }catch(ParseException e){
//                                        e.printStackTrace();
//                                    }
//                                    previous_time = time;
//                                }

                                String temp = urilist.get(min_idx);
                                urilist.set(min_idx, urilist.get(i));
                                urilist.set(i, temp);



                            }
                        }

                        break;
                    case "place":

                        if (stringArray.size() == 0){
                            break;
                        }


                        int count = urilist.size();
////                        String[] str = new String[count];
////                        int k = 0;
//                        for(String row : locations){
//                            String lowercase_location = row.toLowerCase();
//
//                            str[k] = lower_string;
//                            k += 1;
//                        }
                        for (int i = 0; i < count-1; i++)
                        {
                            for (int j = i + 1; j < count; j++)
                            {
                                if (locations.get(i).compareTo(locations.get(j)) > 0)
                                {
//                                    String temp = urilist[i];
//                                    urilist[i] = urilist[j];
//                                    urilist[j] = temp;

                                    String temp = urilist.get(i);
                                    urilist.set(i, urilist.get(j));
                                    urilist.set(j, temp);
                                }
                            }
                        }


                        break;
                    case "people":
                        if (stringArray.size() == 0){
                            break;
                        }


                        count = urilist.size();
////                        String[] str = new String[count];
////                        int k = 0;
//                        for(String row : locations){
//                            String lowercase_location = row.toLowerCase();
//
//                            str[k] = lower_string;
//                            k += 1;
//                        }
                        for (int i = 0; i < count-1; i++)
                        {
                            for (int j = i + 1; j < count; j++)
                            {
//                                if (people_list.get(i).compareTo(people_list.get(j)) > 0)
//                                {
                                if(people_list.get(i).split(";").length > people_list.get(j).split(";").length){
//                                    String temp = urilist[i];
//                                    urilist[i] = urilist[j];
//                                    urilist[j] = temp;
                                    String temp = urilist.get(i);
                                    urilist.set(i, urilist.get(j));
                                    urilist.set(j, temp);
                                }
                            }
                        }


                        break;
                    case"none":
                        break;
                }
//                transaction =
//                        getSupportFragmentManager().beginTransaction();



//                NavHostFragment navHostFragment = (NavHostFragment) getParentFragment();
//                Fragment parent = (Fragment) navHostFragment.getParentFragment();
//                parent.getView().findViewById(R.id.element_id);




//                GridView gallery = paretView;
//                gallery.setAdapter(mImageAdaptor);
//              mImageAdaptor = new ImageAdapter(getActivity(), urilist);
//                ListAdapter adapter = gallery.getAdapter();


//                BlankFragment = new BlankFragment();
//                transaction.replace(R.id.list_fragment, BlankFragment);
//                bundle = new Bundle();
////                        Toast.makeText(getApplicationContext(),urilist.toString()+" "+uri_gen_list.toString(),Toast.LENGTH_LONG).show();;
////                        bundle.putStringArray("list", urilist);
//                bundle.putStringArray("list", uri_gen_list.toArray(new String[0]));
//                // set Fragmentclass Arguments
//                BlankFragment.setArguments(bundle);
//                transaction.commit();
//                uri_gen_list = new ArrayList<String>(Arrays.asList(urilist));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }



        });

        //set radio group of flitering
        RadioGroup radioGroup = (RadioGroup) show_filter_sort.findViewById(R.id.radioGroup);
        Button btn1 = show_filter_sort.findViewById(R.id.btn_selected_date_1);
        Button btn2 = show_filter_sort.findViewById(R.id.btn_selected_date_2);
        EditText txt1 = show_filter_sort.findViewById(R.id.txt_date_1);
        EditText txt2 = show_filter_sort.findViewById(R.id.txt_date_2);
        EditText txt3 = show_filter_sort.findViewById(R.id.txt_person);
        final Spinner spinner = show_filter_sort.findViewById(R.id.spinnerplace_filter);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                group.check(checkedId);
                if(checkedId == R.id.radio_time){
                    btn2.setVisibility(View.INVISIBLE);
                    btn2.setClickable(false);

                    spinner.setVisibility(View.INVISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn1.setClickable(true);
                    txt1.setVisibility(View.VISIBLE);
                    txt2.setVisibility(View.VISIBLE);
                    txt1.setFocusable(false);
                    txt2.setFocusable(false);
                    txt3.setVisibility(View.INVISIBLE);
                }else if (checkedId == R.id.radio_location){
                    btn2.setVisibility(View.VISIBLE);
                    btn2.setClickable(true);
                    btn1.setVisibility(View.INVISIBLE);
                    btn1.setClickable(false);
                    txt1.setVisibility(View.INVISIBLE);
                    txt2.setVisibility(View.INVISIBLE);
                    txt1.setFocusable(false);
                    txt2.setFocusable(false);
                    txt3.setVisibility(View.INVISIBLE);

                    spinner.setVisibility(View.VISIBLE);

                    String[] items = getActivity().getApplication().getResources().getStringArray(R.array.district_name);
                    //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                    //There are multiple variations of this, but this is the basic variant.
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                    //set the spinners adapter to the previously created one.
                    spinner.setAdapter(adapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            // your code here

                            TextView txtView = (TextView) selectedItemView;
                            String district_name = (String) txtView.getText();
//                Toast.makeText(getActivity(),"selected: district: "+district_name +districts[position],Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }
                    });
                }else if (checkedId == R.id.radio_person){
                    btn1.setVisibility(View.INVISIBLE);
                    btn1.setClickable(false);
                    txt1.setVisibility(View.INVISIBLE);
                    txt2.setVisibility(View.INVISIBLE);
                    txt1.setFocusable(false);
                    txt2.setFocusable(false);
                    btn2.setVisibility(View.VISIBLE);
                    btn2.setClickable(true);
                    txt3.setVisibility(View.VISIBLE);
                    txt3.setClickable(true);
                    spinner.setVisibility(View.INVISIBLE);
                }else{
                    btn1.setVisibility(View.INVISIBLE);
                    txt1.setVisibility(View.INVISIBLE);
                    txt2.setVisibility(View.INVISIBLE);
                    txt3.setVisibility(View.INVISIBLE);
                    btn2.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.INVISIBLE);
                }

            }
        });
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        int year = Integer.parseInt(formatter.format(date).split("-")[2]);
        int month = Integer.parseInt(formatter.format(date).split("-")[1]);
        int day = Integer.parseInt(formatter.format(date).split("-")[0]);
        DatePickerDialog date_view = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String dateTime = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(day);
                if(btn1.getText().toString().equals("From")){
                    txt1.setText(dateTime);
                    btn1.setText("To");
                    if(!txt2.getText().toString().equals("")){
                        txt2.setText("");
                    }

                }else if (btn1.getText().toString().equals("To")){
                    txt2.setText(dateTime);
                    btn1.setText("From");
                }

            }

        }, year, month-1, day);

        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                date_view.show();

            }
        });
        txt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn2.setVisibility(View.VISIBLE);
                btn2.setClickable(true);
            }
        });
        //flitering algo
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy:mm:dd");
                String csv = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                File picture_info = new File(csv.concat("/"+ Environment.DIRECTORY_DOCUMENTS), "detail.csv");
                ArrayList<String> stringArray = new ArrayList<String>();
                BufferedReader file = null;
                try {
                    file = new BufferedReader(new FileReader(picture_info));

                    String line;
                    boolean replace = false;
                    while ((line = file.readLine()) != null) {
                        stringArray.add(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error getting data from external storage",Toast.LENGTH_SHORT).show();
                }
                FragmentTransaction transaction;
//                BlankFragment BlankFragment;
                Bundle bundle;
                ArrayList<String> time_list = new ArrayList<String>();
                ArrayList<String> selected_row = new ArrayList<String>();
                ArrayList<String> locations = new ArrayList<String>();
                ArrayList<String> people_list = new ArrayList<String>();

                if(urilist == null){
                    return;
                }
                for(String uri : urilist){
                    String name = uri.split("/")[(uri.split("/")).length-1];
                    for(String string : stringArray){
                        if(stringArray.indexOf(string) == 0 || string.equals("\n")){
                            continue;
                        }
//                        Toast.makeText(getApplicationContext(),name+" "+string.split(",")[0],Toast.LENGTH_SHORT).show();
//                                String lower_string = string.split(",")[2].toLowerCase();
                        if(string.split(",")[5].contains(uri)){
                            selected_row.add(string);
                            time_list.add(String.join(":",string.split(",")[1].substring(1,string.split(",")[1].length()-1).split("-")));
                            locations.add(string.split(",")[2]);
//                            Toast.makeText(getApplicationContext(),string.split(",")[2].toLowerCase(),Toast.LENGTH_SHORT).show();
                            people_list.add(string.split(",")[4].toLowerCase());
//                            uri_gen_list.add(string.split(",")[5].substring(1,string.split(",")[5].length()-1));
                        }
                    }
                }
                ArrayList<String> uri_filtered_list = new ArrayList<String>();
                switch(radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_time:
                        String time_down_bound = txt1.getText().toString();
                        String time_up_bound = txt2.getText().toString();
                        for(int i = 0; i < urilist.size();i++){
                            try {
                                if(sdf.parse(time_list.get(i)).getTime() >= sdf.parse(time_down_bound).getTime() && sdf.parse(time_up_bound).getTime() >= sdf.parse(time_list.get(i)).getTime()){
                                    uri_filtered_list.add(urilist.get(i));
                                }else{

                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
//                        urilist = (ArrayList<String>) uri_filtered_list.clone();
                        break;
                    case R.id.radio_location:
                        String loc_bound = spinner.getSelectedItem().toString();
                        ;
                        int count = urilist.size();
                        for(int i = 0; i < count;i++){

                            if(locations.get(i).contains(loc_bound)){
                                uri_filtered_list.add(urilist.get(i));
                            }
                        }
//                        urilist = (ArrayList<String>) uri_filtered_list.clone();
                        break;
                    case R.id.radio_person:
                        count = urilist.size();
                        String ppl_bound = txt3.getText().toString();
                        for(int i = 0; i < count;i++){
                            if(people_list.get(i).contains(ppl_bound)){
                                uri_filtered_list.add(urilist.get(i));
                            }
                        }
//                        urilist = (ArrayList<String>) uri_filtered_list.clone();
                        break;
                    case R.id.radio_none:

//                        urilist = new ArrayList<String>(Arrays.asList(urilist));
//                        uri_filtered_list = (ArrayList<String>) urilist.clone();
                        break;
                }





            }


        });
        // set dialog with on ok listener
        return new AlertDialog.Builder(requireContext())
                .setMessage("Tag Form")
//                .setView(R.id.editField)
                .setView(showInfo ? show_tag : show_filter_sort)
                .setPositiveButton("ok", (dialog, which) -> {
                    ImageAdapter mImageAdaptor = new ImageAdapter(getContext(), urilist,true);
                    GridView gridview = rootView.findViewById(R.id.gridview);
                    gridview.setAdapter(mImageAdaptor);
//                    String name_out = editName.getText().toString();
//                    String time_out = editView.getText().toString();
//                    String place_out = dropdown.getSelectedItem().toString();
//                    String lat_long_out = districts[dropdown.getSelectedItemPosition()];
//                    String person_out = editPerson.getText().toString();
//                    for(int i = 1; i <= count;i++){
//                        person_out += ";"+((TextView)tag_view.findViewWithTag("edit_person"+String.valueOf(i))).getText().toString();
//                    }
////                    person_out = person_out.substring(0,person_out.length()-1);
//
//                    String uri_out = uri;
//                    Toast.makeText(getActivity(),person_out,Toast.LENGTH_SHORT).show();
//                    String data_out = "'"+name_out + "','" + time_out + "','"+ place_out + "','" + lat_long_out + "','"+person_out+"','"+uri_out+"'";
//                    String csv = Environment.getExternalStorageDirectory().getAbsolutePath();
//
////                    Toast.makeText(getActivity(),csv,Toast.LENGTH_SHORT).show();
////                    CSVWriter writer = new CSVWriter(new FileWriter(csv));
////                    File root = Environment.getExternalStorageDirectory();
//                    File picture_info = new File(csv.concat("/"+Environment.DIRECTORY_DOCUMENTS), "detail.csv");
//                    picture_info.setWritable(true);
//
//                    try {
//
////                        Toast.makeText(getActivity(), Boolean.toString(picture_info.exists()),Toast.LENGTH_SHORT).show();
//                        if(picture_info.exists()){
//                            BufferedReader file = new BufferedReader(new FileReader(picture_info));
//                            StringBuffer inputBuffer = new StringBuffer();
//                            ArrayList<String> stringArray = new ArrayList<String>();
//                            String line;
//                            boolean replace = false;
//                            while ((line = file.readLine()) != null) {
//
//                                if(line.contains(uri_out)){
//                                    replace = true;
//                                    inputBuffer.append(data_out);
//                                    stringArray.add(data_out);
//
//                                }else{
//
//                                    inputBuffer.append(line);
//                                    stringArray.add(line);
//                                }
//                                inputBuffer.append('\n');
////                                stringArray.add("\n");
//                            }
//                            file.close();
////                            Toast.makeText(getActivity(),Boolean.toString(replace),Toast.LENGTH_SHORT).show();;
//                            if(!replace){
//                                inputBuffer.append(data_out);
//                                stringArray.add(data_out);
//                            }
////                            Toast.makeText(getActivity(),stringArray,Toast.LENGTH_SHORT).show();
//                            BufferedWriter writer = new BufferedWriter(new FileWriter(picture_info));
//                            writer.write("Name,Time,Place,Latitude,Person,uri");
//                            writer.write("\n");
//                            for(String string :stringArray){
//
//                                if(string.equals("\n") || string.equals("Name,Time,Place,Latitude,Person,uri") || string.equals("")){
//                                    continue;
//                                }else{
//
//                                    writer.write(string);
//                                    writer.write("\n");
//
//
//                                }
//
//                            }
////                            writer.write(data_out);
////                            writer.newLine();
//                            writer.flush();
//                            writer.close();
//
//                        }else{
//                            BufferedWriter writer = new BufferedWriter(new FileWriter(picture_info, true));
//                            writer.write("Name,Time,Place,Latitude,Person,uri");
//                            writer.newLine();
//                            writer.write(data_out);
//                            writer.newLine();
//                            writer.flush();
//                            writer.close();
//                        }
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//
////                    editPerson.setAdapter();
//
//                    ((MainActivity)getActivity()).setDate_in_EditText(person_out);
                } )
                .create();
    }

    public static String TAG = "PurchaseConfirmationDialog";
}
