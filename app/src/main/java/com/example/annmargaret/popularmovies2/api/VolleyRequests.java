package com.example.annmargaret.popularmovies2.api;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.models.MovieExtras;
import com.example.annmargaret.popularmovies2.views_ui.DetailActivity;
import com.example.annmargaret.popularmovies2.views_ui.FragmentInfoTab;
import com.example.annmargaret.popularmovies2.views_ui.FragmentReviewsTab;
import com.example.annmargaret.popularmovies2.views_ui.FragmentTrailerTab;
import com.example.annmargaret.popularmovies2.views_ui.MainActivity;
import com.example.annmargaret.popularmovies2.database.KeyStore;
import com.example.annmargaret.popularmovies2.models.Movie;
import com.example.annmargaret.popularmovies2.models.Review;
import com.example.annmargaret.popularmovies2.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class VolleyRequests {


    /* Empty Constructor */
    public VolleyRequests() {


    }


    /* Class Methods -- API Requests */


    //Get Popular or Highly-Rated Movies
    static public void getMovies(final MainActivity reference) {
        if(reference != null) {
            String url = "http://api.themoviedb.org/3/movie/" + reference.endpoint + "?api_key=" + KeyStore.API_KEY;
            final String LOG_TAG = "Get Trailer Request";
            JsonObjectRequest req = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray items = response.getJSONArray("results");
                                JSONObject movieObj;
                                for (int i = 0; i < items.length(); i++) {
                                    movieObj = items.getJSONObject(i);
                                    Movie movie = new Movie();
                                    movie.id = movieObj.getInt("id");
                                    movie.displayName = movieObj.getString("original_title");
                                    movie.overview = movieObj.getString("overview");
                                    movie.posterUrl = "http://image.tmdb.org/t/p/w185/" + movieObj.getString("poster_path");
                                    movie.releasedDate = movieObj.getString("release_date");
                                    movie.rating = (float) movieObj.getDouble("vote_average");
                                    movie.popularity = movieObj.getDouble("popularity");
                                    // Add image to adapter
                                    reference.movies.add(movie);
                                    if(reference.movieAdapter != null) {
                                        reference.movieAdapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG, "Error parsing data for Popular Movies");
                }
            });

            reference.mRequestQueue.add(req);

        }
    }

    //Get Movie Tagline
    static public void getMovieTagline(int id, final DetailActivity reference ) {
        if(reference != null) {
            String url = "http://api.themoviedb.org/3/movie/" + id + "?api_key=" + KeyStore.API_KEY + "&language=en-US";
            final String LOG_TAG = "Get Details Request";
            JsonObjectRequest req = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String tagline = response.getString("tagline");

                                //Populate the View with the Tagline
                                TextView taglineText = reference.findViewById(R.id.tagline);
                                if(taglineText != null) {
                                    taglineText.setText(tagline);
                                }


                                Log.v(LOG_TAG, "tagline: " + response.getString("tagline") );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG, "Something went wrong with the API call:" + error);
                }
            });

            reference.mRequestQueue.add(req);
            Log.v(LOG_TAG, "Movie Extras Fetched.");
        }
    }


    //Get Movie Rating
    static public void getRating(int id, final FragmentInfoTab reference ) {
        if(reference != null) {
            String url = "http://api.themoviedb.org/3/movie/" + id + "?api_key=" + KeyStore.API_KEY + "&language=en-US";
            final String LOG_TAG = "Get Details Request";
            JsonObjectRequest req = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int movieRuntime = response.getInt("runtime");

                                //Populate the Fragment with the Runtime
                                TextView runtimeText = reference.currentTab.findViewById(R.id.runtime);
                                if(runtimeText != null) {
                                    runtimeText.setText(reference.getString(R.string.runtime, movieRuntime));
                                }

                                Log.v(LOG_TAG, "runtime: " + response.getInt("runtime") );
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG, "Something went wrong with the API call:" + error);
                }
            });

            reference.mRequestQueue.add(req);
            Log.v(LOG_TAG, "Movie Extras Fetched.");
        }
    }



    //Get Movie Trailer
    static public void getTrailer(int id, final FragmentTrailerTab reference) {
        if(reference != null) {
            String url = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=" + KeyStore.API_KEY + "&language=en-US";
            final String LOG_TAG = "Get Trailer Request";
            JsonObjectRequest req = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray items = response.getJSONArray("results");
                                JSONObject trailer;

                                for (int i = 0; i < items.length(); i++) {

                                    trailer = items.getJSONObject(i);

                                    if (trailer.getString("type").equals("Trailer")) {
                                        Trailer trailer_item = new Trailer();
                                        trailer_item.id = trailer.getString("id");
                                        trailer_item.url = trailer.getString("key");
                                        trailer_item.label = trailer.getString("name");

                                        //For YouTube Fragment
                                        reference.trailerURL = trailer_item.url;
                                        reference.setVideoShareIntent(reference.trailerURL);

                                        Log.v(LOG_TAG, "Trailer Link: " + trailer_item.url + ", for " + trailer_item.label + " stored.");
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG, "Something went wrong with the API call:" + error);
                }
            });

            reference.mRequestQueue.add(req);
            Log.v(LOG_TAG, "Movie Trailer fetched.");
        }
    }




    //Get Movie Reviews
    static public void getReviews(int id, final FragmentReviewsTab reference) {
        if (reference != null) {
            String url = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=" + KeyStore.API_KEY;
            final String LOG_TAG = "Get Reviews Request";
            JsonObjectRequest req = new JsonObjectRequest(url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray items = response.getJSONArray("results");
                                JSONObject review;
                                for (int i = 0; i < items.length(); i++) {
                                    review = items.getJSONObject(i);
                                    Review review_item = new Review();
                                    review_item.author = review.getString("author");
                                    review_item.url = review.getString("url");
                                    review_item.content = review.getString("content");
                                    reference.reviews.add(review_item);
                                    reference.reviewAdapter.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOG_TAG, "Something went wrong with the API call:" + error);
                }
            });

            reference.mRequestQueue.add(req);
            Log.v(LOG_TAG, "Movie Reviews Fetched. Collapsing Card Views Created for Reviews.");

        }

    }

}
