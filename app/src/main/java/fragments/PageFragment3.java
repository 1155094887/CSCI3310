package fragments;

import android.content.ClipData;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.csci3310.R;
import com.example.csci3310.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class PageFragment3 extends Fragment {

    private static final int SELECT_SINGLE_PICTURE = 101;
    private static final int SELECT_MULTIPLE_PICTURE = 201;
    public static final String IMAGE_TYPE = "image/*";
    ArrayList<String> urilist = new ArrayList<String>();
    GridView gallery;
    ImageAdapter mImageAdaptor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater
                .inflate(R.layout.layout_detailgallery,container
                        ,false);
        rootView.findViewById(R.id.btn_pick_multiple_images).setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
//                Intent intent = new Intent();
//                intent.setType(IMAGE_TYPE);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        getString(R.string.select_picture)), SELECT_SINGLE_PICTURE);

                Intent intent = new Intent();
                intent.setType(IMAGE_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // this line is different here !!
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Images"), SELECT_MULTIPLE_PICTURE);

            }
        });
        ContextWrapper contextWrapper= new ContextWrapper(getContext());
        File folder = new File(contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DCIM).toString());
        File[] listOfFiles = folder.listFiles();
        for(File file : listOfFiles){
            Toast.makeText(getActivity(),file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
            urilist.add(file.getAbsolutePath());
        }
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
//        for(String row:stringArray_read){
//            if(urilist.contains(row.split(",")[5])){
//                continue;
//            }else{
//                urilist.add(row.split(",")[5].substring(1,row.split(",")[5].length()-1));
//            }
//        }
        Toast.makeText(getActivity(),"onCreated",Toast.LENGTH_SHORT).show();
        gallery = rootView.findViewById(R.id.gridview);
//        TextView temp = view.findViewById(R.id.txt_fragment);
//        temp.setText("heha");
//      ArrayList<String> xd = new ArrayList<String>(Arrays.asList(new String[]{"haha", "hoho", "hehe"}));
       mImageAdaptor = new ImageAdapter(getActivity(), urilist,true);
//        mImageAdaptor = new ImageAdapter(getActivity(), urilist);
        gallery.setAdapter(mImageAdaptor);

        rootView.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentActivity activity = (FragmentActivity)(getActivity());
                showTagFragment fragment= showTagFragment.newInistance("","",Uri.parse(""),"",false,urilist,gallery);
                fragment.show(activity.getSupportFragmentManager(),showTagFragment.TAG);
            }
        });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {

            if (requestCode == SELECT_SINGLE_PICTURE) {
//                boolean yes = Intent.ACTION_SEND_MULTIPLE.equals(data.getAction());
//                boolean yes2 = data.hasExtra(Intent.EXTRA_STREAM);
//                String xd = data.getType();
//                Uri selectedImageUri = data.getData();
//                Uri uri = data.getData();
//
//                Toast.makeText(this,uri+Boolean.toString(yes)+Boolean.toString(yes2),Toast.LENGTH_LONG).show();
//
//                try (InputStream inputStream = this.getContentResolver().openInputStream(uri)) {
//                    ExifInterface exif = new ExifInterface(inputStream);
//                    int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                    String time = exif.getAttribute(ExifInterface.TAG_DATETIME);
//                    String lat = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
//                    TextView textview = (TextView)findViewById(R.id.txt_hello);
//                    textview.setText(time + lat);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//
////                ExifInterface exif = new ExifInterface(pathToTheImage);
////                String datetime =  exif.getAttribute(ExifInterface.TAG_DATETIME);
//                try {
//                    selectedImagePreview.setImageBitmap(new UserPicture(selectedImageUri, getContentResolver()).getBitmap());
//                } catch (IOException e) {
//                    Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
//                }
                // original code
//                String selectedImagePath = getPath(selectedImageUri);
//                selectedImagePreview.setImageURI(selectedImageUri);
            }
            else if (requestCode == SELECT_MULTIPLE_PICTURE) {
                Toast.makeText(getActivity(),"multiple here",Toast.LENGTH_SHORT).show();
                //And in the Result handling check for that parameter:
//                boolean yes = Intent.ACTION_SEND_MULTIPLE.equals(data.getAction());
//                boolean yes2 = data.hasExtra(Intent.EXTRA_STREAM);
                try {
                    ClipData dataclip = data.getClipData();
                    int count = dataclip.getItemCount();
//                    urilist = new String[count];
                    if (dataclip != null) {
                        int i = 0;
                        for (i = 0; i < dataclip.getItemCount(); i++) {
//                        Uri uri = (Uri) parcel;
                            Uri uri = dataclip.getItemAt(i).getUri();
                            urilist.add(uri.toString());

                            // handle the images one by one here
                        }
                    }
//                    Uri urilen = dataclip.getItemAt(0).getUri();
////                String xd = data.getParcelableArrayExtra();
////                    Toast.makeText(this, Integer.toString(count) + " " + Boolean.toString(yes) + Boolean.toString(yes2), Toast.LENGTH_SHORT).show();
//                    Gallery gallery = root.findViewById(R.id.gridview);
//                    TextView temp = view.findViewById(R.id.txt_fragment);
//                    temp.setText("heha");
                    mImageAdaptor = new ImageAdapter(getActivity(), urilist, false);
                    gallery.setAdapter(mImageAdaptor);
//
//                    FragmentTransaction transaction =
//                            getSupportFragmentManager().beginTransaction();
//
//                    BlankFragment BlankFragment = new BlankFragment();
//                    transaction.replace(R.id.list_fragment, BlankFragment);
//                    Bundle bundle = new Bundle();
//
//                    bundle.putStringArray("list", urilist);
//                    // set Fragmentclass Arguments
//                    BlankFragment.setArguments(bundle);
//                    transaction.commit();
//
//                    uri_gen_list = new ArrayList<String>(Arrays.asList(urilist));
//                    if ((Intent.ACTION_SEND_MULTIPLE.equals(data.getAction())
//                            && data.hasExtra(Intent.EXTRA_STREAM))) {
//                        // retrieve a collection of selected images
//
//
//                        ArrayList<Parcelable> list = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
//                        // iterate over these images
//                        urilist = new String[20];
//                        if (list != null) {
//                            int i = 0;
//                            for (Parcelable parcel : list) {
//                                Uri uri = (Uri) parcel;
//                                urilist[i] = uri.toString();
//                                i += 1;
//                                // handle the images one by one here
//                            }
//                        }
//
//                        // for now just show the last picture
//                        if (!list.isEmpty()) {
//                            Uri imageUri = (Uri) list.get(list.size() - 1);
//
//                            try {
//                                selectedImagePreview.setImageBitmap(new UserPicture(imageUri, getContentResolver()).getBitmap());
//                            } catch (IOException e) {
//                                Log.e(MainActivity.class.getSimpleName(), "Failed to load image", e);
//                            }
//                            // original code
////                        String selectedImagePath = getPath(imageUri);
////                        selectedImagePreview.setImageURI(imageUri);
////                        displayPicture(selectedImagePath, selectedImagePreview);
//                        }
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
//                    }
                }catch (NullPointerException e){
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                }

            }
        } else {
            // report failure
//            Toast.makeText(getApplicationContext(), R.string.msg_failed_to_get_intent_data, Toast.LENGTH_LONG).show();
//            Log.d(MainActivity.class.getSimpleName(), "Failed to get intent data, result code is " + resultCode);
        }
    }
}
