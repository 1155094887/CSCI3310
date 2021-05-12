package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.provider.MediaStore;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    // params
    private static final int SELECT_SINGLE_PICTURE = 101;
    private static final int SELECT_MULTIPLE_PICTURE = 201;
    public static final String IMAGE_TYPE = "image/*";
    private ImageView selectedImagePreview;
    String[] urilist;
    ArrayList<String> histories = new ArrayList<String>();
    ArrayList<String> uri_gen_list = new ArrayList<String>();

    // all functional algo of sorting and flitering and getting photos
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // single image selection
        Context context = this;
        findViewById(R.id.btn_pick_single_image).setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
//                Intent intent = new Intent();
//                intent.setType(IMAGE_TYPE);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);


            }
        });
        findViewById(R.id.click_to_show_tag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT,Uri.parse(""),context, GalleryActivity.class);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                intent.addCategory(Intent.ACTION_OPEN_DOCUMENT);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra("urilist",urilist);
//                intent.set(Intent.ACTION_OPEN_DOCUMENT);
                context.startActivity(intent);
            }
        });
        // multiple image selection
        findViewById(R.id.btn_pick_multiple_images).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // this line is different here !!
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,
                        getString(R.string.select_picture)), SELECT_MULTIPLE_PICTURE);
            }
        });
        // call form
        findViewById(R.id.image_preview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                // in onCreate or any event where your want the user to
//                // select a file
//                Intent intent = new Intent();
//                intent.setType(IMAGE_TYPE);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);
                ImageView v = (ImageView) view;

                Uri uri = (Uri) v.getTag();
                if(uri == null){
                    return;
                }
                String time = "";
                String lat;
                try (InputStream inputStream = getContentResolver().openInputStream(uri)) {
                    ExifInterface exif = new ExifInterface(inputStream);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    time = exif.getAttribute(ExifInterface.TAG_DATETIME);
                    lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    TextView textview = (TextView)findViewById(R.id.txt_hello);
                    textview.setText(time + lat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                try  {
//                    ImageView v = (ImageView) view;
//                    Drawable d = v.getDrawable();
//                    BitmapDrawable bitDw = ((BitmapDrawable) d);
//                    Bitmap bitmap = bitDw.getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] imageInByte = stream.toByteArray();
//                    System.out.println("........length......"+imageInByte);
//                    ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
//                    InputStream inputStream = (InputStream) bis;
//                    ExifInterface exif = new ExifInterface(inputStream);
//                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                    String time = exif.getAttribute(ExifInterface.TAG_DATETIME);
//                    String lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//                    TextView textview = (TextView)findViewById(R.id.txt_hello);
//                    textview.setText(time + lat);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                String name = uri.toString().split("/")[(uri.toString().split("/")).length-1];
                ArrayList<String> history_cpy = (ArrayList<String>) histories.clone();
                Collections.reverse(history_cpy);
                TagformFragment fragment = TagformFragment.newInistance(time,name,uri.toString(), history_cpy);
                fragment.show(
                    getSupportFragmentManager(), TagformFragment.TAG);
                }
        });
        // set sorting spinner
        selectedImagePreview = (ImageView)findViewById(R.id.image_preview);
        Spinner sort = findViewById(R.id.spinnerSort);
        String[] items = {"time","place","people","none"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        sort.setAdapter(adapter);
        sort.setSelection(3);
        //sorting algo
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
                    Toast.makeText(getApplicationContext(),"Error getting data from external storage",Toast.LENGTH_SHORT).show();
                }
                FragmentTransaction transaction;
                BlankFragment BlankFragment;
                Bundle bundle;
                ArrayList<String> time_list = new ArrayList<String>();
                ArrayList<String> selected_row = new ArrayList<String>();
                ArrayList<String> locations = new ArrayList<String>();
                ArrayList<String> people_list = new ArrayList<String>();

                if(urilist == null){
                    return;
                }
                for(String uri : uri_gen_list){
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
                        if(uri_gen_list == null){
                            return;
                        }else{
                            int year = 0;
                            int month = 0;
                            int day = 0;
                            String previous_time = "";
                            String time = "";

                            for(int i = 0; i < uri_gen_list.size()-1; i++){
                                int min_idx = i;
//                                if(urilist[i] == null){
//                                    continue;
//                                }

                                try (InputStream inputStream = getContentResolver().openInputStream(Uri.parse(urilist[i]))) {
                                    ExifInterface exif = new ExifInterface(inputStream);
                                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                    time = exif.getAttribute(ExifInterface.TAG_DATETIME).split(" ")[0];
                                    String lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                                    TextView textview = (TextView)findViewById(R.id.txt_hello);
//                                    textview.setText(time + lat);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                for (int j = i+1; j < uri_gen_list.size(); j++) {
                                    String temp_time = "";
                                    try (InputStream inputStream = getContentResolver().openInputStream(Uri.parse(urilist[j]))) {
                                        ExifInterface exif = new ExifInterface(inputStream);
                                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                                        temp_time = exif.getAttribute(ExifInterface.TAG_DATETIME).split(" ")[0];
                                        String lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                                        TextView textview = (TextView) findViewById(R.id.txt_hello);
//                                        textview.setText(time + lat);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try{
                                        try{
                                            if (sdf.parse(time_list.get(i)).getTime()-sdf.parse(time_list.get(j)).getTime() > 0){
                                                min_idx = j;
                                            }
                                            continue;
                                        }catch (IndexOutOfBoundsException e){
                                            e.printStackTrace();
                                        }
                                        if (sdf.parse(time).getTime()-sdf.parse(temp_time).getTime() > 0){
                                            min_idx = j;
                                        }
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

                                String temp = uri_gen_list.get(min_idx);
                                uri_gen_list.set(min_idx, uri_gen_list.get(i));
                                uri_gen_list.set(i, temp);



                            }
                        }

                        break;
                    case "place":

                        if (stringArray.size() == 0){
                            break;
                        }


                        int count = uri_gen_list.size();
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

                                    String temp = uri_gen_list.get(i);
                                    uri_gen_list.set(i, uri_gen_list.get(j));
                                    uri_gen_list.set(j, temp);
                                }
                            }
                        }


                        break;
                    case "people":
                        if (stringArray.size() == 0){
                            break;
                        }


                        count = uri_gen_list.size();
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
                                    String temp = uri_gen_list.get(i);
                                    uri_gen_list.set(i, uri_gen_list.get(j));
                                    uri_gen_list.set(j, temp);
                                }
                            }
                        }


                        break;
                    case"none":
                        break;
                }
                transaction =
                        getSupportFragmentManager().beginTransaction();

                BlankFragment = new BlankFragment();
                transaction.replace(R.id.list_fragment, BlankFragment);
                bundle = new Bundle();
//                        Toast.makeText(getApplicationContext(),urilist.toString()+" "+uri_gen_list.toString(),Toast.LENGTH_LONG).show();;
//                        bundle.putStringArray("list", urilist);
                bundle.putStringArray("list", uri_gen_list.toArray(new String[0]));
                // set Fragmentclass Arguments
                BlankFragment.setArguments(bundle);
                transaction.commit();
//                uri_gen_list = new ArrayList<String>(Arrays.asList(urilist));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }



        });

        //set radio group of flitering
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        Button btn1 = findViewById(R.id.btn_selected_date_1);
        Button btn2 = findViewById(R.id.btn_selected_date_2);
        EditText txt1 = findViewById(R.id.txt_date_1);
        EditText txt2 = findViewById(R.id.txt_date_2);
        EditText txt3 = findViewById(R.id.txt_person);
        final Spinner spinner = findViewById(R.id.spinnerplace_filter);
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

                    String[] items = getApplication().getResources().getStringArray(R.array.district_name);
                    //create an adapter to describe how the items are displayed, adapters are used in several places in android.
                    //There are multiple variations of this, but this is the basic variant.
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, items);
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
        DatePickerDialog date_view = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
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
                    Toast.makeText(getApplicationContext(),"Error getting data from external storage",Toast.LENGTH_SHORT).show();
                }
                FragmentTransaction transaction;
                BlankFragment BlankFragment;
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
                        if(string.split(",")[0].contains(name)){
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
                        for(int i = 0; i < urilist.length;i++){
                            try {
                                if(sdf.parse(time_list.get(i)).getTime() >= sdf.parse(time_down_bound).getTime() && sdf.parse(time_up_bound).getTime() >= sdf.parse(time_list.get(i)).getTime()){
                                    uri_filtered_list.add(urilist[i]);
                                }else{

                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        uri_gen_list = (ArrayList<String>) uri_filtered_list.clone();
                        break;
                    case R.id.radio_location:
                        String loc_bound = spinner.getSelectedItem().toString();
                        ;
                        int count = urilist.length;
                        for(int i = 0; i < count;i++){

                           if(locations.get(i).contains(loc_bound)){
                               uri_filtered_list.add(urilist[i]);
                           }
                        }
                        uri_gen_list = (ArrayList<String>) uri_filtered_list.clone();
                        break;
                    case R.id.radio_person:
                        count = urilist.length;
                        String ppl_bound = txt3.getText().toString();
                        for(int i = 0; i < count;i++){
                            if(people_list.get(i).contains(ppl_bound)){
                                uri_filtered_list.add(urilist[i]);
                            }
                        }
                        uri_gen_list = (ArrayList<String>) uri_filtered_list.clone();
                        break;
                    case R.id.radio_none:

                        uri_gen_list = new ArrayList<String>(Arrays.asList(urilist));
                        uri_filtered_list = (ArrayList<String>) uri_gen_list.clone();
                        break;
                }
                transaction =
                        getSupportFragmentManager().beginTransaction();

                BlankFragment = new BlankFragment();
                transaction.replace(R.id.list_fragment, BlankFragment);
                bundle = new Bundle();
                bundle.putStringArray("list", uri_filtered_list.toArray(new String[0]));
//                        bundle.putStringArray("list", urilist);
                // set Fragmentclass Arguments
                BlankFragment.setArguments(bundle);
                transaction.commit();


            }


        });
    }

//    @Override
//    protected void onStart(){
//        super.onStart();
//
//    }
    // handle request response
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_SINGLE_PICTURE) {
                boolean yes = Intent.ACTION_SEND_MULTIPLE.equals(data.getAction());
                boolean yes2 = data.hasExtra(Intent.EXTRA_STREAM);
                String xd = data.getType();
                Uri selectedImageUri = data.getData();
                Uri uri = data.getData();

                Toast.makeText(this,uri+Boolean.toString(yes)+Boolean.toString(yes2),Toast.LENGTH_LONG).show();

                try (InputStream inputStream = this.getContentResolver().openInputStream(uri)) {
                    ExifInterface exif = new ExifInterface(inputStream);
                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    String time = exif.getAttribute(ExifInterface.TAG_DATETIME);
                    String lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
                    TextView textview = (TextView)findViewById(R.id.txt_hello);
                    textview.setText(time + lat);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                ExifInterface exif = new ExifInterface(pathToTheImage);
//                String datetime =  exif.getAttribute(ExifInterface.TAG_DATETIME);
                try {
                    selectedImagePreview.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
                } catch (IOException e) {
                    Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
                }
                // original code
//                String selectedImagePath = getPath(selectedImageUri);
//                selectedImagePreview.setImageURI(selectedImageUri);
            }
            else if (requestCode == SELECT_MULTIPLE_PICTURE) {
                //And in the Result handling check for that parameter:
                boolean yes = Intent.ACTION_SEND_MULTIPLE.equals(data.getAction());
                boolean yes2 = data.hasExtra(Intent.EXTRA_STREAM);
                try {
                    ClipData dataclip = data.getClipData();
                    int count = dataclip.getItemCount();
                    urilist = new String[count];
                    if (dataclip != null) {
                        int i = 0;
                        for (i = 0; i < dataclip.getItemCount(); i++) {
//                        Uri uri = (Uri) parcel;
                            Uri uri = dataclip.getItemAt(i).getUri();
                            urilist[i] = uri.toString();

                            // handle the images one by one here
                        }
                    }
                    Uri urilen = dataclip.getItemAt(0).getUri();
//                String xd = data.getParcelableArrayExtra();
                    Toast.makeText(this, Integer.toString(count) + " " + Boolean.toString(yes) + Boolean.toString(yes2), Toast.LENGTH_SHORT).show();


                    FragmentTransaction transaction =
                            getSupportFragmentManager().beginTransaction();

                    BlankFragment BlankFragment = new BlankFragment();
                    transaction.replace(R.id.list_fragment, BlankFragment);
                    Bundle bundle = new Bundle();

                    bundle.putStringArray("list", urilist);
                    // set Fragmentclass Arguments
                    BlankFragment.setArguments(bundle);
                    transaction.commit();

                    uri_gen_list = new ArrayList<String>(Arrays.asList(urilist));
                    if ((Intent.ACTION_SEND_MULTIPLE.equals(data.getAction())
                            && data.hasExtra(Intent.EXTRA_STREAM))) {
                        // retrieve a collection of selected images


                        ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                        // iterate over these images
                        urilist = new String[20];
                        if (list != null) {
                            int i = 0;
                            for (Parcelable parcel : list) {
                                Uri uri = (Uri) parcel;
                                urilist[i] = uri.toString();
                                i += 1;
                                // handle the images one by one here
                            }
                        }

                        // for now just show the last picture
                        if (!list.isEmpty()) {
                            Uri imageUri = (Uri) list.get(list.size() - 1);

                            try {
                                selectedImagePreview.setImageBitmap(new UserPicture(imageUri, getContentResolver()).getBitmap());
                            } catch (IOException e) {
                                Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
                            }
                            // original code
//                        String selectedImagePath = getPath(imageUri);
//                        selectedImagePreview.setImageURI(imageUri);
//                        displayPicture(selectedImagePath, selectedImagePreview);
                        }
//                    GridView gallery = (GridView) findViewById(R.id.gridview);
//                    ImageAdaptor mImageAdaptor = new ImageAdaptor(this, logo, urilist);
//                    gallery.setAdapter(mImageAdaptor);

//                    BlankFragment BlankFragment = new BlankFragment();
//                    FragmentTransaction transaction =
//                            getSupportFragmentManager().beginTransaction();
////                    transaction.replace(R.id.list_fragment,BlankFragment);
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList("lst", list);
//                    // set Fragmentclass Arguments
//                    BlankFragment.setArguments(bundle);
//                    transaction.commit();
                    }
                }catch (NullPointerException e){
                    Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
                }
            }
        } else {
            // report failure
            Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
            Log.d(MainActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    //get data back from form
    public void setDate_in_EditText( String string ) {
        for(String out : string.split(";")){
            if(!histories.contains(out.toLowerCase())){
                histories.add(out.toLowerCase());
            }else{

                histories.add(histories.remove(histories.indexOf(out)));
            }
        }


        if(histories.size() > 30){
            histories.remove(0);
        }
    }


//    /**
//     * helper to scale down image before display to prevent render errors:
//     * "Bitmap too large to be uploaded into a texture"
//     */
//    private void displayPicture(String imagePath, ImageView imageView) {
//
//        // from http://stackoverflow.com/questions/22633638/prevent-bitmap-too-large-to-be-uploaded-into-a-texture-android
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 4;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        int height = bitmap.getHeight(), width = bitmap.getWidth();
//
//        if (height > 1280 && width > 960){
//            Bitmap imgbitmap = BitmapFactory.decodeFile(imagePath, options);
//            imageView.setImageBitmap(imgbitmap);
//        } else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }
}