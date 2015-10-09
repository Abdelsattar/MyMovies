package com.example.abdelsattar.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    private String mSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         mSort = MainActivityFragment.getPreferredSort(this);

        if (findViewById(R.id. movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
             mTwoPane = true;
             // In two-pane mode, show the detail view in this activity by
             // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                 getSupportFragmentManager().beginTransaction()
                         .replace(R.id.movie_detail_container, new detailMovieFragment(),
                                 DETAILFRAGMENT_TAG)
                         .commit();
                 }
             } else {
             mTwoPane = false;
            }
    }


    @Override
    protected void onResume() {
        super.onResume();
        String sort = MainActivityFragment.getPreferredSort( this );
//        update the sort in our second pane using the fragment manager
//        TODO reuse this code to fit to movie
        if (sort != null && !sort.equals(mSort)) {
            MainActivityFragment ff =
                          (MainActivityFragment)getSupportFragmentManager()
                              .findFragmentById(R.id.fragment);
            if ( null != ff ) {
                ff.onSortChanged();
            }
            mSort = sort;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
