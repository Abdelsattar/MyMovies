package com.example.abdelsattar.mymovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class detailMovie extends AppCompatActivity {

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                     .add(R.id.movie_detail_container,
                             new detailMovieFragment())
                    .commit();
        }

    }*/
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_movie);
        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment) != null)
        {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null)
            {
                return;
            }
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            else
            {
                int id = getIntent().getIntExtra(detailMovieFragment.ID_KEY, 0);
                //int date = getIntent().getIntExtra(DetailsFragment.ID_KEY);
                Log.w("TWEETID", "" + id);

                Bundle args = new Bundle();
                args.putInt(detailMovieFragment.ID_KEY, id);
                showTweetDetailFragment(args);

            }
        }
    }
    private void showTweetDetailFragment(Bundle args)
    {
        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        detailMovieFragment fragment
                =  detailMovieFragment.newInstance(args);

        ft.add(R.id.fragment, fragment);
        ft.commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_movie, menu);
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
