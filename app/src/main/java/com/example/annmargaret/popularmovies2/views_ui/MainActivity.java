package com.example.annmargaret.popularmovies2.views_ui;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.annmargaret.popularmovies2.database.DBAdapter;
import com.example.annmargaret.popularmovies2.R;

import com.example.annmargaret.popularmovies2.view_adapters.SearchMenuRunnable;
import com.example.annmargaret.popularmovies2.views_ui.animations.SwipeController;
import com.example.annmargaret.popularmovies2.views_ui.animations.SwipeControllerActions;
import com.example.annmargaret.popularmovies2.api.VolleyRequests;

import com.example.annmargaret.popularmovies2.models.Movie;
import com.example.annmargaret.popularmovies2.view_adapters.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    /* Views + Widgets */
    public static MainActivity instance;
    public RecyclerView recyclerView;
    Menu _menu;
    public Toolbar toolbar;
    SearchView search;
    public MenuItem searchMenuItem;

    /* Models + Adapters*/
    public MovieAdapter movieAdapter;
    public RecyclerView.LayoutManager layoutManager;
    public SearchManager manager;

    /* Primitives */
    static public List<Movie> movies = new ArrayList<>();
    static public List<Movie> favMovies = new ArrayList<>();
    public String endpoint = "popular";
    public static int activeId = 0;
    public static boolean setting_cached = false;
    final String LOG_TAG = "MainActivity";

    /* APIs */
    public RequestQueue mRequestQueue;

    /* State */
    public Parcelable mGridState;
    public String currentQuery;
    private static Bundle onPauseState;





    /* Overrides */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            mGridState = savedInstanceState.getParcelable("GRID_STATE_KEY");
            currentQuery = savedInstanceState.getString("QUERY_KEY");
            if(layoutManager != null) {
                layoutManager.onRestoreInstanceState(mGridState);
            }
        }
        instance = this;
        setContentView(R.layout.activity_main);
        initializeMainActivity();

        if(movies.size() == 0 && savedInstanceState == null){
            VolleyRequests.getMovies(instance);
        }
        movieAdapter = new MovieAdapter(getApplicationContext(), movies);
        recyclerView.setAdapter(movieAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        _menu = menu;
        searchMenuItem = menu.findItem(R.id.search);
        manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = (SearchView) menu.findItem(R.id.search).getActionView();


        if(manager != null && search != null) {
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (activeId == R.id.action_sort_popularity || activeId == R.id.action_sort_popularity){
                        movieAdapter = new MovieAdapter(getApplicationContext(), movies);
                        recyclerView.setAdapter(movieAdapter);
                    } else if(activeId == R.id.action_favorites) {
                        movieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
                        recyclerView.setAdapter(movieAdapter);
                    }
                    movieAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }



        if (currentQuery != null && !currentQuery.isEmpty() && search != null) {
            new Handler(Looper.getMainLooper()).post(new SearchMenuRunnable(search, currentQuery, instance));
        }


        if(mGridState == null) {
            getInitialGridSortOrder(menu);
        }

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int sortId = item.getItemId();
        configSortSettings(item, sortId);
        setGrid(sortId);
        return super.onOptionsItemSelected(item);
    }



    //State Stuff
    @Override
    protected void onSaveInstanceState(Bundle outstate) {

        mGridState = layoutManager.onSaveInstanceState();
        currentQuery = search.getQuery().toString();

        outstate.putParcelable("GRID_STATE_KEY", mGridState);
        outstate.putString("QUERY_KEY", currentQuery);

        super.onSaveInstanceState(outstate);
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        if(state != null) {
            mGridState = state.getParcelable("GRID_STATE_KEY");
            currentQuery = state.getString("QUERY_KEY");
        }

        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mGridState != null) {
            layoutManager.onRestoreInstanceState(mGridState);
        }

        if(onPauseState != null && layoutManager != null) {
            layoutManager.onRestoreInstanceState(mGridState);
        }
    }

    @Override
    protected void onPause() {

        onPauseState = new Bundle();
        mGridState = layoutManager.onSaveInstanceState();
        //mFavsState = favsLayoutManager.onSaveInstanceState();
        currentQuery = search.getQuery().toString();
        onPauseState.putParcelable("GRID_STATE_KEY", mGridState);
        //onPauseState.putParcelable("FAVS_STATE_KEY", mFavsState);
        onPauseState.putString("QUERY_KEY", currentQuery);
        super.onPause();
    }






    /* Helpers */
    public void initializeMainActivity() {

        //Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.pop_logo_custom_grey);



        //Instantiate movie adapter
        movieAdapter = new MovieAdapter(getApplicationContext(), movies);


        //Setup RecyclerView Grid
        recyclerView = (RecyclerView) findViewById(R.id.rvMain);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        } else {
            layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        }


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(movieAdapter);





        //Volley Service
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }





    public void configSortSettings(MenuItem item, int id) {
        id = item.getItemId();
        if (id == R.id.action_sort_rating) {
            movieAdapter = new MovieAdapter(getApplicationContext(), movies);
            movieAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(movieAdapter);
            endpoint = "top_rated";
            //updateUI(false);
        } else if (id == R.id.action_sort_popularity) {
            movieAdapter = new MovieAdapter(getApplicationContext(), movies);
            movieAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(movieAdapter);
            endpoint = "popular";
        }else if (id == R.id.action_favorites) {
            movieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
            movieAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(movieAdapter);
        }
        item.setChecked(true);
        Log.v(LOG_TAG, "Grid sort settings configured.");
    }





    public void setGrid(int id) {
        if(id == R.id.action_sort_popularity || id == R.id.action_sort_rating ) {
            updateUI(false);
            activeId = id;
        } else if(id == R.id.action_favorites) {
            //temp fix for reviewers
            updateUI(true);
            activeId = id;
        }
    }





    public void getInitialGridSortOrder(Menu menu) {
        if(activeId == 0) {
            activeId = R.id.action_sort_popularity;
        } else{
            menu.findItem(activeId).setChecked(true);
        }
        Log.v(LOG_TAG, "Grid initial sort order is set.");
    }




    public void getFavorites(){
        if(getApplicationContext() != null) {
            favMovies.addAll((new DBAdapter(getApplicationContext()).getFavoriteMovies(getApplicationContext().getContentResolver())));
        }
    }



    public void updateUI(boolean cached){
        if(movieAdapter != null) {
            movieAdapter.clearItems();
        }
        setting_cached = cached;

        if (!cached) {
            movies.clear();
            VolleyRequests.getMovies(instance);
            movieAdapter = new MovieAdapter(getApplicationContext(), movies);
            movieAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(movieAdapter);
        } else {
            favMovies.clear();
            getFavorites();
            movieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
            movieAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(movieAdapter);
        }
    }

}
