package com.ndraeger.storify;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HeadlinesFragment extends Fragment {


    public HeadlinesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_headlines, container, false);
    }


    public static HeadlinesFragment newInstance() {

        Bundle args = new Bundle();

        HeadlinesFragment fragment = new HeadlinesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
