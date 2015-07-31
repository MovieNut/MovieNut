package com.example.movienut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import bolts.AppLinks;

/**
 * Created by WeiLin on 4/7/15.
 */
public class DisplayResults extends Activity {
    public String[] moviesInfo;
    public String[] description;
    public String[] image;
    public String[] releaseDates;
    ArrayList<Movies> movies = new ArrayList<>();
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_display_results);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        Toast.makeText(getApplicationContext(), "LOADING", Toast.LENGTH_SHORT).show();
        getMoviesInfo();

        ListView list = (ListView) findViewById(R.id.listView);
        moviesAdapter adapter = new moviesAdapter(this, moviesInfo, description, image);
        list.setAdapter(adapter);
    }

    private void getMoviesInfo() {
        Intent intent = getIntent();
        moviesInfo = intent.getStringArrayExtra("movieInfo");
        assert(moviesInfo != null) : "moviesInfo null";
        description = intent.getStringArrayExtra("description");
        assert(description != null) : "description null";
        image = intent.getStringArrayExtra("image");
        assert(image != null) : "image null";
        releaseDates = intent.getStringArrayExtra("releaseDate");

        try {
            storeInMovieClass();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        sortByDate();

        storeBackIntoString();
    }

    private void sortByDate() {
        Collections.sort(movies, new Comparator<Movies>() {
            public int compare(Movies o1, Movies o2) {
                if (o1.getDate() == "" || o2.getDate() == "" || o1.getDate() == null || o2.getDate() == null) {
                    return 0;
                }
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    private void storeBackIntoString() {
        for (int i = 0; i < movies.size(); i++) {
            moviesInfo[i + 1] = movies.get(i).getMovieTitle();
            description[i + 1] = movies.get(i).getDescription();
            image[i + 1] = movies.get(i).getImageURL();
        }
    }

    private void storeInMovieClass() throws ParseException {

        for (int i = 1; i < moviesInfo.length; i++) {
            movies.add(new Movies());
            movies.get(i - 1).setMovieTitle(moviesInfo[i]);
            movies.get(i - 1).setDecription(description[i]);
            movies.get(i - 1).setImageURL(image[i]);
            movies.get(i - 1).setDate(releaseDates[i]);
        }
    }

    class moviesAdapter extends ArrayAdapter<String> implements com.example.movienut.moviesAdapter  {
        Context context;
        String[] moviesInfo;
        String[] description;
        String[] image;

        moviesAdapter(Context c, String[] moviesInfo, String[] description, String[] image) {
            super(c, R.layout.single_row, R.id.textView3, moviesInfo);
            this.context = c;
            this.moviesInfo = moviesInfo;
            this.description = description;
            this.image = image;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.single_row, parent, false);
            ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
            TextView movieTitles = (TextView) row.findViewById(R.id.textView3);
            TextView myDescription = (TextView) row.findViewById(R.id.textView4);

            movieTitles.setText(moviesInfo[position]);
            myDescription.setText(description[position]);

            setImage(position, myImage);
            return row;
        }

        private void setImage(int position, ImageView myImage) {
            if (image.length > position && (!image[position].equals("") || !(image[position] == null))) {

                String url = null;
                try {
                    url = new URL(image[position]).toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Picasso.with(context).load(url).resize(50, 50).centerCrop().into(myImage);
            }
        }
    }

    public void shareContent(View view) {

//        LinkedList<String> newMovies = new LinkedList<String>();
//
//        for (int i = 0; i < movies.size(); i++) {
//            newMovies.add(movies.get(i).getMovieTitle());
//            Log.d("Movies List", "why: " + movies.get(i).getMovieTitle());
//        }

        ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                .putString("og:type", "video.movie")
                .putString("og:title", movies.get(1).getMovieTitle())
                .putString("og:image",movies.get(1).getImageURL())
                .putString("og:description",movies.get(1).getDescription())
                .build();
        ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                .setActionType("video.wants_to_watch")
                .putObject("movie", object)
                .build();
        // Create the content
        ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                .setPreviewPropertyName("movie")
                .setAction(action)
                .build();

        shareDialog.show(content);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_results, menu);
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









