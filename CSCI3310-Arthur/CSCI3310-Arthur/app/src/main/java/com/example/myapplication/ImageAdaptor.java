package com.example.myapplication;

import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.media.ExifInterface;
import java.io.*;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;


/**
 * ImageAdaptor for the GridView
 */
public class ImageAdaptor extends BaseAdapter {
    Context context;
    int[] dissertImageIds;
    Uri[] ImageUris;
    boolean mode;
    private ImageViewHolder holder;

    ImageAdaptor(Context c, int[] imageIds, Uri[] uris, boolean isEdit) {
        context = c;
        dissertImageIds = imageIds;
        ImageUris = uris.clone();
        mode = isEdit;
    }

    /**
     * Inner View Holder class
     */
    private class ImageViewHolder  {
        int position;
        ImageView mImageView;
        ;

        public ImageViewHolder(@NonNull View itemView) {

            mImageView = (ImageView) itemView.findViewById(R.id.image);

            // Set up listener to handle context menu creation
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                /**
                 * Callback during creation of context menu
                 * @param menu
                 * @param v
                 * @param menuInfo
                 */

                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                    
                    menu.add("Tag Info").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            showTagFragment fragment = showTagFragment.newInistance("-.-","xd",ImageUris[getPosition()], "yo",true);
                            FragmentActivity activity = (FragmentActivity)(context);

                            fragment.show(
                                    activity.getSupportFragmentManager(), showTagFragment.TAG);

                            return true;
                        }
                    });

                }
            });
        }

        // getter and setter for the layout position
        void setPosition(int pos) { position = pos;}
        int getPosition() { return position;}
        double parse(String ratio) {
            if (ratio.contains("/")) {
                String[] rat = ratio.split("/");
                return Double.parseDouble(rat[0]) / Double.parseDouble(rat[1]);
            } else {
                return Double.parseDouble(ratio);
            }
        }
    }

    @Override
    public int getCount() {
        return ImageUris.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    //set uri to each gridview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // ViewHolder Pattern is implemented via getTag/setTag of convertView
            ImageViewHolder holder;
        if(convertView == null) {
            convertView = (LayoutInflater.from(context)).inflate(R.layout.grid_item_layout, null);
            holder = new ImageViewHolder(convertView);
            holder.setPosition(position);
            convertView.setTag(holder);
        } else {
            holder = (ImageViewHolder) convertView.getTag();
        }

        holder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//        holder.mImageView.setImageBitmap();
        if (position >= ImageUris.length){
            holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
            return convertView;
        }
        if (ImageUris != null && ImageUris[position] != null){
            try {
//                Toast.makeText(context,"started xdddd"+Integer.toString(position)+ImageUris[position],Toast.LENGTH_LONG).show();
                holder.mImageView.setImageBitmap(new UserPicture(ImageUris[position], context.getContentResolver()).getBitmap());
            } catch (IOException e) {
                e.printStackTrace();
                holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }else{
            holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
        Uri tempuri = ImageUris[position];
        if(mode) {
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        ImageView imgview = (ImageView) view;
                        Drawable imgDraw = imgview.getDrawable();
//                Toast.makeText(context,"position"+Integer.toString(position)+"is clicked",Toast.LENGTH_SHORT).show();
                        ImageView imgpreview = (ImageView) parent.getRootView().findViewById(R.id.image_preview);
                        imgpreview.setImageDrawable(imgDraw);
                        imgpreview.setImageURI(tempuri);
                        imgpreview.setTag(tempuri);


                }
            });
        }
        return convertView;
    }

}
