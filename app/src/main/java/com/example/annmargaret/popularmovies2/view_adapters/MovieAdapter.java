package com.example.annmargaret.popularmovies2.view_adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import com.example.annmargaret.popularmovies2.views_ui.DetailActivity;
import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements Filterable {
    /* Vars */
    List<Movie> moviesList;
    List<Movie> moviesListCopy;
    public Context mContext;


    /* Constructor */
    public MovieAdapter(Context context, List<Movie> movies) {
        this.moviesList = movies;
        this.moviesListCopy = movies;
        this.mContext = context;
        getFilter();
    }



    /* Overrides */
    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {
        Picasso.with(mContext)
                .load(moviesList.get(position).getPosterUrl())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.movie_poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetailsActivity(position);
            }
        });

    }


    @Nullable
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_grid_item, parent, false);
        return new MovieViewHolder(itemView);

    }


    //For Search Filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                moviesList = (List<Movie>) results.values;
                MovieAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Movie> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = moviesListCopy;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }



    /* Helpers */
    public void add(Movie movie) { moviesList.add(movie); notifyItemInserted(getItemCount() - 1); }
    public void clearItems() { moviesList.clear(); notifyDataSetChanged(); }
    public void setMoviesList(List<Movie> update) {
        moviesList = update;
    }


    public void goToDetailsActivity(int position) {
        Intent getDetails = new Intent(mContext, DetailActivity.class);
        getDetails.putExtra(Intent.EXTRA_TEXT, (Parcelable) moviesList.get(position));
        mContext.startActivity(getDetails);
    }


    protected List<Movie> getFilteredResults(String constraint) {
        List<Movie> results = new ArrayList<>();
        if (moviesList != null && moviesList.size() > 0 && constraint != null)
        for (Movie movie : moviesList) {
            if (movie.getTitle().toLowerCase().contains(constraint) || movie.getOverview().toLowerCase().contains(constraint)) {
                results.add(movie);
            }
        }
        return results;
    }




    /* Custom ViewHolder for Movie Item */
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView movie_poster;


        public MovieViewHolder(View view) {
            super(view);
            movie_poster = view.findViewById(R.id.movie_poster);
            movie_poster.setPadding(0, 0, 0, 0);
            movie_poster.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            movie_poster.setAdjustViewBounds(true);
        }
    }




}
