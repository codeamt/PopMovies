package com.example.annmargaret.popularmovies2.views_ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.api.VolleyRequests;
import com.example.annmargaret.popularmovies2.database.DBAdapter;
import com.example.annmargaret.popularmovies2.models.Movie;
import com.example.annmargaret.popularmovies2.view_adapters.DetailTabsPagerAdapter;
import com.example.annmargaret.popularmovies2.view_adapters.MovieAdapter;
import com.example.annmargaret.popularmovies2.views_ui.animations.ClickOnFab;
import com.example.annmargaret.popularmovies2.views_ui.animations.IconTextView;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private static String LOG_TAG = "DetailActivity";

    /* Views + Wigets*/
    Toolbar toolbar;
    Toolbar toolbar2;
    ImageView backdrop;
    FloatingActionButton fab;
    public static DetailActivity instance;
    FloatingActionButton fabOnScroll;
    AppBarLayout appBarLayout;
    CoordinatorLayout coordinatorLayout;
    public TabLayout tabLayout;
    static MainActivity mainActivity = MainActivity.instance;


    /* Models */
    public Movie movie;


    /* APIs */
    public RequestQueue mRequestQueue;


    /* Primitives */
    private int mMaxScrollSize;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 40;
    private boolean mIsAvatarShown = true;



    /* State */
    CoordinatorLayout.LayoutParams appBarParams;
    AppBarLayout.Behavior appBarBehavior;
    Parcelable toolbarState;



    /* Overrides */
    @Override
    public Intent getIntent() {
        return super.getIntent();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        instance = this;
        movie = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);



        //Start Volley Service and Get Movie Tagline
        mRequestQueue = Volley.newRequestQueue(instance);


        if(appBarLayout != null) {
            appBarParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarBehavior = (AppBarLayout.Behavior) appBarParams.getBehavior();
        }

        //Populate and Animate UI
        inflateUI();
        String message = getString(R.string.fab_scroll_instructions);
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //Inflate Detail Menu
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(appBarBehavior != null) {
            toolbarState = appBarBehavior.onSaveInstanceState(coordinatorLayout, appBarLayout);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        if(appBarBehavior != null && appBarLayout != null && toolbarState != null) {
            appBarBehavior.onRestoreInstanceState(coordinatorLayout, appBarLayout, toolbarState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    //For Coordinator Layout Animation
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if(mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if(percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            ((Toolbar) findViewById(R.id.toolbarNoParalax)).setVisibility(View.VISIBLE);
            backdrop.animate()
                    .scaleY(0).scaleX(0)
                    .setDuration(200)
                    .start();

        }



        if(percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;
            ((Toolbar) findViewById(R.id.toolbarNoParalax)).setVisibility(View.INVISIBLE);
            backdrop.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }

        if (i == 0) {
            fab.setVisibility(View.GONE);
        }
        else {
            fab.setVisibility(View.VISIBLE);
        }
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            appBarLayout.setExpanded(false, false);
        }


    }


    @Override
    public void onBackPressed() {
        if(mainActivity != null && mainActivity.activeId == R.id.action_favorites) {
            mainActivity.favMovies.clear();
            mainActivity.movieAdapter.clearItems();
            mainActivity.getFavorites();
            mainActivity.movieAdapter = new MovieAdapter(mainActivity.getApplicationContext(), mainActivity.favMovies);
            mainActivity.recyclerView.setAdapter(mainActivity.movieAdapter);
            mainActivity.movieAdapter.notifyDataSetChanged();
        }
        super.onBackPressed();
    }







    /* Helpers */
    public static void start(Context c) {
        c.startActivity(new Intent(c, DetailActivity.class));
    }


    private void inflateUI() {


        //Setup Toolbars
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar2 = (Toolbar) findViewById(R.id.toolbarNoParalax);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar2.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        if(getSupportActionBar()!= null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("");




        //Setup Detail Landing Page
        if(appBarLayout != null) {
            appBarParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            appBarBehavior = (AppBarLayout.Behavior) appBarParams.getBehavior();
        }
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        appBarLayout.addOnOffsetChangedListener(this);




        //Setup Child Views
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.detailActivity);
        ((RatingBar) findViewById(R.id.rating)).setRating(movie.rating / 2f);
        ((TextView) findViewById(R.id.ratingTextView)).setText(getString(R.string.rating_value, (float) Math.round(movie.rating * 10d) / 10d));
        ((TextView) findViewById(R.id.movie_title)).setText(movie.displayName);
        backdrop = findViewById(R.id.header_backdrop);
        Picasso.with(getApplicationContext()).load(movie.posterUrl).
                placeholder(R.mipmap.ic_launcher).into((ImageView) findViewById(R.id.header_backdrop));
        VolleyRequests.getMovieTagline(movie.id, instance);


        //Set Landing Page Scroll
        fabOnScroll = (FloatingActionButton) findViewById(R.id.fabScrollUp);
        fabOnScroll.setOnClickListener(new View.OnClickListener() {
            final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
            @Override
            public void onClick(View view) {
                appBarLayout.setExpanded(false, true);
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new ClickOnFab(fab, movie, getApplicationContext()));



        //Inflate Tabs + Pager
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        String fontPath1 = "FontAwesome.otf";
        String fontPath2 = "FontAwesomeSolid.otf";
        String fontPath3 = "FontAwesomeBrands.otf";

        IconTextView infoIcon = new IconTextView(getApplicationContext(), fontPath2);
        IconTextView youTubeIcon = new IconTextView(getApplicationContext(), fontPath3);
        IconTextView commentsIcon = new IconTextView(getApplicationContext(), fontPath2);
        infoIcon.setText(R.string.icon_info);
        youTubeIcon.setText(R.string.icon_youtube);
        commentsIcon.setText(R.string.icon_comments);

        tabLayout.addTab(tabLayout.newTab().setCustomView(infoIcon));
        tabLayout.addTab(tabLayout.newTab().setCustomView(youTubeIcon));
        tabLayout.addTab(tabLayout.newTab().setCustomView(commentsIcon));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);



        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(3);
        final DetailTabsPagerAdapter adapter = new DetailTabsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));





        // Check if Movie is a favorite; Animate Fab
        DBAdapter moviesDB = new DBAdapter(getApplicationContext());
        if (instance != null) {
            boolean favStatus = moviesDB.isMovieFavorited(instance.getContentResolver(), movie.id);
            if (favStatus)
                fab.setImageDrawable(ContextCompat.getDrawable(instance, android.R.drawable.btn_star_big_on));
            else
                fab.setImageDrawable(ContextCompat.getDrawable(instance, android.R.drawable.btn_star_big_off));
        }
    }




}
