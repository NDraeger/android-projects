package com.ndraeger.storify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FragmentManager fragmentManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment newFragment = null;
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);
            boolean shouldTransition = true;

            switch(item.getItemId()) {
                case R.id.navigation_headlines:
                    if(currentFragment instanceof HeadlinesFragment)
                        shouldTransition = false;
                    else
                        newFragment = HeadlinesFragment.newInstance();
                    break;
                case R.id.navigation_favorites:
                    if(currentFragment instanceof FavoritesFragment)
                        shouldTransition = false;
                    else
                        newFragment = new FavoritesFragment();
                    break;
                case R.id.navigation_settings:
                    if(currentFragment instanceof SettingsFragment)
                        shouldTransition = false;
                    else
                        newFragment = new SettingsFragment();
                    break;
                default:
                    return false;
            }

            if(shouldTransition) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, newFragment)
                        .commit();
            }

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if(fragment == null) {
            fragment = new HeadlinesFragment();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

}
