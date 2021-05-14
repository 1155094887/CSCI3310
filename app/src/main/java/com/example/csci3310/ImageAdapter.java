package com.example.csci3310;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import fragments.TagformFragment;
import fragments.showTagFragment;

public class ImageAdapter extends BaseAdapter {

//    private Context mContext;
//    private ArrayList<String> uris = new ArrayList<String>();
//    public int[] imageArray = {
//            R.drawable.photo_1,R.drawable.photo_2,R.drawable.photo_3,R.drawable.photo_4,
//            R.drawable.photo_5,R.drawable.photo_6,R.drawable.photo_7,R.drawable.photo_8,
//            R.drawable.photo_9,R.drawable.photo_10,R.drawable.photo_11,R.drawable.photo_12,
//            R.drawable.photo_13,R.drawable.photo_14,R.drawable.photo_15,R.drawable.photo_16,
//            R.drawable.photo_17,R.drawable.photo_18,R.drawable.photo_19,R.drawable.photo_20
//    };
//
//    public ImageAdapter(Context mContext,ArrayList<String> uris) {
//        this.mContext = mContext;
//        this.uris = uris;
//    }
//
//    @Override
//    public int getCount() {
//        return imageArray.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return imageArray[i];
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource((imageArray[i]));
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(340,350));
//
////        if (position >= ImageUris.length){
////            holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
////            return convertView;
////        }
////        if (ImageUris != null && ImageUris[position] != null){
////            try {
//////                Toast.makeText(context,"started xdddd"+Integer.toString(position)+ImageUris[position],Toast.LENGTH_LONG).show();
////                holder.mImageView.setImageBitmap(new UserPicture(ImageUris[position], context.getContentResolver()).getBitmap());
////            } catch (IOException e) {
////                e.printStackTrace();
////                holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
////            }
////        }else{
////            holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
////        }
//
//        return imageView;
//    }
    Context context;
    int[] dissertImageIds;
    ArrayList<Uri> ImageUris = new ArrayList<Uri>();
    boolean mode;
    private ImageViewHolder holder;
    ArrayList<String> uri_strings;

    public ImageAdapter(Context c, ArrayList<String> uris, boolean isEdit) {
        context = c;
        uri_strings = uris;
        try{
            String[] temp = uris.toArray(new String[0]);
            for(int i = 0; i < temp.length;i++){
                ImageUris.add(Uri.parse(temp[i]));

            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
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
                    menu.add("Edit Info").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int position = getPosition();
//                            FileOutputStream outStream = new FileOutputStream();
                            ArrayList<String>history = new ArrayList<String>();
                            FragmentActivity activity = (FragmentActivity)(context);
                            File file = new File(String.valueOf(ImageUris.get(position)));
                            Bitmap file_bitmap = null;
                            if(file.exists()){
                                file_bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                            }else{
                                try {
                                    file_bitmap = new UserPicture(ImageUris.get(position), activity.getContentResolver()).getBitmap();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            
                           
//                            xd.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                            TagformFragment form = new TagformFragment().newInistance("",System.currentTimeMillis()+"", String.valueOf(ImageUris.get(position)),history,file_bitmap,false);
                            form.show(activity.getSupportFragmentManager(),TagformFragment.TAG);

                            return true;
                        }
                    });
                    menu.add("Tag Info").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            FragmentActivity activity = (FragmentActivity)(context);
                            showTagFragment fragment= showTagFragment.newInistance("","",Uri.parse(""),"",true,uri_strings);
                            fragment.show(activity.getSupportFragmentManager(), showTagFragment.TAG);


//                            showTagFragment fragment = showTagFragment.newInistance("-.-","xd",ImageUris[getPosition()], "yo",true);
//                            FragmentActivity activity = (FragmentActivity)(context);
//
//                            fragment.show(
//                                    activity.getSupportFragmentManager(), showTagFragment.TAG);

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
        return ImageUris.size();
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
        if (position >= ImageUris.size()){
            holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
            return convertView;
        }
        if (ImageUris != null && ImageUris.get(position) != null){
            try {
//                Toast.makeText(context,"started xdddd"+Integer.toString(position)+ImageUris[position],Toast.LENGTH_LONG).show();
                if(ImageUris.get(position).toString().endsWith("jpg") || ImageUris.get(position).toString().endsWith("png")){
                    Bitmap myBitmap = BitmapFactory.decodeFile(String.valueOf(ImageUris.get(position)));
                    holder.mImageView.setImageBitmap(myBitmap);
                }else{
                    holder.mImageView.setImageBitmap(new UserPicture(ImageUris.get(position), context.getContentResolver()).getBitmap());
                }







            } catch (IOException e) {
                e.printStackTrace();
                holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
            }
        }else{
            holder.mImageView.setImageResource(R.drawable.ic_launcher_foreground);
        }
        Uri tempuri = ImageUris.get(position);
        if(mode) {
            holder.mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    ImageView imgview = (ImageView) view;
//                    Drawable imgDraw = imgview.getDrawable();
////                Toast.makeText(context,"position"+Integer.toString(position)+"is clicked",Toast.LENGTH_SHORT).show();
//                    ImageView imgpreview = (ImageView) parent.getRootView().findViewById(R.id.image_preview);
//                    imgpreview.setImageDrawable(imgDraw);
//                    imgpreview.setImageURI(tempuri);
//                    imgpreview.setTag(tempuri);


                }
            });
        }
        return convertView;
    }
}
