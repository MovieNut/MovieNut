package com.example.movienut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by WeiLin on 12/7/15.
 */
public class AddWatchedMovies extends Activity {

    public List<MovieDb> movieList;
    EditText movieOut;
    ListView moviesList;
    TmdbApi accountApi;
    String[] releaseDates, images, displayMovies, descriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_watched_movies);
        addWatchMoviesFromFb();
    }

    private void addWatchMoviesFromFb() {
        Map<String, Boolean> map = Storage.loadMap(getApplicationContext());

        ArrayList<Movies> watchedMovieList = (ArrayList<Movies>)getIntent().getSerializableExtra("watchedMovies");

        if(watchedMovieList != null) {
            for (int i = 0; i < watchedMovieList.size(); i++) {
                searchMovieByTitle(watchedMovieList, i);
                addIdToMap(map, watchedMovieList, i);
            }
        }

        Storage.saveMap(map, getApplicationContext());
    }

    private void addIdToMap(Map<String, Boolean> map, ArrayList<Movies> watchedMovieList, int i) {
        for (int j = 0; j < movieList.size(); j++) {
            if (watchedMovieList.get(i).getDate().equals(movieList.get(i).getReleaseDate())) {
              //  Movies movie = setMovieInfo(i);
                map.put(String.valueOf(movieList.get(i).getId()), true);
                break;
            }
        }
    }

    private Movies setMovieInfo(int i) {
        Movies movie = new Movies();
        movie.setMovieTitle(movieList.get(i).getOriginalTitle());
        movie.setDate(movieList.get(i).getReleaseDate());
        movie.setDecription(movieList.get(i).getOverview());
        movie.setImageURL(movieList.get(i).getPosterPath());
        return movie;
    }

    private void searchMovieByTitle(ArrayList<Movies> watchedMovieList, int i) {
        TmdbApi accountApi = new TmdbApi("3f2950a48b75db414b1dbb148cfcad89");
        TmdbSearch searchResult = accountApi.getSearch();
        movieList = searchResult.searchMovie(watchedMovieList.get(i).getMovieTitle(), null, "", false, null).getResults();
    }

    public void buttonToFindWatched(View v) throws IOException {
         movieOut = (EditText) findViewById(R.id.txtAdd);

        String searchKeyword = movieOut.getText().toString();

        try {
            if (searchKeyword == null || searchKeyword.equals("")) {
                throw new NullPointerException();
            } else {
                runSearchKeyword(searchKeyword);
            }
        } catch (NullPointerException e) {
            movieOut.setError("No Keyword entered!");
           // Toast.makeText(this, "No keyword entered!", Toast.LENGTH_LONG).show();
        }
    }

    private void runSearchKeyword(String searchKeyword) {

            permitsNetwork();

            TmdbApi accountApi = new TmdbApi("3f2950a48b75db414b1dbb148cfcad89");
            TmdbSearch searchResult = accountApi.getSearch();
            movieList = searchResult.searchMovie(searchKeyword, null, "", false, null).getResults();

            getListOfMovies();

    }

    private void getListOfMovies() {
        try {
            vertifyListisNull();
        } catch (NullPointerException e) {
            movieOut.setError("Movie entered not found!");
          //  Toast.makeText(this, "Movies entered not found!", Toast.LENGTH_LONG).show();
        }
    }

    private void vertifyListisNull() throws NullPointerException{
        if (movieList == null || movieList.size() <= 0) {
            throw new NullPointerException();
        } else {
            String[] moviesName = new String[movieList.size()];
            String[] description = new String[movieList.size()];
            getMovieInfo(moviesName, description);

            moviesList = (ListView) findViewById(R.id.listView3);
            moviesAdapter adapter = new moviesAdapter(this, moviesName, description);
            moviesList.setAdapter(adapter);


        }
    }

    private void getMovieInfo(String[] moviesName, String[] description) {
        for (int i = 0; i < movieList.size(); i++) {
            String releaseDate;
            releaseDate = getReleaseDate(i);
            moviesName[i] = movieList.get(i).getOriginalTitle() + "(" + releaseDate + ")";
            description[i] = movieList.get(i).getOverview();
        }
    }

    private String getReleaseDate(int i) {
        String releaseDate;
        if(movieList.get(i).getReleaseDate() != null){
            releaseDate = movieList.get(i).getReleaseDate().substring(0, 4);
        } else {
            releaseDate = "";
        }
        return releaseDate;
    }

    private void permitsNetwork() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    //APi : 3f2950a48b75db414b1dbb148cfcad89
    //weblink: http://api.themoviedb.org/3/movie/550?api_key=3f2950a48b75db414b1dbb148cfcad89
    //http://api.themoviedb.org/3/search/movie?api_key=3f2950a48b75db414b1dbb148cfcad89&query=avengers
    //  http://api.themoviedb.org/3/movie/8966/similar?api_key=3f2950a48b75db414b1dbb148cfcad89

    public void buttonToShowAll(View v) {
        int count = 1;
        Map<String, Boolean> map = Storage.loadMap(getApplicationContext());
        Set keys = map.keySet();

        if(keys.size() == 0){
            Toast.makeText(getApplicationContext(),  "No watched movies!", Toast.LENGTH_LONG).show();
        } else {
            initilizeStringArray(map);

            addMovieInfo(count, map);

            startAnotherActivity();
        }

    }

    private void addMovieInfo(int count, Map<String, Boolean> map) {
        for (String key : map.keySet()) {
            MovieDb movie = accountApi.getMovies().getMovie(Integer.parseInt(key), "", null);
            String releaseDate = addReleaseDate(movie.getReleaseDate(), count);
            displayMovies[count] = movie.getOriginalTitle() + "("
                    + releaseDate + ")";
            addDescription(movie.getOverview(), count);
            addImageUrl(movie.getPosterPath(), count);
            count++;
        }
    }

    private void initilizeStringArray(Map<String, Boolean> map) {
        releaseDates = new String[map.size() + 1];
        images = new String[map.size() + 1];
        images[0] = "";
        displayMovies = new String[map.size() + 1];
        displayMovies[0] = "List of watched Movie";
        descriptions = new String[map.size() + 1];
        descriptions[0] = "";
        accountApi = new TmdbApi("3f2950a48b75db414b1dbb148cfcad89");
    }

    private void startAnotherActivity() {
        Intent displyResults = new Intent(AddWatchedMovies.this, DisplayResults.class);
        displyResults.putExtra("movieInfo", displayMovies);
        displyResults.putExtra("description", descriptions);
        displyResults.putExtra("image", images);
        displyResults.putExtra("releaseDate", releaseDates);
        startActivity(displyResults);
    }

    private String addImageUrl(String image, int i) {
        if (Utils.createImageUrl(accountApi, image, "original") != null) {
            images[i] = Utils.createImageUrl(accountApi, image, "original").toString();

        } else {
            images[i] = image;
        }
        return image;
    }

    private void addDescription(String description, int i) {
        if (description == null) {
            descriptions[i] = "NO DESCRIPTION YET";
        } else {
            descriptions[i] = description;
        }
    }

    private String addReleaseDate(String releaseDate, int i) {
        if (releaseDate == null || releaseDate.length() <= 4) {
            releaseDate = "unknown";
            releaseDates[i] = "";
        } else {
            releaseDate = releaseDate.substring(0, 4);
            releaseDates[i] = releaseDate;
        }
        return releaseDate;
    }

    public void buttonToClearAll(View v) {
        Storage.saveMap(new HashMap<String, Boolean>(), getApplicationContext());
        Toast.makeText(this, "ALL CLEAR!", Toast.LENGTH_LONG).show();
    }
    class moviesAdapter extends ArrayAdapter<String> {
        Context context;
        String[] list;
        String[] description;
        private int[] colors = new int[] { Color.parseColor("#fffff1d6"), Color.parseColor("#D2E4FC") };

        moviesAdapter(Context c, String[] list, String[] description) {
            super(c, R.layout.selection_row, R.id.textView, list);
            this.context = c;
            this.list = list;
            this.description = description;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.selection_row, parent, false);
            TextView name = (TextView) row.findViewById(R.id.textView);
            TextView descriptionOut = (TextView) row.findViewById(R.id.textView2);
            Button addButton = (Button) row.findViewById(R.id.addButton);
            Button removeButton = (Button) row.findViewById(R.id.removeButton);
            descriptionOut.setVisibility(View.VISIBLE);
            addButton.setVisibility(View.VISIBLE);
            removeButton.setVisibility(View.VISIBLE);

            int colorPos = position % colors.length;
            row.setBackgroundColor(colors[colorPos]);

            descriptionOut.setText(description[position]);
            name.setText(list[position]);

            accessAddButton(addButton);
            accessRemoveButton(removeButton);


            return row;
        }

        private void accessRemoveButton(Button removeButton) {
            removeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final int position = moviesList.getPositionForView((View) v.getParent());
                    Map<String, Boolean> map = Storage.loadMap(getApplicationContext());
                    map.remove(String.valueOf(movieList.get(position).getId()));
                    Storage.saveMap(map, getApplicationContext());
                    Toast.makeText(getApplicationContext(), movieList.get(position).getOriginalTitle() + " is removed as watched movie!", Toast.LENGTH_LONG).show();

                }

            });
        }

        private void accessAddButton(Button addButton) {
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final int position = moviesList.getPositionForView((View) v.getParent());
                    Map<String, Boolean> map = Storage.loadMap(getApplicationContext());
                    map.put(String.valueOf(movieList.get(position).getId()), true);
                    Storage.saveMap(map, getApplicationContext());
                    Toast.makeText(getApplicationContext(), movieList.get(position).getOriginalTitle() + " is added as watched movie!", Toast.LENGTH_LONG).show();

                }

            });
        }

    }


}



