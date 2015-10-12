package com.example.abdelsattar.mymovies;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class detailMovieFragment extends Fragment implements  View.OnClickListener{
    private ArrayList<Object> reviewsData;
    private ArrayList<Object> videosData;
    static String ID_KEY="id_key";

    private ArrayList<Review> reviewsString;
    private ArrayList<Video> videosString;

    Movie movieObj;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;

    // now to fill this with data getting from API to use it in the Exapnadable list

    HashMap<String, List<Object>> listDataChild;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    public static detailMovieFragment newInstance(Bundle args)
    {
        detailMovieFragment fragmentInstance = new detailMovieFragment();
        if(args != null)
        {
            fragmentInstance.setArguments(args);
        }
        return fragmentInstance;
    }
/*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onResume() {
        super.onResume();
    }
    */

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        Bundle bundle = getActivity().getIntent().getExtras();
        movieObj = new Movie();


        TextView titleTV = (TextView) rootView.findViewById(R.id.titleTV);
        TextView overviewTV = (TextView) rootView.findViewById(R.id.overviewTV);
        TextView releaseDateTV = (TextView) rootView.findViewById(R.id.rDateTV);
        TextView ratingTV = (TextView) rootView.findViewById(R.id.ratingTV);

        //TODO implement this to not crash
        if(bundle !=null){
            movieObj.setTitle(bundle.getString("title"));
            movieObj.setMovieID(bundle.getString("id"));
            movieObj.setPosterURL(bundle.getString("pURL"));
            movieObj.setOverview(bundle.getString("overview"));
            movieObj.setRating(bundle.getString("rating"));
            movieObj.setRating(bundle.getString("rDate"));

            titleTV.setText(movieObj.getTitle());
            overviewTV.setText(movieObj.getOverview());
            releaseDateTV.setText(movieObj.getReleaseDate());
            ratingTV.setText(movieObj.getRating());


            ImageView bgImage = (ImageView) rootView.findViewById(R.id.bgImage);
            Picasso.with(getActivity())
                    .load(bundle.getString("pURL"))
                    .into(bgImage);

            FetchVideoDetailTask videoDetailTask = new FetchVideoDetailTask();
            videoDetailTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movieObj.getMovieID());

            FetchReviewDetailTask reviewDetailTask = new FetchReviewDetailTask();
            reviewDetailTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movieObj.getMovieID());
            /******************  implementing Expandable list   ************************** */

            // get the listview
            expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);

            // preparing list data

            listDataChild = new HashMap<String, List<Object>>();
            listDataHeader = new ArrayList<String>();
            listDataHeader.add("Videos");
            listDataHeader.add("Reviews");

            listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

            expListView.setAdapter(listAdapter);
            expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {

                    String UrlToView;
                    if(groupPosition==0){
                        Video video= (Video) listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition);
                        UrlToView = video.getUrl();
                    }
                    else {

                        Review review= (Review) listDataChild.get(listDataHeader.get(groupPosition))
                                .get(childPosition);
                        UrlToView = review.getUrl();
                    }
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(UrlToView)));
                    return false;
                }
            });

            expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,int groupPosition, long id) {
                    setListViewHeight(parent, groupPosition);
                    return false;
                }
            });


        }
        /****************** *****************************  ************************** */
        Button favourite = (Button) rootView.findViewById(R.id.favourite);
        favourite.setOnClickListener(this);

        return rootView;

   }


    @Override
    public void onClick(View view) {

              //  Log.d("Videos", "d5l favourite");
                // i will put all data of the movie in an shared prefrence based on id
                // and put also reviews videos details
                // will diffrenation bu delmiter
                SharedPreferences pref =
                        getActivity().getSharedPreferences(
                                getString(R.string.pref_movie_name),
                                Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();
                String Movies = pref.getString(getString(R.string.pref_movie_key), "");
                String  movieDetail = "";
               // Log.d("Movie from sharedprefrence", Movies);
                movieDetail= movieObj.getMovieID()+"|"
                            + movieObj.getPosterURL()+"|"
                            + movieObj.getBackgroundUrl()+"|"
                            + movieObj.getTitle()+"|"
                            + movieObj.getOverview()+"|"
                            + movieObj.getReleaseDate()+"|"
                            + movieObj.getRating()+'#';

                    boolean exist = pref.getBoolean(movieObj.getMovieID(), false);
                    if(!exist){
                        Movies+=movieDetail;
                        String videos="";
                        String reviews="";

                        editor.putString(getString(R.string.pref_movie_key),Movies);
                        editor.putBoolean(movieObj.getMovieID(),true);

                        //buliding videos
                        for(int i=0 ; i<videosString.size(); i++){
                            videos += videosString.get(i).getName()+"|"
                                    +videosString.get(i).getUrl()
                                    +'#';
                        }
                        //buliding Reviews
                        for(int i=0 ; i<reviewsString.size(); i++){
                            reviews += reviewsString.get(i).getAuthor()+"|"
                                    +reviewsString.get(i).getContent()+"|"
                                    +reviewsString.get(i).getUrl()+
                                    +'#';
                        }

                        editor.putString( movieObj.getMovieID()+'|'
                                          +"videos"
                                ,Movies);
                        editor.putString( movieObj.getMovieID()+'|'
                                          +"reviews"
                                ,reviews);
                        Log.d("Videos", videos);
                        Log.d("Reviews", reviews);
                        editor.commit();

//                        String mLOL = pref.getString("Movies","HEY");

//                        Log.d("mMOvies from pref when click", mLOL);
//                        Log.d("Reviewsfrom pref when click", reviews);
//

    //                }

                }
    }

    private void setListViewHeight(ExpandableListView listView, int group) {
        ExpandableListAdapter listAdapter =
                (ExpandableListAdapter) listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

    }


    public class FetchVideoDetailTask extends AsyncTask<String, Void, Video[]> {

        private final String LOG_TAG = FetchVideoDetailTask.class.getSimpleName();


      @Override
      protected void onPostExecute(Video[] result) {
          //  /**************
          if (result != null) {
              Toast.makeText(getActivity(),
                      "videos is processing now!",
                      Toast.LENGTH_SHORT).
                      show();

              videosData = new ArrayList<Object>(Arrays.asList(result));
              videosString = new ArrayList<Video>(Arrays.asList(result));
              //           mGridAdapter.setGridData(mGridData);
              listDataChild.put(listDataHeader.get(0), videosData);

          } else {
              Toast.makeText(getActivity(),
                      "Failed to fetch videos!",
                      Toast.LENGTH_SHORT).
                      show();
          }
          //  ********************/
          // New data is back from the server.  Hooray!
          //        mProgressBar.setVisibility(View.GONE);

      }
        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Video[] getvideoDataFromJson(String videoJsonStr)
                throws JSONException {

//            videosObjects = new ArrayList<Object>();

            final String Results_ID = "results";
            final String Video_ID = "id";
            final String Name_ID = "name";
            final String Site_ID = "site";
            final String Type_ID = "type";
            final String Video_URLKey_ID = "key";
            final String Video_URL_Base = "https://www.youtube.com/watch?v=";

            JSONObject VideoJson = new JSONObject(videoJsonStr);
            JSONArray videoArray = VideoJson.getJSONArray(Results_ID);


            Video[] resultStrs = new Video[videoArray.length()];
            String id, name,site,type,key,videoURL;
            Video videoObj;

            for (int i = 0; i < videoArray.length(); i++) {
                // Get the JSON object representing the day
                JSONObject reviewDetails = videoArray.getJSONObject(i);

                id = reviewDetails.getString(Video_ID);
                name = reviewDetails.getString(Name_ID);
                type = reviewDetails.getString(Type_ID);
                site = reviewDetails.getString(Site_ID);
                key = reviewDetails.getString(Video_URLKey_ID);

                videoURL = Video_URL_Base +key;
                videoObj = new Video();
                videoObj.setId(id);
                videoObj.setName(name);
                videoObj.setSite(site);
                videoObj.setUrl(videoURL);
                videoObj.setType(type);

              //  Log.v(LOG_TAG, " video string: " +name +" "+ type+"  " +site+" "+videoURL);
                resultStrs[i] = videoObj;
       //         videosObjects.add(videoObj);
//                videosData.add(videoObj);
            }
            return resultStrs;
        }

        /**
         *
         * @param params the first one will be the id of
         *               the movie we want to get it's vidos
         * @return
         */
        @Override
        protected Video[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String videoJSONStr = null;

            try {
                // Here Built URL
                //http://api.themoviedb.org/3/movie/[movie_ID]/reviews?api_key=[Your Key]
                //http://api.themoviedb.org/3/movie/[mocide_ID]/videos?api_key=[Your Key]
                //TODO here u must go the webiste and get your own key by regesiter in it
                final String Base_URLWithKey = "http://api.themoviedb.org/3/movie/" + params[0]+
                        "/videos?api_key=8ab3b4fc3a364ee0e493c5b0aba84ed1";

                Uri builtUri = Uri.parse(Base_URLWithKey).buildUpon()
                        .build();
                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
//                Toast.makeText(getActivity(),
//                        "No available data ",
//                        Toast.LENGTH_SHORT)
//                        .show();
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
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
                videoJSONStr = buffer.toString();
                Log.v(LOG_TAG, "Movie string: " + videoJSONStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
                return getvideoDataFromJson(videoJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

    }


    public class FetchReviewDetailTask extends AsyncTask<String, Void, Review[]> {

        private final String LOG_TAG = FetchReviewDetailTask.class.getSimpleName();

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * <p/>
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Review[] getMovieDataFromJson(String reviewJsonStr)
                throws JSONException {

            final String Results_ID = "results";
            final String Review_ID = "id";
            final String Author_ID = "author";
            final String Content_ID = "content";
            final String Review_URL_ID = "url";

            JSONObject ReviewJson = new JSONObject(reviewJsonStr);
            JSONArray reviewArray = ReviewJson.getJSONArray(Results_ID);


            Review[] resultStrs = new Review[reviewArray.length()];
            String content, author,id, reviewUrl;


            Review reviewObj;
            Movie item = new Movie();
            for (int i = 0; i < reviewArray.length(); i++) {
                // Get the JSON object representing the day
                JSONObject reviewDetails = reviewArray.getJSONObject(i);

                id = reviewDetails.getString(Review_ID);
                content = reviewDetails.getString(Content_ID);
                author = reviewDetails.getString(Author_ID);
                reviewUrl = reviewDetails.getString(Review_URL_ID);

                reviewObj = new Review();
                reviewObj.setId(id);
                reviewObj.setAuthor(author);
                reviewObj.setContent(content);
                reviewObj.setUrl(reviewUrl);

//                  Log.v(LOG_TAG, "Movie string: " + );
                resultStrs[i] = reviewObj;
               // reviewsData.add(reviewObj);
            }
            return resultStrs;
        }

        /**
         *
         * @param params the first one will be the id of
         *               the movie we want to get it's review
         * @return
         */
        @Override
        protected Review[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String reviewJSONStr = null;

            try {
                // Here Built URL
                //http://api.themoviedb.org/3/movie/[movie_ID]/reviews?api_key=[Your Key]
                //TODO here u must go the webiste and get your own key by regesiter in it
                final String Base_URLWithKey = "http://api.themoviedb.org/3/movie/" + params[0]+
                        "/reviews?api_key=8ab3b4fc3a364ee0e493c5b0aba84ed1";

                Uri builtUri = Uri.parse(Base_URLWithKey).buildUpon()
                        .build();
                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());
                // Create the request to themoviedb, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


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
                reviewJSONStr = buffer.toString();
                Log.v(LOG_TAG, "Movie string: " + reviewJSONStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
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
                return getMovieDataFromJson(reviewJSONStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(Review[] result) {
            //  /**************
            if (result != null) {
                Toast.makeText(getActivity(),
                        "Reviews is processing now!",
                        Toast.LENGTH_SHORT).
                        show();

                reviewsData = new ArrayList<Object>(Arrays.asList(result));
                reviewsString = new ArrayList<Review>(Arrays.asList(result));

                //           mGridAdapter.setGridData(mGridData);
                // put the data under the list Group
                listDataChild.put(listDataHeader.get(1), reviewsData);


            } else {
                Toast.makeText(getActivity(),
                        "Failed to fetch Reviews!",
                        Toast.LENGTH_SHORT).
                        show();
            }
            //  ********************/
            // New data is back from the server.  Hooray!
            //        mProgressBar.setVisibility(View.GONE);

        }
    }



}
