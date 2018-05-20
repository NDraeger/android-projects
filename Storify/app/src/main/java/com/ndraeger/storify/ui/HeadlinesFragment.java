package com.ndraeger.storify.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ndraeger.storify.R;
import com.ndraeger.storify.network.NetworkTransmissionCoordinator;
import com.ndraeger.storify.news.Article;
import com.ndraeger.storify.news.MinimalSource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HeadlinesFragment extends Fragment {

    private final String FETCH_HEADLINES_URL = "https://newsapi.org/v2/top-headlines?country=%s&apiKey=%s";

    private RecyclerView headlinesRecylerView;
    private RecyclerView.Adapter headlinesAdapter;
    private RecyclerView.LayoutManager headlinesLayoutManager;

    public HeadlinesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_headlines, container, false);

        headlinesRecylerView = (RecyclerView) rootView.findViewById(R.id.headlines_recycler_view);

        headlinesLayoutManager = new LinearLayoutManager(getContext());
        headlinesRecylerView.setLayoutManager(headlinesLayoutManager);

        List<Article> articles = new ArrayList<>();
        articles.add(new Article(new MinimalSource("asdf", "asdfakg"), "Maxi Maier", "Breaking News: Wastlnews is a coconut", "Lorem Ipsum", "www.asdfa.com", "asdfjahsd.com/img",new Date(1002,21,343)));

        headlinesAdapter = new HeadlinesAdapter(articles);
        headlinesRecylerView.setAdapter(headlinesAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchHeadlines();
    }

    public static HeadlinesFragment newInstance() {

        Bundle args = new Bundle();

        HeadlinesFragment fragment = new HeadlinesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String getFetchHeadlinesUrl(Context context) {
        return String.format(
                FETCH_HEADLINES_URL,
                context.getResources().getConfiguration().locale.getCountry(),
                context.getResources().getString(R.string.newsapi_api_key)
        );
    }

    private void fetchHeadlines() {
        JsonObjectRequest fetchHeadlinesRequest = new JsonObjectRequest
                (Request.Method.GET, getFetchHeadlinesUrl(getActivity()), null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        final Activity activity = getActivity();

                        Snackbar
                                .make(
                                    activity.findViewById(R.id.headlines_coordinator_layout),
                                    activity.getResources().getString(R.string.fetch_headlines_error),
                                    Snackbar.LENGTH_INDEFINITE)
                                .setAction(
                                        activity.getResources().getString(R.string.fetch_headlines_error_action),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                fetchHeadlines();
                                            }
                                        })
                                .show();
                    }
                });
        NetworkTransmissionCoordinator.getInstance(getActivity().getApplicationContext()).addToRequestQueue(fetchHeadlinesRequest);
    }

    private class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesAdapter.ViewHolder> {

        private List<Article> headlines;

        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView titleTextView;
            private TextView publishedAtTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.titleTextView = (TextView) itemView.findViewById(R.id.list_item_headline_title_text_view);
                this.publishedAtTextView = (TextView) itemView.findViewById(R.id.list_item_headline_published_at_text_view);
            }
        }

        public HeadlinesAdapter(List<Article> headlines) {
            this.headlines = headlines;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_headline, parent, false);
            ViewHolder vh = new ViewHolder(itemView);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.titleTextView.setText(headlines.get(position).getTitle());
            holder.publishedAtTextView.setText(headlines.get(position).getPublishedAt().toString());
        }

        @Override
        public int getItemCount() {
            return headlines.size();
        }
    }
}
