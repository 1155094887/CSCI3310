package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    static int[] logo = {
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground,
            R.drawable.ic_launcher_foreground
    };



    String[] list;
    Uri[] urilist;
    GridView gallery;
    ImageAdaptor mImageAdaptor;
    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static BlankFragment newInstance(ArrayList<Parcelable> param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            list = getArguments().getParcelableArrayList(ARG_PARAM1);
//            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//        list = getArguments().getStringArray("list");
        Toast.makeText(getContext().getApplicationContext(),"on created",Toast.LENGTH_SHORT).show();
//        try{
//            list = getArguments().getStringArray("list");
//            Toast.makeText(getContext(),"onactivityCreate"+Integer.toString(list.length),Toast.LENGTH_SHORT).show();
//
//            urilist = new Uri[list.length];
//            if( list != null ) {
//                int i = 0;
//                for (String parcel : list) {
//                    if (parcel == null )
//                        break;
//                    Uri uri = Uri.parse(parcel);
//                    urilist[i] = uri;
//                    i += 1;
//                    // handle the images one by one here
//                }
//            }
////            GridView gallery = (GridView) getActivity().findViewById(R.id.gridview);
//////            TextView temp = (TextView) getActivity().findViewById(R.id.txt_fragment);
//////            temp.setText("heha");
////            ImageAdaptor mImageAdaptor = new ImageAdaptor(getActivity(), logo, urilist);
////            gallery.setAdapter(mImageAdaptor);
//        }catch (NullPointerException e){
//            e.printStackTrace();
//        }
    }
    //get uril here
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        try {
//            mNameStr = getArguments().getString("name");
//        } catch (Exception e) {
//            mNameStr = "pikachu";
//        }
//        link = getArguments().getString("link");
        try{
            list = getArguments().getStringArray("list");
//            Toast.makeText(getContext(),"onactivityCreate"+Integer.toString(list.length),Toast.LENGTH_SHORT).show();

            urilist = new Uri[list.length];
            if( list != null ) {
                int i = 0;
                for (String parcel : list) {
                    if (parcel == null )
                        continue;
                    else{
                        Uri uri = Uri.parse(parcel);
                        urilist[i] = uri;
                        i += 1;
                    }

                    // handle the images one by one here
                }
            }

//            Toast.makeText(getContext(),"onactivityCreate"+Integer.toString(list.length)+Integer.toString(urilist.length) ,Toast.LENGTH_SHORT).show();
            Uri[] xd = {Uri.parse("https://stackoverflow.com/questions/10230929/how-can-i-convert-string-to-arrayliststring"), Uri.parse("https://stackoverflow.com/questions/10230929/how-can-i-convert-string-to-arrayliststring"), Uri.parse("https://stackoverflow.com/questions/10230929/how-can-i-convert-string-to-arrayliststring")};
            GridView gallery = (GridView) getActivity().findViewById(R.id.gridview);
//            TextView temp = (TextView) getActivity().findViewById(R.id.txt_fragment);
//            temp.setText("heha");
            ImageAdaptor mImageAdaptor = new ImageAdaptor(getActivity(), logo, urilist,true);
            gallery.setAdapter(mImageAdaptor);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }
    //set Adapter
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        try{
            list = getArguments().getStringArray("list");
//            Toast.makeText(getContext(),"onactivityCreate"+Integer.toString(list.length),Toast.LENGTH_SHORT).show();

            urilist = new Uri[list.length];
            if( list != null ) {
                int i = 0;
                for (String parcel : list) {
                    if (parcel == null )
                        continue;
                    else{
                        Uri uri = Uri.parse(parcel);
                        urilist[i] = uri;
                        i += 1;
                    }

                    // handle the images one by one here
                }
            }

//            Toast.makeText(getContext(),"onactivityCreate"+Integer.toString(list.length)+Integer.toString(urilist.length) ,Toast.LENGTH_SHORT).show();
            gallery = view.findViewById(R.id.gridview);
//        TextView temp = view.findViewById(R.id.txt_fragment);
//        temp.setText("heha");
            mImageAdaptor = new ImageAdaptor(getActivity(), logo, urilist,true);
            gallery.setAdapter(mImageAdaptor);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        return view;
    }
}