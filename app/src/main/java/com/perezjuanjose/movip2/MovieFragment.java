package com.perezjuanjose.movip2;

import android.app.LoaderManager;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.perezjuanjose.movip2.data.FilmsColumns;
import com.perezjuanjose.movip2.data.FilmsProvider;

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
import java.util.Vector;


/**
 * A placeholder fragment containing a simple view.
 */


public class MovieFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>  {

    private static final int MOVIES_LOADER = 0;

    public static boolean preferenceHasChanged = false;
    static private String API_KEY  ="79424eca98daa0b906a464bf7d8f9f0f";
    private FilmsAdapter mMoviesAdapter;
    private ArrayList<Film> listMovies;
    private int moviePage;

    static  private String ORDER_BY = "order_by";//Preference to fetch the movie information in the web POPULARITY, VOTE AVERAGE, VOTE COUNT
    static  private String ASC_DESC = "asc_desc";//Preference to fetch the movie information in the web aSCENDENTE OR DESCENDENTE

    private String oldOrder_BY=null;
    private String oldASC_DEC=null;





    @Override
    public void onStart(){

        Log.i("Prererencias", "On Start");
        Log.i("Prererencias", "mthis" + preferenceHasChanged);
        if(preferenceHasChanged) {

            updateData();
            preferenceHasChanged=false;

            //updateData();
        }
        super.onStart();
   }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

        getLoaderManager().initLoader(MOVIES_LOADER, null, this);

        // Update data form the web ot read the saved datea
        if(savedInstanceState==null || !savedInstanceState.containsKey("Films")){

            updateData();


        }else{
           listMovies= savedInstanceState.getParcelableArrayList("Films");

            Log.i("Prererencias", "lee Parcelable");


        }
    }


    @Override
    public void onSaveInstanceState (Bundle outState) {
        //Save the data we read from the web. We don't need to fetch the information again when we rotate te phone.
        outState.putParcelableArrayList("Films", listMovies);
        super.onCreate(outState);
    }



    //Up date the Apater with the movis information reading the preferene
public void updateData(){

   // Read the Preference to call the web
    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String order_by=prefs.getString(ORDER_BY, "popularity.");
    String asc_desc = prefs.getString(ASC_DESC, "desc");

    Log.i("Prererencias", "order_by:" + order_by + "asc_desc: " + asc_desc);

    //Fetche the movis data
    FetchMoviesTask moviesTask = new FetchMoviesTask();
    moviesTask.execute(order_by, asc_desc);
}


    public MovieFragment() {

        listMovies = new ArrayList<Film>();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);


        Log.i("On create View", "Ejecutada");
        // List<Film> listMovies = new ArrayList<Film>(Arrays.asList(data));

        // Here we set the adapater.

        mMoviesAdapter =
                new FilmsAdapter(
                        getActivity(),
                        listMovies );
        View v= inflater.inflate(R.layout.moviefragment, container, false);

        // Get a reference to the View, and attach this adapter to it.
        GridView listView = (GridView) v.findViewById(R.id.movies_list);
        listView.setAdapter(mMoviesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getActivity(), MovieDetalle.class);

                intent.putExtra(Intent.EXTRA_TEXT, mMoviesAdapter.getItem(position).title);


                intent.putExtra("ADULTS", mMoviesAdapter.getItem(position).adults);
                intent.putExtra("BACKDROP_PATH", mMoviesAdapter.getItem(position).backdrop_path);
                intent.putExtra("ORIGINLANGUAJE", mMoviesAdapter.getItem(position).origianlLanguaje);
                intent.putExtra("ORIGINALTITLE", mMoviesAdapter.getItem(position).originalTitle);
                intent.putExtra("OVERVIEW", mMoviesAdapter.getItem(position).overview);
                intent.putExtra("RELEASEDATE", mMoviesAdapter.getItem(position).releaseDate);
                intent.putExtra("POSTERPATH", mMoviesAdapter.getItem(position).posterPath);
                intent.putExtra("POPULARITY", mMoviesAdapter.getItem(position).popularity);
                intent.putExtra("TITLE", mMoviesAdapter.getItem(position).title);
                intent.putExtra("VIDEO", mMoviesAdapter.getItem(position).video);
                intent.putExtra("VOTEAVERAGE", mMoviesAdapter.getItem(position).voteAverage);
                intent.putExtra("VOTECOUNT", mMoviesAdapter.getItem(position).vote_count);




                startActivity(intent);


            }
        });

        return v;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), FilmsProvider.Films.CONTENT_URI,
                null,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public class FetchMoviesTask extends AsyncTask<String, Void, Film[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();




        /**
         Extract information from de String abd return an Array of Film
         */
        private Film[] getFilmDataFromJson(String movieJsonStr)
                throws JSONException {

            int total_pages;
            int total_results;


            // These are the names of the JSON objects that need to be extracted.

            final String OWM_PAGE = "page";
            final String OWM_RESULTS = "results";
            final String OWM_TOTAL_PAGES = "total_pages";
            final String OWM_TOTAL_RESULTS = "total_results";
            final String OWM_ADULT = "adult";
            final String OWM_BACKDROP_PATH = "backdrop_path";
            final String OWM_ID = "id";
            final String OWM_ORIGINAL_LANGUAJE = "original_language";
            final String OWM_ORIGINAL_TITLE = "original_title";
            final String OWM_OVERVIEW = "overview";
            final String OWM_RELEASE_DATE = "release_date";
            final String OWM_POSTER_PATH = "poster_path";
            final String OWM_POPULARITY = "popularity";
            final String OWM_TITLE = "title";
            final String OWM_VIDEO = "video";
            final String OWM_VOTE_AVERAGE = "vote_average";
            final String OWM_VOTE_COUNT = "vote_count";



            // OWM returns the data number of pages and results.

            JSONObject pageMoviesJson = new JSONObject(movieJsonStr);
            total_pages=pageMoviesJson.getInt(OWM_TOTAL_PAGES);
            total_results=pageMoviesJson.getInt(OWM_TOTAL_RESULTS);
            Log.i(LOG_TAG, "Total pages:" + total_pages + "total results: " + total_results);
            JSONArray resultsArray = pageMoviesJson.getJSONArray(OWM_RESULTS);

            // OWM returns the data of the movies in an array
            // Insert the new film information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(resultsArray.length());

            Film[] resulMovies = new Film[resultsArray.length()];
            for(int i = 0; i < resultsArray.length(); i++) {
                // Thise are the variables of the class film
                Boolean adults;
                String backdrop_path;
                String origianlLanguaje;
                String originalTitle;
                String overview;
                String releaseDate;
                String posterPath;
                Double popularity;
                String title;
                Boolean video;
                Double voteAverage;
                int vote_count;

                // Get the JSON object representing list
                JSONObject movie = resultsArray.getJSONObject(i);
                    adults=movie.getBoolean(OWM_ADULT);
                    backdrop_path = movie.getString(OWM_BACKDROP_PATH);
                    origianlLanguaje=movie.getString(OWM_ORIGINAL_LANGUAJE);
                    originalTitle= movie.getString(OWM_ORIGINAL_TITLE);
                    overview=movie.getString(OWM_OVERVIEW);
                    releaseDate=movie.getString(OWM_RELEASE_DATE);
                    posterPath=movie.getString(OWM_POSTER_PATH);
                    popularity = movie.getDouble(OWM_POPULARITY);
                    title = movie.getString(OWM_TITLE);
                    video=movie.getBoolean(OWM_VIDEO);
                    voteAverage=movie.getDouble(OWM_VOTE_AVERAGE);
                    vote_count=movie.getInt(OWM_VOTE_COUNT);



                resulMovies[i]= new Film(adults,backdrop_path,origianlLanguaje,originalTitle,overview,releaseDate,posterPath,popularity,title,video,voteAverage,vote_count);

                Log.d(LOG_TAG, "insert");
                ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(1);


                    ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                            FilmsProvider.Films.CONTENT_URI);
                    builder.withValue(FilmsColumns.COLUMN_ADULTS, adults);
                    builder.withValue(FilmsColumns.COLUMN_BACKDROP_PATH, backdrop_path);
                    builder.withValue(FilmsColumns.COLUMN_ORIGINAL_LANGUAJE, origianlLanguaje);
                builder.withValue(FilmsColumns.COLUMN_ORIGINAL_TITLE, originalTitle);
                builder.withValue(FilmsColumns.COLUMN_OVERVIEW, overview);
                builder.withValue(FilmsColumns.COLUMN_RELEASE_DATE, releaseDate);
                builder.withValue(FilmsColumns.COLUMN_POSTER_PATH, posterPath);
                builder.withValue(FilmsColumns.COLUMN_POPULARITY, popularity);
                builder.withValue(FilmsColumns.COLUMN_TITLE, title);
                builder.withValue(FilmsColumns.COLUMN_VIDEO, video);
                builder.withValue(FilmsColumns.COLUMN_VOTE_AGERAGE, voteAverage);
                builder.withValue(FilmsColumns.COLUMN_VOTE_COUNT, vote_count);
                builder.withValue(FilmsColumns.COLUMN_VOTE_AGERAGE, voteAverage);
                    batchOperations.add(builder.build());


                try{
                    getActivity().getContentResolver().applyBatch(FilmsProvider.AUTHORITY, batchOperations);
                } catch(RemoteException | OperationApplicationException e){
                    Log.e(LOG_TAG, "Error applying batch insert", e);
                }







//
//                ContentValues filmValues = new ContentValues();
//
//                filmValues.put(FilmsColumns.COLUMN_ADULTS, adults);
//                filmValues.put(FilmsColumns.COLUMN_BACKDROP_PATH, backdrop_path);
//                filmValues.put(FilmsColumns.COLUMN_ORIGINAL_LANGUAJE, origianlLanguaje);
//                filmValues.put(FilmsColumns.COLUMN_ORIGINAL_TITLE, originalTitle);
//                filmValues.put(FilmsColumns.COLUMN_OVERVIEW, overview);
//                filmValues.put(FilmsColumns.COLUMN_RELEASE_DATE, releaseDate);
//                filmValues.put(FilmsColumns.COLUMN_POSTER_PATH, posterPath);
//                filmValues.put(FilmsColumns.COLUMN_POPULARITY, popularity);
//                filmValues.put(FilmsColumns.COLUMN_TITLE, title);
//                filmValues.put(FilmsColumns.COLUMN_VIDEO, video);
//                filmValues.put(FilmsColumns.COLUMN_VOTE_AGERAGE, voteAverage);
//                filmValues.put(FilmsColumns.COLUMN_VOTE_COUNT, vote_count);
//
//
//
//                cVVector.add(filmValues);
//
//                int inserted = 0;
//                // add to database
//                if ( cVVector.size() > 0 ) {
//                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
//                    cVVector.toArray(cvArray);
//
//                  inserted = getActivity().getContentResolver().bulkInsert(FilmsProvider.Films.CONTENT_URI, cvArray);
//                }

                //Log.d(LOG_TAG, "FetchWeatherTask Complete. " + inserted + " Inserted");



            }

            for (Film s : resulMovies) {
                Log.i(LOG_TAG, "FMovies entry: " + s.getTitle() + " -" + s.getPopularity() + " -" + s.backdrop_path);
            }
            return resulMovies;

        }
        @Override
        protected Film[] doInBackground(String... params) {
            // If there's no films, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // This are the parameters passed to look n the web
            Log.i(LOG_TAG, "Params 0 " + params[0]);
            Log.i(LOG_TAG, "Params 1 " + params[1]);



            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/discover/movie?";
                final String QUERY_API_KEY = "api_key";


                final String FORMAT_SORT_BY = "sort_by";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                       .appendQueryParameter(QUERY_API_KEY,API_KEY)
                       .appendQueryParameter(FORMAT_SORT_BY, params[0]+params[1])
                       .build();

                URL url = new URL(builtUri.toString());

                Log.i(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
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
                    return null;
                }
                movieJsonStr = buffer.toString();

                Log.i(LOG_TAG, "Forecast string: " + movieJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
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
                return getFilmDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(Film[] result) {
            if (result != null) {
               mMoviesAdapter.clear();
               for(Film movi : result) {
               mMoviesAdapter.add(movi);
                }
            }
        }
    }

}
