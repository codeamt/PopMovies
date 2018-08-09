package com.example.annmargaret.popularmovies2.views_ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.annmargaret.popularmovies2.api.VolleyRequests;
import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.models.Movie;
import com.squareup.picasso.Picasso;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FragmentInfoTab extends Fragment {

    /* Instantiate Vars */


    /* Views + Widgets  */
    public static FragmentInfoTab instance;
    public View currentTab;
    public ScrollView scrollView;


    /* Models */
    public Movie movie;



    /* Primitives */
    private static String LOG_TAG = "DetailInfoTab";


    /* APIs */
    public RequestQueue mRequestQueue;







   /* Constructor */
    public FragmentInfoTab(){
        instance = this;
    }





    /* Overrides */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentTab =  inflater.inflate(R.layout.detail_info_tab, container, false);
        scrollView = currentTab.findViewById(R.id.infoScrollView);
        if(getActivity() != null) {
            movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        }

        Log.v(LOG_TAG, "Finished Overriding onCreateView Method to launch Views for Info Tab.");
        return currentTab;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Fill out info tab, and Fab
        populateFields();
        VolleyRequests.getRating(movie.id, instance);
        Log.v(LOG_TAG, "Finished Overriding onActivityCreated Method from Info Tab Fragment");

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onResume() {
        super.onResume();
    }







    /* Helpers */

    public void populateFields() {
        if(getActivity() != null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }
        updateUI();
    }


    public void updateUI() {

        //Get and Set Views
        if (movie != null) {

            //Set Movie Details
            Picasso.with(getContext()).load(movie.posterUrl).
                    placeholder(R.mipmap.ic_launcher).into((ImageView) currentTab.findViewById(R.id.posterImageView));
            ((TextView) currentTab.findViewById(R.id.title)).setText(movie.displayName);
            ((JustifiedTextView) currentTab.findViewById(R.id.overviewTextView)).setText(getString(R.string.synopsis, movie.overview));
            ((RatingBar) currentTab.findViewById(R.id.rating)).setRating(movie.rating / 2f);
            //thinking hardcoding this attribute is okay, since, the translation would still be descent
            ((TextView) currentTab.findViewById(R.id.ratingTextView)).setText(getString(R.string.rating_value, (float) Math.round(movie.rating * 10d) / 10d));


            //Handle Release Date Population
            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
            SimpleDateFormat dfInput = new SimpleDateFormat("yyyy-MM-dd");
            String releasedDate;

            try {
                releasedDate = df.format(dfInput.parse(movie.releasedDate));
            } catch (ParseException e) {
                e.printStackTrace();
                releasedDate = movie.releasedDate;
            }
            ((TextView) currentTab.findViewById(R.id.releaseDate)).setText(getString(R.string.release_date, releasedDate));



        }

    }
}
