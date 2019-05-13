package com.example.offlinemaps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;


//Remember to implement  GalleryStripAdapter.GalleryStripCallBacks to fragment  for communication of fragment and GalleryStripAdapter
public class SlideShowFragment extends DialogFragment implements GalleryStripAdapter.GalleryStripCallBacks {
    //declare static variable which will serve as key of current position argument
    private static final String ARG_CURRENT_POSITION = "position";
    //Declare list of GalleryItems
    List<GalleryItem> galleryItems;
    //Deceleration of  Gallery Strip Adapter
    GalleryStripAdapter mGalleryStripAdapter;
    // //Deceleration of  Slide show View Pager Adapter
    SlideShowPagerAdapter mSlideShowPagerAdapter;
    //Deceleration of viewPager
    ViewPager mViewPagerGallery;
    TextView textViewImageName;
    RecyclerView recyclerViewGalleryStrip;
    //set bottom to visible of first load
    boolean isBottomBarVisible = true;
    private int mCurrentPosition;


    public SlideShowFragment() {
        // Required empty public constructor
    }

    //This method will create new instance of SlideShowFragment
    public static SlideShowFragment newInstance(int position) {
        SlideShowFragment fragment = new SlideShowFragment();
        //Create bundle
        Bundle args = new Bundle();
        //put Current Position in the bundle
        args.putInt(ARG_CURRENT_POSITION, position);
        //set arguments of SlideShowFragment
        fragment.setArguments(args);
        //return fragment instance
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initialise GalleryItems List
        galleryItems = new ArrayList<>();
        if (getArguments() != null) {
            //get Current selected position from arguments
            mCurrentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
            //get GalleryItems from activity
            galleryItems = ((GalleryActivity) getActivity()).galleryItems;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        mViewPagerGallery = view.findViewById(R.id.viewPagerGallery);
        // set On Touch Listener on mViewPagerGallery to hide show bottom bar
        mViewPagerGallery.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if (isBottomBarVisible) {
                        //bottom bar is visible make it invisible
                        FadeOutBottomBar();
                    } else {
                        //bottom bar is invisible make it visible
                        FadeInBottomBar();
                    }
                }
                return false;
            }

        });
        textViewImageName = view.findViewById(R.id.textViewImageName);
        //Initialise View Pager Adapter
        mSlideShowPagerAdapter = new SlideShowPagerAdapter(getContext(), galleryItems);
        //set adapter to Viewpager
        mViewPagerGallery.setAdapter(mSlideShowPagerAdapter);
        recyclerViewGalleryStrip = view.findViewById(R.id.recyclerViewGalleryStrip);
        //Create GalleryStripRecyclerView's Layout manager
        final RecyclerView.LayoutManager mGalleryStripLayoutManger = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        //set layout manager of GalleryStripRecyclerView
        recyclerViewGalleryStrip.setLayoutManager(mGalleryStripLayoutManger);
        //Create GalleryStripRecyclerView's Adapter
        mGalleryStripAdapter = new GalleryStripAdapter(galleryItems, getContext(), this, mCurrentPosition);
        //set Adapter of GalleryStripRecyclerView
        recyclerViewGalleryStrip.setAdapter(mGalleryStripAdapter);
        //tell viewpager to open currently selected item and pass position of current item
        mViewPagerGallery.setCurrentItem(mCurrentPosition);
        //set image name textview's text according to position
        textViewImageName.setText(galleryItems.get(mCurrentPosition).imageName);
        //Add OnPageChangeListener to viewpager to handle page changes
        mViewPagerGallery.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //set image name textview's text on any page selected
                textViewImageName.setText(galleryItems.get(position).imageName);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //first check When Page is scrolled and gets stable
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //get current  item on view pager
                    int currentSelected = mViewPagerGallery.getCurrentItem();
                    //scroll strip smoothly to current  position of viewpager
                    mGalleryStripLayoutManger.smoothScrollToPosition(recyclerViewGalleryStrip, null, currentSelected);
                    //select current item of viewpager on gallery strip at bottom
                    mGalleryStripAdapter.setSelected(currentSelected);

                }

            }
        });
        return view;
    }

    //Overridden method by GalleryStripAdapter.GalleryStripCallBacks for communication on gallery strip item selected
    @Override
    public void onGalleryStripItemSelected(int position) {
        //set current item of viewpager
        mViewPagerGallery.setCurrentItem(position);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //remove selection on destroy
        mGalleryStripAdapter.removeSelection();
    }

    //Method to fadeIn bottom bar which is image textview name
    public void FadeInBottomBar() {
        //define alpha animation
        AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        //set duration
        fadeIn.setDuration(1200);
        //set animation listener
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //set textview visible on animation ends
                textViewImageName.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //start animation
        textViewImageName.startAnimation(fadeIn);
        isBottomBarVisible = true;
    }

    public void FadeOutBottomBar() {
        //define alpha animation
        AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
        //set duration
        fadeOut.setDuration(1200);
        //set animation listener
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //set textview Visibility gone on animation ends
                textViewImageName.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        //start animation
        textViewImageName.startAnimation(fadeOut);
        isBottomBarVisible = false;

    }
}
