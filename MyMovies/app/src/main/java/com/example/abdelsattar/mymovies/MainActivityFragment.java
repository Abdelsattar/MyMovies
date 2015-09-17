package com.example.abdelsattar.mymovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayAdapter<String> mMovieAdapter;

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private ImageAdapter mGridAdapter;
    private ArrayList<Movie> mGridData;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        /********************** new code******************/

        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new ImageAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                Movie item = (Movie) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), detailMovie.class);
//                              ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);
//
//                // Interesting data to pass across are the thumbnail size/location, the
//                // resourceId of the source bitmap, the picture description, and the
//                // orientation (to avoid returning back to an obsolete configuration if
//                // the device rotates again in the meantime)
//
//                int[] screenLocation = new int[2];
//                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("title"     , item.getTitle()).
                        putExtra("overview" , item.getOverview()).
                        putExtra("rating"   , item.getRating()).
                        putExtra("rDate"    , item.getReleaseDate()).
                        putExtra("bgURL"    , item.getBackgroundUrl()).
                        putExtra("pURL"     , item.getPosterURL());

                //Start details activity
                startActivity(intent);
            }
        });


        //Start download
        new FetchMoviesTask().execute();
        mProgressBar.setVisibility(View.VISIBLE);

        /*************************************************/
        return rootView;

    }


    public class FetchMoviesTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private String[] getMovieDataFromJson(String MovieJsonStr)
                throws JSONException {

            final String Results_ID = "results";
            final String Movie_ID = "id";
            final String Title_ID = "original_title";
            final String Poster_URL_ID = "poster_path";
            final String Background_URL_ID = "backdrop_path";
            final String OverView_ID = "overview";
            final String Rating_ID = "vote_average";
            final String release_Date_ID = "release_date";
            final String baseImageUrl = "http://image.tmdb.org/t/p/";
            final String imageSizeForUrl = "w185/";

            JSONObject MovieJson = new JSONObject(MovieJsonStr);
            JSONArray moviesArray = MovieJson.getJSONArray(Results_ID);


            String[] resultStrs = new String[moviesArray.length()];
            String title, posterURL, overview, rating,
                    releaseDate, backgroundURL,movieID;

            Movie movieObj;
            Movie item = new Movie();
            for (int i = 0; i < moviesArray.length(); i++) {
                // Get the JSON object representing the day
                JSONObject MovieDetails = moviesArray.getJSONObject(i);

                title = MovieDetails.getString(Title_ID);
                posterURL = MovieDetails.getString(Poster_URL_ID);
                overview = MovieDetails.getString(OverView_ID);
                rating = MovieDetails.getString(Rating_ID);
                releaseDate = MovieDetails.getString(release_Date_ID);
                backgroundURL = MovieDetails.getString(Background_URL_ID);
                movieID = MovieDetails.getString(Movie_ID);

                posterURL = baseImageUrl + imageSizeForUrl + posterURL;
                backgroundURL = baseImageUrl + imageSizeForUrl + backgroundURL;

                String details = posterURL
                        + "|" + backgroundURL
                        + "|" + title
                        + "|" + overview
                        + "|" + rating
                        + "|" + releaseDate
                        + "|" + movieID;

                movieObj = new Movie();
                movieObj.setTitle(title);
                movieObj.setOverview(overview);
                movieObj.setRating(rating);
                movieObj.setReleaseDate(releaseDate);
                movieObj.setBackgroundUrl(backgroundURL);
                movieObj.setPosterURL(posterURL);
                movieObj.setMovieID(movieID);

                Log.v(LOG_TAG, "Movie string: " + details);
                resultStrs[i] = details;

                mGridData.add(movieObj);
            }
            return resultStrs;
        }

        @Override
        protected String[] doInBackground(String... params) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String movieJSONStr = null;
            String sortType = "popularity.desc";

            try {
                // Here Built URL
                //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&
                // api_key=8ab3b4fc3a364ee0e493c5b0aba84ed1
                //TODO here u must go the webiste and get your own key by regesiter in it
                final String Base_URLWithKey = "http://api.themoviedb.org/3/discover/movie?" +
                        "api_key=8ab3b4fc3a364ee0e493c5b0aba84ed1";
                final String SORT_PARAM = "sort_by";

                Uri builtUri = Uri.parse(Base_URLWithKey).buildUpon()
                        .appendQueryParameter(SORT_PARAM, sortType)
                        .build();
                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to themoviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    Toast.makeText(getActivity(),
                            "No available data ",
                            Toast.LENGTH_SHORT)
                            .show();
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    Toast.makeText(getActivity(),
                            "the buffer is empty  ",
                            Toast.LENGTH_SHORT)
                            .show();
                    return null;
                }
                movieJSONStr = buffer.toString();

                Log.v(LOG_TAG, "Movie string: " + movieJSONStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the Movie data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(movieJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            //  /**************
            if (result != null) {
                Toast.makeText(getActivity(),
                        "Data is processing now!",
                        Toast.LENGTH_SHORT).
                        show();

                mGridAdapter.setGridData(mGridData);

            } else {
                Toast.makeText(getActivity(),
                        "Failed to fetch data!",
                        Toast.LENGTH_SHORT).
                        show();
            }
            //  ********************/
            // New data is back from the server.  Hooray!
            mProgressBar.setVisibility(View.GONE);

        }
    }

}