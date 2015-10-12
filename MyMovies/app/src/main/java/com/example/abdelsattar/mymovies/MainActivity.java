package com.example.abdelsattar.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.OnItemClickedListener {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;
    private boolean mPosition;
    private String mSort;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.movie_detail_container) != null)
        {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // If we're being restored from a previous state, don't need to do anything
            // and should return or else we could end up with overlapping fragments.
            if (savedInstanceState != null)
            {
                return;
            }
            else
            {
                // In two-pane mode, show the detail view in this activity by
                // adding or replacing the detail fragment using a fragment transaction.
                //Needs ad id to select automatically
                showDetailFragment(null);
            }
        }
        else
        {
            mTwoPane = false;
        }

        showMainFragment(null);
    }

    private void showDetailFragment(Bundle args)
    {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        detailMovieFragment fragment
                = detailMovieFragment.newInstance(args);

        ft.replace(R.id.movie_detail_container, fragment)
                .commit();
    }

    private void showMainFragment(Bundle args)
    {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        MainActivityFragment fragment
                = MainActivityFragment.newInstance(args);

        ft.add(R.id.fragment, fragment);
        ft.commit();
    }
 /*
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
*/
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
    public void OnItemClicked(Movie item) {

        if (mTwoPane)
        {
            // In two-pane mode, show the detail view in this activity by adding or replacing the
            // detail fragment using a fragment transaction.
            Bundle args = new Bundle();
            //Pass the movie details DetailsActivity
                    args.putString("title"       , item.getTitle());
                    args.putString("overview" , item.getOverview());
                    args.putString("rating"   , item.getRating());
                    args.putString("rDate"    , item.getReleaseDate());
                    args.putString("id"       , item.getMovieID());
                    args.putString("pURL"     , item.getPosterURL());
                    args.putString("pURL"     , item.getPosterURL());

            showDetailFragment(args);
        }
        else
        {
            Intent intent = new Intent(getApplication(), detailMovie.class);

            //Pass the movie details DetailsActivity
            intent.putExtra("title"     , item.getTitle()).
                    putExtra("overview" , item.getOverview()).
                    putExtra("rating"   , item.getRating()).
                    putExtra("rDate"    , item.getReleaseDate()).
                    putExtra("id"    , item.getMovieID()).
                    putExtra("pURL"     , item.getPosterURL()).
                    putExtra("pURL"     , item.getPosterURL());
          //  mPosition = position;
            //Start details activity
            startActivity(intent);
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
