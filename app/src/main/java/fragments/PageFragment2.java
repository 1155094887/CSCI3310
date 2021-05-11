package fragments;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.csci3310.ImageAdapter;
import com.example.csci3310.MainActivity;
import com.example.csci3310.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.lang.reflect.Array;
import java.util.ArrayList;

// reference(data transit): https://www.youtube.com/watch?v=m6zcM6Q2qZU
// reference(bottom sheet dialog): https://www.youtube.com/watch?v=hfoXhiMTc0c&t=239s
// reference(grid): https://www.youtube.com/watch?v=MRv0xtnaJAY
public class PageFragment2 extends Fragment {


         GridView gridView;
         TextView location;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        private GoogleMap mMap;
        private static final int REQUEST_LOCATION_PERMISSION = 1;



        ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
        // New Territories
        LatLng TuenWan = new LatLng(22.37056950316158, 114.11391258558831);
        LatLng KwaiTsing = new LatLng(22.354762079577917, 114.12175424354473);
        LatLng TuenMun = new LatLng(22.394120156912688, 114.0060225115134);
        LatLng YuenLong = new LatLng(22.45874277043533, 114.04001421324894);
        LatLng North = new LatLng(22.51098778990976, 114.15466434147893);
        LatLng TaiPo = new LatLng(22.442291811911243, 114.16630388473506);
        LatLng ShaTin = new LatLng(22.38517154868905, 114.20868808222872);
        LatLng SaiKung = new LatLng(22.38374106211243, 114.27001873724012);
        LatLng Island = new LatLng(22.266199878565686, 113.94522668495618);
        // Kowloon
        LatLng KowloonCity = new LatLng(22.32956364725957, 114.1902729239266);
        LatLng KwunTong = new LatLng(22.315063050268023, 114.22604469555978);
        LatLng ShamShuiPo = new LatLng(22.33335222166781, 114.15289038653788);
        LatLng WongTaiSin = new LatLng(22.347349562822977, 114.19566732200276);
        LatLng YauTsimMong = new LatLng(22.31037900726782, 114.16924594738953);
        // Hong Kong Island
        LatLng CentralAndWestern = new LatLng(22.27190102164926, 114.15697420127789);
        LatLng Eastern = new LatLng(22.279993431526233, 114.2279560819658);
        LatLng Southern = new LatLng(22.247267668630133, 114.18669895963914);
        LatLng WanChai = new LatLng(22.27739526210846, 114.18137465207651);

        ArrayList<String> title = new ArrayList<String>();

        public void enableMyLocation() {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }
        }

        public void setRequestLocationPermission(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
            // Check if location permissions are granted and if so enable the
            // location data layer.
            switch (requestCode) {
                case REQUEST_LOCATION_PERMISSION:
                    if (grantResults.length > 0
                            && grantResults[0]
                            == PackageManager.PERMISSION_GRANTED) {
                        enableMyLocation();
                        Toast toast = Toast.makeText(getActivity(),"TESTING YO", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                    }
            }
        }



        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            // ADD ITEM TO ARRAY LIST
            // New Territories
            arrayList.add(TuenWan); arrayList.add(KwaiTsing); arrayList.add(TuenMun); arrayList.add(YuenLong); arrayList.add(North); arrayList.add(TaiPo); arrayList.add(ShaTin); arrayList.add(SaiKung); arrayList.add(Island);
            // Kwoloon
            arrayList.add(KowloonCity); arrayList.add(KwunTong); arrayList.add(ShamShuiPo); arrayList.add(WongTaiSin); arrayList.add(YauTsimMong);
            // Hong Kong Isalnd
            arrayList.add(CentralAndWestern); arrayList.add(Eastern); arrayList.add(Southern); arrayList.add(WanChai);

            // ADD TITLE FOR MARKER IN TITLE ARRAYLIST
            // New Territories
            title.add("Tuen Wan"); title.add("Kwai Tsing"); title.add("Tuen Mun"); title.add("Yuen Long"); title.add("North"); title.add("Tai Po"); title.add("Sha Tin"); title.add("Sai Kung"); title.add("Island");
            // Kowloon
            title.add("Kowloon City"); title.add("Kwun Tong"); title.add("Sham Shui Po"); title.add("Wong Tai Sin"); title.add("Yau Tsim Mong");
            // Hong Kong Island
            title.add("Central and Western"); title.add("Eastern"); title.add("Southern"); title.add("Wan Chai");

            // ADD MARKER TO LOCATION
            for(int i=0; i<arrayList.size();i++) {
                    mMap.addMarker(new MarkerOptions().position(arrayList.get(i)).title(String.valueOf(title.get(i))));
            }

            // START POINT & ZOOM SIZE OF THE MAP
            LatLng home = new LatLng(22.366305913420714, 114.13061155926287);
            float zoom = 10;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));



            // OnClick EVENT when click the pin
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {


                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(PageFragment2.super.getContext());
                    View bottomSheetView = LayoutInflater.from(getActivity().getApplicationContext())
                            .inflate(
                                    R.layout.layout_bottom_sheet,
                                    (LinearLayout)getActivity().findViewById(R.id.bottomSheetContainer)

                            );

                    // --- Display the selected location in the bottomsheet
                    location = bottomSheetView.findViewById(R.id.location);
                    location.setText(marker.getTitle());

                    // --- Bottomsheet Button Action ---
                    bottomSheetView.findViewById(R.id.testing_button).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast toast = Toast.makeText(getActivity(),"OK Button Successful", Toast.LENGTH_SHORT);
                            toast.show();
                            bottomSheetDialog.dismiss();
                        }
                    });
                    // --- Bottomsheet Button Action ---

                    // Set the grid view
                    gridView = (GridView) bottomSheetView.findViewById(R.id.grid_view);
                    gridView.setAdapter(new ImageAdapter(getContext())); // this變getContext()

                    // OnClickListener of the grid photo
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Toast toast = Toast.makeText(getActivity(),"Photo Button Successful", Toast.LENGTH_SHORT);
                            toast.show();
                            bottomSheetDialog.dismiss();
                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();


                    return false;
                }
            });

            enableMyLocation();





        }
    };



    // DONT TOUCH
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater
                .inflate(R.layout.fragment_maps,container
                        ,false);



        return rootView;

    }

    // DONT TOUCH
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


    }
}


