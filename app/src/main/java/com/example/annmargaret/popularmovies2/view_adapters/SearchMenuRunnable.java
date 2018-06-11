package com.example.annmargaret.popularmovies2.view_adapters;

import android.content.res.Configuration;
import android.support.v7.widget.SearchView;

import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.api.VolleyRequests;
import com.example.annmargaret.popularmovies2.views_ui.MainActivity;

import java.lang.ref.WeakReference;

public class SearchMenuRunnable implements Runnable {
    WeakReference<SearchView> searchViewWeakReference;
    String mSearchQuery;
    MainActivity mainActivity;


    public SearchMenuRunnable(SearchView searchView, String searchQuery, MainActivity reference) {
        searchViewWeakReference = new WeakReference<>(searchView);
        mSearchQuery = searchQuery;
        mainActivity = reference;
    }

    @Override
    public void run() {
        if(null != searchViewWeakReference.get()) {
            SearchView searchView = searchViewWeakReference.get();
            searchView.setIconified(false);
            mainActivity.searchMenuItem.expandActionView();
            searchView.setQuery(mSearchQuery, false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {


                    if (mainActivity.activeId == R.id.action_sort_popularity || mainActivity.activeId == R.id.action_sort_rating){
                        mainActivity.movieAdapter.setMoviesList(mainActivity.movies);
                        mainActivity.movieAdapter.notifyDataSetChanged();
                    } else if(mainActivity.activeId == R.id.action_favorites) {
                        mainActivity.movieAdapter.setMoviesList(mainActivity.favMovies);
                        mainActivity.movieAdapter.notifyDataSetChanged();
                    }

                    mainActivity.movieAdapter.getFilter().filter(newText);
                    return false;
                }
            });

            searchView.clearFocus();
        }
    }






}
