package com.ndraeger.storify.ui;


import android.app.Activity;
import android.content.Context;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.ndraeger.storify.R;
import com.ndraeger.storify.network.ArticlesResponse;
import com.ndraeger.storify.network.NetworkTransmissionCoordinator;
import com.ndraeger.storify.news.Article;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HeadlinesFragment extends Fragment {

    private final String FETCH_HEADLINES_URL = "https://newsapi.org/v2/top-headlines?country=%s&apiKey=%s";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView headlinesRecylerView;
    private ArticlesAdapter headlinesAdapter;
    private RecyclerView.LayoutManager headlinesLayoutManager;
    private ProgressBar headlinesProgressBar;
    private RelativeLayout connectionErrorLayout;
    private Button connectionErrorButton;

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
        headlinesAdapter = new ArticlesAdapter(articles, getContext());
        headlinesRecylerView.setAdapter(headlinesAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.headlines_swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchHeadlines();
            }
        });

        headlinesProgressBar = (ProgressBar) rootView.findViewById(R.id.headlines_progress_bar);

        connectionErrorLayout = (RelativeLayout) rootView.findViewById(R.id.connection_error_layout);
        connectionErrorButton = (Button) rootView.findViewById(R.id.connection_error_button);
        connectionErrorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectionErrorLayout.setVisibility(View.GONE);
                headlinesProgressBar.setVisibility(View.VISIBLE);
                fetchHeadlines();
            }
        });

        ((AppCompatActivity)getActivity()).setSupportActionBar((Toolbar) getActivity().findViewById(R.id.main_activity_toolbar));

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
                        Gson gson = new Gson();
                        ArticlesResponse articlesResponse = gson.fromJson(response.toString(), ArticlesResponse.class);
                        headlinesAdapter.updateDataSet(articlesResponse.getArticles());
                        swipeRefreshLayout.setRefreshing(false);
                        headlinesProgressBar.setVisibility(View.GONE);
                        swipeRefreshLayout.setVisibility(View.VISIBLE);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        final Activity activity = getActivity();
                        headlinesProgressBar.setVisibility(View.GONE);
                        connectionErrorLayout.setVisibility(View.VISIBLE);
                    }
                });
        NetworkTransmissionCoordinator.getInstance(getActivity().getApplicationContext()).addToRequestQueue(fetchHeadlinesRequest);
    }

}
