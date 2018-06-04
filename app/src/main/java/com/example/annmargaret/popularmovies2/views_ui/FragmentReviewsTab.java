package com.example.annmargaret.popularmovies2.views_ui;

import android.content.Intent;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.models.Movie;
import com.example.annmargaret.popularmovies2.api.VolleyRequests;
import com.example.annmargaret.popularmovies2.models.Review;
import com.example.annmargaret.popularmovies2.view_adapters.ReviewAdapter;


import java.util.ArrayList;
import java.util.List;

public class FragmentReviewsTab extends Fragment {

    /* Views and Widgets */
    public static FragmentReviewsTab instance;
    public View currentTab;
    public RecyclerView reviewsList;
    public ScrollView scrollView;
    public RelativeLayout scrollViewContainer;


    /* Models + Adapters */
    public Movie movie;
    public ReviewAdapter reviewAdapter;
    public RecyclerView.LayoutManager layoutManager;


    /* Primitives */
    public List<Review> reviews = new ArrayList<>();
    private static String LOG_TAG = "DetailReviewsTab";



    /* APIs */
    public RequestQueue mRequestQueue;


    Parcelable mListState;






    /* Constructor */
    public FragmentReviewsTab() { instance = this; }





    /* Overrides */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setUpViews(inflater, container);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if( savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable("REVIEWS_STATE_KEY");
        }

        populateFields();
        Log.v(LOG_TAG, "Finished Overriding onActivityCreated Method for Details Review Tab. Fields Populated. UI updated.");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "Finished Overriding onCreate Method for Details Review Tab. Movie args inherited from parent fragment.");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outstate) {
        super.onSaveInstanceState(outstate);


        mListState = layoutManager.onSaveInstanceState();
        outstate.putParcelable("REVIEWS_STATE_KEY", mListState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(mListState != null) {
            layoutManager.onRestoreInstanceState(mListState);
        }
    }





    /* Helpers */




    public View setUpViews(LayoutInflater inflater, ViewGroup container) {

        setHasOptionsMenu(true);



        //Get Movie
        if(getActivity() != null) {
            movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        }


        //Setup RecyclerView for Reviews
        currentTab = inflater.inflate(R.layout.detail_reviews_tab, container, false);
        reviewsList = currentTab.findViewById(R.id.reviewsList);
        if(getActivity() != null) {
            reviewAdapter = new ReviewAdapter(getActivity().getApplicationContext(), reviews);
            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            reviewsList.setLayoutManager(layoutManager);
            reviewsList.setItemAnimator(new DefaultItemAnimator());
            reviewsList.setAdapter(reviewAdapter);
        }

        //Get Scroll Reference for State Changes
        scrollView = currentTab.findViewById(R.id.reviewScrollView);
        scrollViewContainer = currentTab.findViewById(R.id.reviewScrollViewContainer);


        Log.v(LOG_TAG, "Views setup for onCreateView Method");
        return currentTab;
    }



    public void populateFields() {
        if(getActivity() != null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        updateUI();
    }


    //Populate Detail Fragment UI
    public void updateUI() {
        VolleyRequests.getReviews(movie.id, instance);
    }



}
