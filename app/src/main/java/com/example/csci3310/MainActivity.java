package com.example.csci3310;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import fragments.PageFragment1;
import fragments.PageFragment2;
import fragments.PageFragment3;

public class MainActivity extends AppCompatActivity{

    private ViewPager pager;
    private PagerAdapter pagerAdapter; // 一個拎adapter既方法

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> list = new ArrayList<>();
        list.add(new PageFragment2());
        list.add(new PageFragment1());
        list.add(new PageFragment3());


        pager = findViewById(R.id.pager);
        pagerAdapter = new SliderPagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

        


    }


    }



