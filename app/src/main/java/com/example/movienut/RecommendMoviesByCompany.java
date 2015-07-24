package com.example.movienut;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbSearch;
import info.movito.themoviedbapi.Utils;
import info.movito.themoviedbapi.model.Collection;
import info.movito.themoviedbapi.model.Company;
import info.movito.themoviedbapi.model.MovieDb;


public class RecommendMoviesByCompany extends Activity {
    String displayMovies = "";
    String description = " " + "\n";
    String image = " " + "\n";
    public String[] listOfImage;
    public String[] listOfDescription;
    public String[] moviesInfo;
    public String[] companyName;
    public String[] releaseDates;
    int idOfMovies = -1;
    public String searchKeyWord;
    TmdbApi accountApi;
    public List<Company> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_of_similar_name);

        searchKeyWord = getSearchKeyword();

        permitsNetwork();

        accountApi = new TmdbApi("3f2950a48b75db414b1dbb148cfcad89");

        TmdbSearch searchResult = accountApi.getSearch();
        list = searchResult.searchCompany(searchKeyWord, 0).getResults();

        try {
            verifyIsListNull();
        } catch (NullPointerException e) {
            returnHomePage();
        }
    }

    private void verifyIsListNull() {
        if (list == null || list.size() <= 0) {
            throw new NullPointerException();
        } else {
            companyName = new String[list.size()];
            for (int i = 0; i < list.size(); i++) {
                companyName[i] = list.get(i).getName();
            }

            runMoviesInThatCompany();
        }
    }

    private void runMoviesInThatCompany() {
        ListView peopleNameList = (ListView) findViewById(R.id.listView2);
        moviesAdapter adapter = new moviesAdapter(this, companyName);
        peopleNameList.setAdapter(adapter);

        selectOneCompany(peopleNameList);
    }

    private void selectOneCompany(ListView peopleNameList) {
        peopleNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                getId(list, position);

                try {
                    List<Collection> result = accountApi.getCompany().getCompanyMovies(idOfMovies, "", 0).getResults();
                    verifyResultNull(result);
                } catch (NullPointerException e) {
                    returnHomePage();
                }

            }

        });
    }

    private void verifyResultNull(List<Collection> result) throws NullPointerException {
        if (result == null || result.size() <= 0) {
            throw new NullPointerException();
        } else {
            Toast.makeText(getApplicationContext(), "LOADING", Toast.LENGTH_SHORT).show();
            getListOfMovies(accountApi, result);
            Intent displyResults = new Intent(RecommendMoviesByCompany.this, DisplayResults.class);
            displyResults.putExtra("movieInfo", moviesInfo);
            displyResults.putExtra("description", listOfDescription);
            displyResults.putExtra("image", listOfImage);
            displyResults.putExtra("releaseDate", releaseDates);
            startActivity(displyResults);
        }
    }


    class moviesAdapter extends ArrayAdapter<String> {
        Context context;
        String[] list;
        private int[] colors = new int[] { Color.parseColor("#fffff1d6"), Color.parseColor("#D2E4FC") };

        moviesAdapter(Context c, String[] list) {
            super(c, R.layout.selection_row, R.id.textView,list);
            this.context = c;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.selection_row, parent, false);
            TextView name = (TextView) row.findViewById(R.id.textView);
            TextView space = (TextView) row.findViewById(R.id.textView2);

            int colorPos = position % colors.length;
            row.setBackgroundColor(colors[colorPos]);

            name.setText(list[position]);
            space.setVisibility(View.VISIBLE);
            space.setText("");

            return row;
        }



    }

    private void returnHomePage() {
        Intent returnHome = new Intent(this, MainActivity.class);
        startActivity(returnHome);
        this.finish();
        Toast.makeText(getApplicationContext(), "Movies or peoples could not be found!", Toast.LENGTH_LONG).show();
    }

    private void getId(List<Company> list, int position) {
        if(list.size() > 0){
            idOfMovies = list.get(position).getId();
            displayMovies = "Movies made by " + " " + list.get(position).getName() + "\n";
        }
    }

    private void getListOfMovies(TmdbApi accountApi, List<Collection> result) {

        releaseDates = new String[result.size() + 1];
        releaseDates[0] = "";
        Map<String, Boolean> map = Storage.loadMap(getApplicationContext());

        for (int i = 0; i < result.size(); i++) {
            addMovieInfo(accountApi, result, map, i);
        }
        moviesInfo = displayMovies.split("\\r?\\n");
        listOfDescription = description.split("\\r?\\n");
        listOfImage = image.split("\\r?\\n");
    }

    private void addMovieInfo(TmdbApi accountApi, List<Collection> result, Map<String, Boolean> map, int i) {
        String releaseDate;
        MovieDb movie;
        if (map.get(String.valueOf(result.get(i).getId())) == null) {
            releaseDate = result.get(i).getReleaseDate();
            releaseDate = addReleaseDate(i, releaseDate);

            displayMovies = displayMovies + result.get(i).getName() + "(" + releaseDate + ")" + "\n";
            movie = accountApi.getMovies().getMovie(result.get(i).getId(), "");

            addDescription(movie);
            addImage(accountApi, result, i);
        }
    }

    private void addImage(TmdbApi accountApi, List<Collection> result, int i) {
        if (Utils.createImageUrl(accountApi, result.get(i).getPosterPath(), "original") != null) {
            image = image + Utils.createImageUrl(accountApi, result.get(i).getPosterPath(), "original").toString() + "\n";

        } else {
            image = image + "\n";
        }
    }

    private void addDescription(MovieDb movie) {
        if (movie.getOverview() == null) {
            description = description + "NO DESCRIPTION YET" + "\n";
        } else {
            description = description + movie.getOverview() + "\n";
        }
    }

    private String addReleaseDate(int i, String releaseDate) {
        if (releaseDate == null || releaseDate.length() <= 4) {
            releaseDate = "unknown";
            releaseDates[i + 1] = "";
        } else {
            releaseDate = releaseDate.substring(0, 4);
            releaseDates[i + 1] = releaseDate;
        }
        return releaseDate;
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
        getMenuInflater().inflate(R.menu.menu_recommend_movies_by_company, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
