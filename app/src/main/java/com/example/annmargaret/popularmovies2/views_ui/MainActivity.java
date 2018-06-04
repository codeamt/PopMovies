package com.example.annmargaret.popularmovies2.views_ui;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
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
    RecyclerView recyclerView;
    RecyclerView favsRecyclerView;
    Menu _menu;
    DrawerLayout mDrawer;
    NavigationView nvDrawer;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;
    View headerLayout;

    /* Models + Adapters*/
    Movie movie;
    public MovieAdapter movieAdapter;
    public MovieAdapter favsMovieAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.LayoutManager favsLayoutManager;
    public SwipeController swipeController;

    /* Primitives */
    static public List<Movie> movies = new ArrayList<>();
    static public List<Movie> favMovies = new ArrayList<>();
    public String endpoint = "popular";
    static int activeId = 0;
    public static boolean setting_cached = false;
    final String LOG_TAG = "MainActivity";

    /* APIs */
    public RequestQueue mRequestQueue;

    /* State */
    Parcelable mGridState;
    Parcelable mFavsState;




    /* Overrides */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_main);
        initializeMainActivity();
        VolleyRequests.getMovies(instance);
        getFavorites();
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

                ContentResolver contentResolver = getApplicationContext().getContentResolver();
                DBAdapter moviesDB = new DBAdapter(getApplicationContext());
                String message;

                movie = favMovies.get(position);


                if (moviesDB.isMovieFavorited(contentResolver, movie.id)) {
                    DetailActivity detailActivity = new DetailActivity();
                    message = getString(R.string.unfav_text);
                    moviesDB.removeMovie(contentResolver, movie.id);
                    if (detailActivity.instance != null) {
                        detailActivity.instance.fab.setImageDrawable(ContextCompat.getDrawable(detailActivity.instance.getApplicationContext(), android.R.drawable.btn_star_big_off));
                    }
                    favsMovieAdapter.clearItems();
                    favMovies.clear();
                    getFavorites();

                    favsMovieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
                    favsRecyclerView.setAdapter(favsMovieAdapter);
                    favsMovieAdapter.notifyDataSetChanged();

                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }

            }
        }, getApplicationContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(favsRecyclerView);


        favsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        _menu = menu;
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();

        if(manager != null && search != null) {
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    movieAdapter = new MovieAdapter(getApplicationContext(), movies);
                    recyclerView.setAdapter(movieAdapter);
                    movieAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }

        getInitialGridSortOrder(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int sortId = item.getItemId();
        configSortSettings(item, sortId);
        setGrid(sortId);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outstate) {
        super.onSaveInstanceState(outstate);
        mGridState = layoutManager.onSaveInstanceState();
        mFavsState = favsLayoutManager.onSaveInstanceState();

        outstate.putParcelable("GRID_STATE_KEY", mGridState);
        outstate.putParcelable("FAVS_STATE_KEY", mFavsState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle state) {
        if(state != null) {
            favsMovieAdapter.clearItems();
            favMovies.clear();
            getFavorites();


            favsMovieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
            favsRecyclerView.setAdapter(favsMovieAdapter);
            favsMovieAdapter.notifyDataSetChanged();

            if( activeId == R.id.action_sort_popularity || activeId == R.id.action_sort_rating) {
                updateUI(false);
            }


            mFavsState = state.getParcelable("FAVS_STATE_KEY");
            mGridState = state.getParcelable("GRID_STATE_KEY");

        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onResume() {
        super.onResume();
        favsMovieAdapter.clearItems();
        favMovies.clear();
        getFavorites();

        favsMovieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
        favsRecyclerView.setAdapter(favsMovieAdapter);
        favsMovieAdapter.notifyDataSetChanged();




        favsLayoutManager.onRestoreInstanceState(mFavsState);
        layoutManager.onRestoreInstanceState(mGridState);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            favsMovieAdapter.clearItems();
            favMovies.clear();
            getFavorites();

            favsMovieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
            favsRecyclerView.setAdapter(favsMovieAdapter);
            favsMovieAdapter.notifyDataSetChanged();

            if( activeId == R.id.action_sort_popularity || activeId == R.id.action_sort_rating) {
                updateUI(false);
            }
        }

        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            favsMovieAdapter.clearItems();
            favMovies.clear();
            getFavorites();
        }

        favsMovieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
        favsRecyclerView.setAdapter(favsMovieAdapter);
        favsMovieAdapter.notifyDataSetChanged();

        if( activeId == R.id.action_sort_popularity || activeId == R.id.action_sort_rating) {
            updateUI(false);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }



    /* Helpers */
    public void initializeMainActivity() {

        //Setup Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        toolbar.setLogo(R.drawable.pop_logo_custom_grey);



        //Setup Drawer
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                favsMovieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
                favsRecyclerView.setAdapter(favsMovieAdapter);
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
                String message = getString(R.string.swipe_instructions);
                if(favMovies.size() > 0) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                _menu.findItem(R.id.action_sort_popularity).setChecked(true);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });



        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        headerLayout = nvDrawer.getHeaderView(0);

        TextView drawerCloseOut = headerLayout.findViewById(R.id.drawer_close);
        drawerCloseOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                mDrawer.closeDrawers();
            }
        });






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


        //Setup Favs RecyclerView
        favsRecyclerView = (RecyclerView) headerLayout.findViewById(R.id.rvFavs);
        favsLayoutManager = new LinearLayoutManager(getApplicationContext());
        if(favsRecyclerView != null) {
            favsRecyclerView.setLayoutManager(favsLayoutManager);
            favsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            favsMovieAdapter = new MovieAdapter(getApplicationContext(), favMovies);
            favsRecyclerView.setAdapter(favsMovieAdapter);
        }




        //Volley Service
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }




    public void configSortSettings(MenuItem item, int id) {
        id = item.getItemId();
        if (id == R.id.action_sort_rating) {
            endpoint = "top_rated";
            updateUI(false);
        } else if (id == R.id.action_sort_popularity) {
            endpoint = "popular";
        }else if (id == R.id.action_favorites) {
            mDrawer.openDrawer(GravityCompat.START);
        }
        item.setChecked(true);
        Log.v(LOG_TAG, "Grid sort settings configured.");
    }





    public void setGrid(int id) {
        if(id == R.id.action_sort_popularity || id == R.id.action_sort_rating ) {
            updateUI(false);
            activeId = id;
        } else if(id == R.id.action_favorites) {
            mDrawer.openDrawer(GravityCompat.START);
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
            if(favsMovieAdapter != null) {
                favsMovieAdapter.notifyDataSetChanged();
            }
        }
    }



    public void updateUI(boolean cached){
        movies.clear();
        if(movieAdapter != null) {
            movieAdapter.clearItems();
        }
        setting_cached = cached;

        if (!cached) {
            VolleyRequests.getMovies(instance);
        } else {
            getFavorites();
        }
    }



}
