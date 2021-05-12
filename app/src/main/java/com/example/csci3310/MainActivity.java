package com.example.csci3310;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fragments.PhotoTakingFragment;
import fragments.MapDirectingFragment;
import fragments.PageFragment3;

public class MainActivity extends AppCompatActivity{

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private String toast_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<Fragment> list = new ArrayList<>();
        list.add(new MapDirectingFragment());
        list.add(new PhotoTakingFragment());
        list.add(new PageFragment3());

        pager = findViewById(R.id.pager);
        pagerAdapter = new SliderPagerAdapter(getSupportFragmentManager(), list);

        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);

    }

    public void toast_action(View view) { //You can import function here (Overall page)
        toast_message = "Test Success";
        Toast toast = Toast.makeText(this, toast_message, Toast.LENGTH_SHORT);
        toast.show();
    }




}