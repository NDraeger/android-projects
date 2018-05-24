package com.ndraeger.storify.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ndraeger.storify.R;
import com.ndraeger.storify.network.NetworkTransmissionCoordinator;
import com.ndraeger.storify.news.Article;

import java.util.Calendar;
import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private Context context;
    private List<Article> articles;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView titleTextView;
        private TextView sourceTextView;
        private TextView publishedAtTextView;
        private RelativeLayout mainLayout;
        private RelativeLayout skeletonLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mainLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_article_main_layout);
            this.skeletonLayout = (RelativeLayout) itemView.findViewById(R.id.list_item_article_skeleton_layout);
            this.titleTextView = (TextView) itemView.findViewById(R.id.list_item_article_title_text_view);
            this.publishedAtTextView = (TextView) itemView.findViewById(R.id.list_item_article_published_at_text_view);
            this.imageView = (ImageView) itemView.findViewById(R.id.list_item_article_image);
            this.sourceTextView = (TextView) itemView.findViewById(R.id.list_item_article_source_text_view);
        }
    }

    public ArticlesAdapter(List<Article> articles, Context context) {
        this.articles = articles;
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            holder.titleTextView.setText(articles.get(position).getTitle());
            holder.publishedAtTextView.setText(
                    DateUtils.getRelativeTimeSpanString(
                            articles.get(position).getPublishedAt().getTime(),
                            Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS
                    )
            );
            holder.sourceTextView.setText(articles.get(position).getSource().getName());

            if(articles.get(position).getUrlToImage() != null) {
                NetworkTransmissionCoordinator.getInstance(context).getImageLoader().get(articles.get(position).getUrlToImage(), new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        if (response.getBitmap() != null) {
                            holder.imageView.setImageBitmap(response.getBitmap());
                            holder.skeletonLayout.setVisibility(View.GONE);
                            holder.mainLayout.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
            } else {
                holder.skeletonLayout.setVisibility(View.GONE);
                holder.mainLayout.setVisibility(View.VISIBLE);
            }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void updateDataSet(List<Article> newArticles) {
        this.articles = newArticles;
        notifyDataSetChanged();
    }
}
