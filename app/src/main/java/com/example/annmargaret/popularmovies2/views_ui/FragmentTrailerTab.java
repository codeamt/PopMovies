package com.example.annmargaret.popularmovies2.views_ui;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.api.VolleyRequests;
import com.example.annmargaret.popularmovies2.database.KeyStore;
import com.example.annmargaret.popularmovies2.models.Movie;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


public class FragmentTrailerTab extends Fragment {


    /* Views */
    static FragmentTrailerTab instance;
    public View currentTab;
    Menu _menu;
    android.support.v7.widget.ShareActionProvider mShareActionProvider;
    YouTubePlayer mYouTubePlayer;

    /* Models */
    public Movie movie;

    /* Primitives */
    public static String trailerURL;
    static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    private static String LOG_TAG = "DetailTrailerTab";


    /* APIs */
    public RequestQueue mRequestQueue;
    public FragmentTrailerTab.FragmentYouTube youTubeFragment;






    /* Constructor  */
    public FragmentTrailerTab() {
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
        setUpViews(inflater, container, savedInstanceState);
        Log.v(LOG_TAG, "Finished Overriding onCreateView Method to launch Views for Info Tab.");
        return currentTab;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getData(savedInstanceState);
        Log.v(LOG_TAG, "Finished Overriding onActivityCreated Method from Info Tab Fragment");

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_share, menu);
        _menu = menu;
        setVideoShareIntent(trailerURL);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outstate) {
        super.onSaveInstanceState(outstate);
        outstate.putString("CURRENT_VIDEO_ID", trailerURL);
    }

    @Override
    public void onResume() {
        super.onResume();
    }












    /* Helpers */
    public void setUpViews(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentTab =  inflater.inflate(R.layout.detail_trailer_tab, container, false);
        if(getActivity() != null) {
            movie = getActivity().getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        }
        youTubeFragment = new FragmentTrailerTab.FragmentYouTube();


        Log.v(LOG_TAG, "Finished Overriding onCreateView Method to launch Views for Info Tab.");
    }



    public void getData(Bundle state){
       //Get Volley Service and Store Trailer URL reference
        if(getActivity() != null) {
            mRequestQueue = Volley.newRequestQueue(getActivity());
        }

        if(trailerURL == null) {
            VolleyRequests.getTrailer(movie.id, instance);
        }
        else if(state != null){
            trailerURL = state.getString("CURRENT_VIDEO_ID");
        }
    }



    private Intent createVideoShareIntent(String url, String title) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey this trailer for '" + title + "' looks awesome! Check it out: " + url);
        return shareIntent;
    }


    public void setVideoShareIntent(String url) {
        if(_menu != null) {
            MenuItem shareItem = _menu.findItem(R.id.action_share);
            mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);

            if (mShareActionProvider != null && movie != null) {

                mShareActionProvider.setShareIntent(createVideoShareIntent(YOUTUBE_BASE_URL +
                        url, movie.displayName));

            } else {
                if(movie != null) {
                    mShareActionProvider.setShareIntent(createVideoShareIntent("<Aww, man. Trailer can't be shared.>", movie.displayName));
                }
            }
        } else {
            Log.d(LOG_TAG, "Sharing unavailable at this time.");
        }

        Log.v(LOG_TAG, "Video sharing configured. ");

    }




    /* YouTube Fragment (Inner Class) Implementation */

    static public class FragmentYouTube extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.you_tube_api, container, false);
            YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();



            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

            youTubePlayerFragment.initialize(KeyStore.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {


                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    instance.mYouTubePlayer = player;

                    if (!wasRestored && instance.trailerURL !=null) {
                        instance.mYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                        instance.mYouTubePlayer.loadVideo(instance.trailerURL);

                    }

                    if (wasRestored && instance.trailerURL != null) {
                        instance.mYouTubePlayer.loadVideo(instance.trailerURL);

                    }




                    instance.mYouTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onAdStarted() { }

                        @Override
                        public void onError(YouTubePlayer.ErrorReason arg0) { }

                        @Override
                        public void onLoaded(String arg0) { }

                        @Override
                        public void onLoading() { }

                        @Override
                        public void onVideoEnded() { }

                        @Override
                        public void onVideoStarted() { }
                    });


                    instance.mYouTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
                        @Override
                        public void onBuffering(boolean arg0) { }

                        @Override
                        public void onPaused() { }

                        @Override
                        public void onPlaying() { }

                        @Override
                        public void onSeekTo(int arg0) { }

                        @Override
                        public void onStopped() { }
                    });
                }


                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult error) {
                    // YouTube error
                    String errorMessage = error.toString();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
                    Log.d("errorMessage:", errorMessage);
                }



            });



            return rootView;
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outstate){
            FragmentTrailerTab.FragmentYouTube.super.onSaveInstanceState(outstate);
            outstate.putString("trailer_url", instance.trailerURL);

        }

        @Override
        public void onActivityCreated(Bundle instanceState) {
            super.onActivityCreated(instanceState);
            if(instanceState != null) {
                instance.trailerURL = instanceState.getString("trailer_url");
            }


        }

        @Override
        public void onResume() {
            FragmentTrailerTab.FragmentYouTube.super.onResume();
            if(getArguments() != null) {
                getArguments().getString("trailer_url");
            }
        }


    }




}
