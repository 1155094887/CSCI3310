package com.example.csci3310;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    public int[] imageArray = {
            R.drawable.photo_1,R.drawable.photo_2,R.drawable.photo_3,R.drawable.photo_4,
            R.drawable.photo_5,R.drawable.photo_6,R.drawable.photo_7,R.drawable.photo_8,
            R.drawable.photo_9,R.drawable.photo_10,R.drawable.photo_11,R.drawable.photo_12,
            R.drawable.photo_13,R.drawable.photo_14,R.drawable.photo_15,R.drawable.photo_16,
            R.drawable.photo_17,R.drawable.photo_18,R.drawable.photo_19,R.drawable.photo_20
    };

    public ImageAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public Object getItem(int i) {
        return imageArray[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource((imageArray[i]));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340,350));

        return imageView;
    }
}
