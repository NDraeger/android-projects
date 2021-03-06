package com.ndraeger.storify.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ndraeger.storify.R;


public class FavoritesFragment extends Fragment {


    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getAppBarLayout().setElevation(0);
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }


    public static FavoritesFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
