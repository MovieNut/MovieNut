package com.example.movienut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.Genre;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by WeiLin on 14/7/15.
 */
public class RecommendMovieByGenre extends Activity {
    private TmdbApi accountApi;
    private List<Genre> genreList;
    String displayMovies;
    public String searchKeyWord;
    public String image = "" + "\n";
    String description = " " + "\n";
    public  String[] listOfDescription;
    public String[] moviesInfo;
    public String[] listOfImage;
    public String[] releaseDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_of_similar_name);

        searchKeyWord = getSearchKeyword();

        permitsNetwork();

        accountApi = new TmdbApi("3f2950a48b75db414b1dbb148cfcad89");


        if(searchKeyWord.equals("UserAccess")) {
            userAccessRecommendGenre();

        } else {
            adminAccessRecommendByGenre();

        }
    }

    private void adminAccessRecommendByGenre() {
        Intent intent = getIntent();
        String genre = intent.getStringExtra("genre").toLowerCase();
        genreList = accountApi.getGenre().getGenreList("");

        for(int i = 0; i < genreList.size(); i++){
            if(genre.equals(genreList.get(i).getName().toLowerCase())) {
                displayMovies = genreList.get(i).getName() + " movies" + "\n";
                displayMoviesBasedOnGenre(genreList.get(i).getId());
                break;
            }
        }
    }

    private void userAccessRecommendGenre() {
        genreList = accountApi.getGenre().getGenreList("");

        //name: action, adventure, animation, comedy, crime, documentry, drama, family, fantasy, foreign, history, horror
        //,music, mystery, romance, science fiction, tv movie, thriller, war, western.
        String[] genreType = getGenreList();

        ListView genreTypeList = (ListView) findViewById(R.id.listView2);
        moviesAdapter adapter = new moviesAdapter(this, genreType);
        genreTypeList.setAdapter(adapter);

        selectOneMovie(genreTypeList);
    }

    private String[] getGenreList() {
        String[] genreType = new String[genreList.size()];
        for (int i = 0; i < genreList.size(); i++) {
            genreType[i] = genreList.get(i).getName();
        }
        return genreType;
    }

    private void selectOneMovie(ListView genreTypeList) {
        genreTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
              //  Toast.makeText(getApplicationContext(), "LOADING", Toast.LENGTH_LONG).show();
                int idOfGenre = genreList.get(position).getId();
                displayMovies = genreList.get(position).getName() + " movies" + "\n";

                displayMoviesBasedOnGenre(idOfGenre);
            }

        });
    }

    private void displayMoviesBasedOnGenre(int idOfGenre) {
        try {
            List<MovieDb> result = accountApi.getGenre().getGenreMovies(idOfGenre, "", null, true).getResults();

            verifyResultNull(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            returnHomePage();
        }
    }

    private void verifyResultNull(List<MovieDb> result) throws IOException {
        if (result == null || result.size() <= 0) {
            throw new NullPointerException();
        } else {

            getListOfMovies(result);
            Intent displyResults = new Intent(RecommendMovieByGenre.this, DisplayResults.class);
            displyResults.putExtra("movieInfo", moviesInfo);
            displyResults.putExtra("description", listOfDescription);
            displyResults.putExtra("image", listOfImage);
            displyResults.putExtra("releaseDate", releaseDates);
            startActivity(displyResults);
        }
    }

    private void returnHomePage() {
        Intent returnHome = new Intent(this, Home.class);
        startActivity(returnHome);
        this.finish();
        Toast.makeText(getApplicationContext(), "Error encountered!", Toast.LENGTH_LONG).show();
    }

    private void getListOfMovies(List<MovieDb> result) throws IOException {
        releaseDates = new String[result.size() + 1];
        releaseDates[0] = "";
        Map<String, Boolean> map = Storage.loadMap(getApplicationContext());

        addMovieInfo(result, map);
        moviesInfo = displayMovies.split("\\r?\\n");
        listOfDescription = description.split("\\r?\\n");
        listOfImage = image.split("\\r?\\n");
    }

    private void addMovieInfo(List<MovieDb> result, Map<String, Boolean> map) {
        String releaseDate;
        int count = 0;

        for (int i = 0; i < result.size(); i++) {
            if (map.get(String.valueOf(result.get(i).getId())) == null) {
                releaseDate = result.get(i).getReleaseDate();
                releaseDate = addReleaseDate(releaseDate, count);

                displayMovies = displayMovies + result.get(i).getOriginalTitle() + "("
                        + releaseDate + ")" + "\n";

                addDescription(result, i);
                addImageUrl(result, i);
                count++;
            }
        }
    }

    private void addImageUrl(List<MovieDb> result, int i) {
        if (Utils.createImageUrl(accountApi, result.get(i).getPosterPath(), "original") != null) {
            image = image + Utils.createImageUrl(accountApi, result.get(i).getPosterPath(), "original").toString() + "\n";

        } else {
            image = image + " " + "\n";
        }
    }

    private void addDescription(List<MovieDb> result, int i) {
        if (result.get(i).getOverview() == null) {
            description = description + "NO DESCRIPTION YET" + "\n";
        } else {
            description = description + result.get(i).getOverview() + "\n";
        }
    }

    private String addReleaseDate(String releaseDate, int i) {
        if (releaseDate == null || releaseDate.length() <= 4) {
            releaseDate = "unknown";
            releaseDates[i + 1] = "";
        } else {
            releaseDate = releaseDate.substring(0, 4);
            releaseDates[i + 1] = releaseDate;
        }
        return releaseDate;
    }

    class moviesAdapter extends ArrayAdapter<String> {
        Context context;
        String[] list;
        private int[] colors = new int[] { Color.parseColor("#fffff1d6"), Color.parseColor("#D2E4FC") };

        moviesAdapter(Context c, String[] list) {
            super(c, R.layout.selection_row, R.id.textView, list);
            this.context = c;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.genre_list, parent, false);
            TextView name = (TextView) row.findViewById(R.id.genreType);

            int colorPos = position % colors.length;
            row.setBackgroundColor(colors[colorPos]);

            name.setText(list[position]);

            return row;
        }

    }

    private String getSearchKeyword() {
        Intent intent = getIntent();
        return intent.getStringExtra("searchKeyWord");
    }

    private void permitsNetwork() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_feature, menu);
        return true;
    }
}

