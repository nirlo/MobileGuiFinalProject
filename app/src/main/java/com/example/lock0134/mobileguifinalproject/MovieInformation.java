package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_ACTORS;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_DATA;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_DESC;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_GENRE;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_ID;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_LENGTH;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_RATE;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.KEY_URL;
import static com.example.lock0134.mobileguifinalproject.MovieDatabaseHelper.MOVIE_TABLE;

public class MovieInformation extends Activity {
    private final String ACTIVITY_NAME = "MovieInformation: ";
    Button addMovieB;
    Button downloadMovieB;
    Button infoB;
    ListView movieList;
    ArrayList<String> urlArray = new ArrayList<>();
    ArrayList<String> actorNames = new ArrayList<>();
    ArrayList<String> lengthArray = new ArrayList<>();
    ArrayList<String> descArray = new ArrayList<>();
    ArrayList<String> ratingArray = new ArrayList<>();
    ArrayList<String> genreArray = new ArrayList<>();
    ArrayList<String> titleNames = new ArrayList<>();
    ArrayList<String> XMLArray = new ArrayList<>();
    MovieAdapter adap;
    int arrayCount = 0;
    final MovieDatabaseHelper dbHelp = new MovieDatabaseHelper(MovieInformation.this);
    Cursor c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_information);
        addMovieB = (Button) findViewById(R.id.addMovieButton);
        downloadMovieB = (Button) findViewById(R.id.downloadButton);
        movieList = (ListView) findViewById(R.id.movieListView);
        infoB = (Button) findViewById(R.id.infoB);
        addMovieB.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             Intent intent = new Intent(MovieInformation.this, MovieDetails.class);
                                             startActivityForResult(intent, 50);
                                         }
                                     }
        );

        downloadMovieB.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  MovieQuery q = new MovieQuery();
                                                  q.execute();
                                                  Log.i(ACTIVITY_NAME, "Download and read successfully.");
                                                  CharSequence text = "XML Downloaded Successfully";
                                                  int duration = Toast.LENGTH_LONG;
                                                  Toast toast = Toast.makeText(getBaseContext(), text, duration);
                                                  toast.show();
                                                  XMLArray = q.getList();

                                              }
                                          }
        );


        infoB.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view) {
                CharSequence text = "Created By: Alec Kennedy ";
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getBaseContext(), text, duration);
                toast.show();
            }
        });

        adap = new MovieAdapter(this);
        movieList.setAdapter(adap);
        final SQLiteDatabase db = dbHelp.getWritableDatabase();


        String[] columns = new String[]{KEY_ID, KEY_DATA, KEY_ACTORS, KEY_DESC, KEY_GENRE, KEY_LENGTH, KEY_RATE, KEY_URL};
        c = db.query(MOVIE_TABLE, columns, null, null, null, null,null);
        int iRow = c.getColumnIndex(KEY_DATA);
        int iActor = c.getColumnIndex(KEY_ACTORS);
        int iDesc = c.getColumnIndex(KEY_DESC);
        int iGenre = c.getColumnIndex(KEY_GENRE);
        int iLength = c.getColumnIndex(KEY_LENGTH);
        int iRate = c.getColumnIndex(KEY_RATE);
        int iURL = c.getColumnIndex(KEY_URL);
        int iMessage = c.getColumnIndex(KEY_ID);

        String result = "";

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
        {
            titleNames.add(c.getString(iRow));
            actorNames.add(c.getString(iActor));
            descArray.add(c.getString(iDesc));
            genreArray.add(c.getString(iGenre));
            lengthArray.add(c.getString(iLength));
            ratingArray.add(c.getString(iRate));
            urlArray.add(c.getString(iURL));

        }

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer pos = i;

                Intent intent = new Intent(MovieInformation.this, MovieDetails.class);
                intent.putExtra("TITLE", titleNames.get(i));
                intent.putExtra("ACTOR", actorNames.get(i));
                intent.putExtra("LENGTH", lengthArray.get(i));
                intent.putExtra("DESC", descArray.get(i));
                intent.putExtra("RATING", ratingArray.get(i));
                intent.putExtra("GENRE", genreArray.get(i));
                intent.putExtra("URL", urlArray.get(i));
                intent.putExtra("POSITION", pos.toString());
                startActivityForResult(intent, 60);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 50) {
            final SQLiteDatabase db = dbHelp.getWritableDatabase();
            String t = data.getExtras().get("Title").toString();
            String a = data.getExtras().get("Actor").toString();
            String l = data.getExtras().get("Length").toString();
            String d = data.getExtras().get("Desc").toString();
            String r = data.getExtras().get("Rating").toString();
            String g = data.getExtras().get("Genre").toString();
            String u = data.getExtras().get("url").toString();
            titleNames.add(t);
            actorNames.add(a);
            lengthArray.add(l);
            descArray.add(d);
            ratingArray.add(r);
            genreArray.add(g);
            urlArray.add(u);
            ContentValues cValues = new ContentValues();
            cValues.put(dbHelp.KEY_DATA, t);
            cValues.put(dbHelp.KEY_ACTORS, a);
            cValues.put(dbHelp.KEY_LENGTH, l);
            cValues.put(dbHelp.KEY_DESC, d);
            cValues.put(dbHelp.KEY_RATE, r);
            cValues.put(dbHelp.KEY_GENRE, g);
            cValues.put(dbHelp.KEY_URL, u);
            db.insert(dbHelp.MOVIE_TABLE, null, cValues);
            adap.notifyDataSetChanged();
        }

        else if(resultCode == 60)
        {
            int pos = Integer.parseInt(data.getStringExtra("pos"));
            final SQLiteDatabase db = dbHelp.getWritableDatabase();
            String t = data.getExtras().get("Title").toString();
            String a = data.getExtras().get("Actor").toString();
            String l = data.getExtras().get("Length").toString();
            String d = data.getExtras().get("Desc").toString();
            String r = data.getExtras().get("Rating").toString();
            String g = data.getExtras().get("Genre").toString();
            String u = data.getExtras().get("url").toString();
            titleNames.add(t);
            actorNames.add(a);
            lengthArray.add(l);
            descArray.add(d);
            ratingArray.add(r);
            genreArray.add(g);
            urlArray.add(u);
            ContentValues cValues = new ContentValues();
            cValues.put(dbHelp.KEY_DATA, t);
            cValues.put(dbHelp.KEY_ACTORS, a);
            cValues.put(dbHelp.KEY_LENGTH, l);
            cValues.put(dbHelp.KEY_DESC, d);
            cValues.put(dbHelp.KEY_RATE, r);
            cValues.put(dbHelp.KEY_GENRE, g);
            cValues.put(dbHelp.KEY_URL, u);


        }
    }

    @Override
    public void onDestroy()
    {
        dbHelp.getWritableDatabase().close();
        super.onDestroy();
    }

    public void addDownloadedMovies(ArrayList<String> l)
    {
        for(int i=0;i<l.size();i++)
        {
            Log.i("lol", l.get(i));
            if(l.get(i).isEmpty() || l.get(i).equals(null))
            {
                l.remove(i);
            }
        }
    }



    class MovieQuery extends AsyncTask<String, Integer, String> {
        private final String ACTIVITY_NAME = "MovieQuery";
        String actors;
        String title;
        String description;
        String length;
        String rating;
        String genre;
        String url;
        ArrayList<String> l = new ArrayList<String>();

        @Override
        protected String doInBackground(String... strings) {
            try {
                parse(downloadUrl("http://torunski.ca/CST2335/MovieInfo.xml"));
                Log.i(ACTIVITY_NAME, "URL downloaded");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }


        public void parse(InputStream in) throws XmlPullParserException, IOException {

            try {

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                int eventType = parser.getEventType();
                while (eventType != parser.END_DOCUMENT) {
                    if (eventType == parser.TEXT) {
                            l.add(parser.getText());

                    }
                    eventType = parser.next();
                }
                System.out.println("End document");

            } finally {
                in.close();
            }

        }

        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }

        public ArrayList<String> getList()
        {
            return this.l;
        }
    }


    class MovieAdapter extends ArrayAdapter<String> {

        public MovieAdapter(Context ctx) {
            super(ctx, 0);
        }

        public String getItem(int p)
        {
            return titleNames.get(p);
        }

        public int getCount()
        {
            return titleNames.size();
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = MovieInformation.this.getLayoutInflater();
            View v = inflater.inflate(R.layout.customr_row, null);
            TextView title = (TextView) v.findViewById(R.id.movieName);
            title.setText(getItem(position));

            return v;
        }

    }
}

