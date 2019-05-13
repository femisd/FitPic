package com.example.offlinemaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for slide show viewpager
 */

public class SlideShowPagerAdapter extends PagerAdapter {

    Context mContext;
    //Layout inflater
    LayoutInflater mLayoutInflater;
    //list of Gallery Items
    List<GalleryItem> galleryItems;

    public SlideShowPagerAdapter(Context context, List<GalleryItem> galleryItems) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //set galleryItems
        this.galleryItems = galleryItems;
    }

    @Override
    public int getCount() {
        return galleryItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = itemView.findViewById(R.id.imageViewThumbnail);
        //load current image in viewpager
        Picasso.get().load(galleryItems.get(position).imageUri).fit().into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }

}
