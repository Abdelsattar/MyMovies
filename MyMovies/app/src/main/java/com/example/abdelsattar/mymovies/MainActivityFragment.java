package com.example.abdelsattar.mymovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";


    ArrayList<Review> reviews;
    ArrayList<Video> videos;
    private ImageAdapter mGridAdapter;
    private ArrayList<Movie> mGridData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);

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

                //Pass the movie details DetailsActivity
                intent.putExtra("title"     , item.getTitle()).
                        putExtra("overview" , item.getOverview()).
                        putExtra("rating"   , item.getRating()).
                        putExtra("rDate"    , item.getReleaseDate()).
                        putExtra("id"    , item.getMovieID()).
                        putExtra("pURL"     , item.getPosterURL()).
                        putExtra("pURL"     , item.getPosterURL());

                mPosition = position;
                //Start details activity
                startActivity(intent);

            }
        });

        //--------------------

        if(savedInstanceState == null || !savedInstanceState.containsKey(SELECTED_KEY)) {

            mProgressBar.setVisibility(View.VISIBLE);
            //getting the setting value
            String Sort_By = getPreferredLocation(getActivity());
            // favourite
            String Fav =getString(R.string.pref_sort_favourite);
            String pop =getString(R.string.pref_sort_popular);
            String rate =getString(R.string.pref_sort_rate);


            Toast.makeText(getActivity(),
                    "hey_there "+Sort_By ,
                    Toast.LENGTH_SHORT)
                    .show();
            if ( Sort_By.contentEquals(pop)  || Sort_By.contentEquals(rate) ) {
                Toast.makeText(getActivity(),
                        "hey_there "+Sort_By ,
                        Toast.LENGTH_SHORT)
                        .show();
                //  Log.d("Sort BY",Sort_By);
                mGridData.clear();
                new FetchMoviesTask().execute(Sort_By);
            }
            else {
                SharedPreferences pref =
                        getActivity().getSharedPreferences(
                                getString(R.string.pref_movie_name),
                                Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
//            Log.d("Entered","favourtie");
                String fM  = pref.getString(getString(R.string.pref_movie_key),
                        "");
                // Log.d("Getting Movie ",fM );

                String fMovies[] ;
                if(fM!=null){

                    fMovies = fM.split("#");
                    mGridData.clear();
                    String rMovies[], vMovies[]  ;
                    reviews = new ArrayList<>();
                    videos = new ArrayList<>();
                    String [] movieDetails;
                    String []reviewDetail;
                    String [] videoDetail;
                    Movie movieObj;
                    Review review;
                    Video video;


                    for(int i=0 ; i<fMovies .length ; i++){
                        movieDetails=fMovies[i].split("|");
                        movieObj = new Movie();

                        movieObj.setMovieID(movieDetails[0]);
                        movieObj.setPosterURL(movieDetails[1]);
                        movieObj.setBackgroundUrl(movieDetails[2]);
                        movieObj.setTitle(movieDetails[3]);
                        movieObj.setOverview(movieDetails[4]);
                        movieObj.setReleaseDate(movieDetails[5]);
                        movieObj.setRating(movieDetails[6]);

                        Log.d("Movie From Prefrence",
                                " ---  "+movieDetails[0] +
                                        " --- "+movieDetails[1] +
                                        " ---"+movieDetails[2] +
                                        " ---- "+movieDetails[3] +
                                        " ---  "+movieDetails[4] +
                                        " --- "+movieDetails[5] +
                                        " --- "+movieDetails[6] );

                        rMovies = pref.getString(getString(R.string.pref_movie_name),
                                null).split("#");
                        for(int j=0 ; j <rMovies.length ; j++){
                            reviewDetail =rMovies[i].split("|");
                            review = new Review();
//                        Log.d("Review", "Author "+reviewDetail[0]
//                                        + "Content "+reviewDetail[1]);
                            review.setAuthor(reviewDetail[0]);
                            review.setContent(reviewDetail[1]);

                            reviews.add(review);
                        }
                        vMovies =pref.getString(getString(R.string.pref_movie_name),
                                null).split("#");
                        for(int j=0 ; j <vMovies.length ; j++){
                            videoDetail =vMovies[i].split("|");
                            video = new Video();

//                        Log.d("Videos", "Title "+videoDetail[0]
//                                + "URL "+videoDetail[1]);
                            video.setName(videoDetail[0]);
                            video.setUrl(videoDetail[1]);

                            videos.add(video);
                        }

                        mGridData.add(movieObj);
                    }
//                for(int q=0 ; q<mGridData.size() ; q++){
//
//                    Log.d("Favorite "+ q, "Backgorund "+mGridData.get(q).getBackgroundUrl()
//                                        + "Date " +mGridData.get(q).getReleaseDate()
//                                        + "Movie ID :" +mGridData.get(q).getMovieID()
//                                        + "Rting: "+mGridData.get(q).getRating()
//                                        +"Title" + mGridData.get(q).getTitle()
//                    );
//                }
                    mGridAdapter.setGridData(mGridData);
//                Log.d("Favorite", mGridData.toString());
                    Toast.makeText(getActivity(),
                            "Here are your Movies",
                            Toast.LENGTH_SHORT)
                            .show();

                }else {
                    Toast.makeText(getActivity(),
                            "You Don't have any Favourite",
                            Toast.LENGTH_SHORT)
                            .show();

                }
            }
        }else {


//            Toast.makeText(getActivity(),
//                    "hey  Else " ,
//                    Toast.LENGTH_SHORT)
//                    .show();
            mGridData = savedInstanceState.getParcelableArrayList(SELECTED_KEY);

            mGridAdapter = new ImageAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
            mGridView.setAdapter(mGridAdapter);
            Log.d("Main Grid" , mGridData.toString());
           // set
        }
        //----------------

        /*************************************************/
        return rootView;

    }

    /*
    @Override
    public void onStart() {
        super.onStart();

        //Start download

        /*

    }*/

    @Override
    public void onResume() {
        super.onResume();
       // onSortChanged();
    }

    void onSortChanged( ) {
        updateMovies();
     //   getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
 //       Log.d("Save instance ", mGridData.toString());

//        Toast.makeText(getActivity(),
//                "Here are your Movies",
//                Toast.LENGTH_SHORT)
//                .show();
        outState.putParcelableArrayList(SELECTED_KEY, mGridData);
        super.onSaveInstanceState(outState);
    }


    private void updateMovies() {
        FetchMoviesTask weatherTask = new FetchMoviesTask();
        String location = getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }

    public static String getPreferredLocation(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),
                context.getString(R.string.pref_sort_default));
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
            //vote_average.desc & popularity.desc"
            String sortTypePopular = "popularity.desc";
            String sortTypeRate = "vote_average.desc";

            try {
                // Here Built URL
                //http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&
                // api_key=8ab3b4fc3a364ee0e493c5b0aba84ed1
                //TODO here u must go the webiste and get your own key by regesiter in it
                final String Base_URLWithKey = "http://api.themoviedb.org/3/discover/movie?" +
                        "api_key=8ab3b4fc3a364ee0e493c5b0aba84ed1";
                final String SORT_PARAM = "sort_by";

                Uri builtUri = Uri.parse(Base_URLWithKey).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
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

              //  Log.v(LOG_TAG, "Movie string: " + movieJSONStr);
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