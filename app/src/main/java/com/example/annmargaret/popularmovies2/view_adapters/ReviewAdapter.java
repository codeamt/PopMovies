package com.example.annmargaret.popularmovies2.view_adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.annmargaret.popularmovies2.R;
import com.example.annmargaret.popularmovies2.models.Review;


import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    /* Vars */
    Context mContext;
    List<Review> reviewsList;



    /* Constructor */
    public ReviewAdapter(Context context, List<Review> list) {
        this.mContext = context;
        this.reviewsList = list;
    }



    /* Overrides */
    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    @Nullable
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);
        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ReviewViewHolder holder, final int position) {
        final Review review = reviewsList.get(position);
        holder.author.setText(review.author);
        holder.content.setText(review.content);
    }




    /* Helpers */
    public void add(Review review) { reviewsList.add(review); }

    public void setReviewsList(List<Review> newList) {
        reviewsList.clear();
        reviewsList = newList;
        notifyDataSetChanged();
    }


    // For the "Read More/Less Functionality of View
    private void collapseReviewView(final View view) {
        final TextView contentView = (TextView) view.findViewById(R.id.reviewContent);
        if (contentView != null) {
            contentView.post(new Runnable() {
                @Override
                public void run() {
                    if (contentView.getLineCount() <= 5) {
                        ((TextView) view.findViewById(R.id.statusCollapsed)).setVisibility(View.GONE);
                        ((CardView) view.findViewById(R.id.readmorewrap)).setVisibility(View.GONE);
                    } else {
                        contentView.setMaxLines(5);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                TextView statusView = (TextView) view.findViewById(R.id.statusCollapsed);
                                TextView contentView2 = (TextView) view.findViewById(R.id.reviewContent);
                                if (statusView.getText().equals(mContext.getResources().getString(R.string.text_more))) {
                                    contentView2.setMaxLines(10000);
                                    statusView.setText(mContext.getResources().getString(R.string.text_less));
                                } else {
                                    contentView2.setMaxLines(5);
                                    statusView.setText(mContext.getResources().getString(R.string.text_more));
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    /* Custom ViewHolder for Review */
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView content;
        TextView collapseButton;
        public ReviewViewHolder(View view) {
            super(view);
            author = (TextView) view.findViewById(R.id.reviewAuthor);
            content = (TextView) view.findViewById(R.id.reviewContent);
            collapseButton = (TextView) view.findViewById(R.id.statusCollapsed);
            collapseReviewView(view);
        }
    }
}
