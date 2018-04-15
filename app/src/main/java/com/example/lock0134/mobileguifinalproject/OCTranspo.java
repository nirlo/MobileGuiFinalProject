package com.example.lock0134.mobileguifinalproject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class OCTranspo extends Activity {
    protected EditText stop;
    protected Button search;
    protected BusStopsDatabaseHelpder dbHelper;
    protected ListView previous;
    protected HashMap<String, ArrayList<String>> previousStops;
    protected SQLiteDatabase db;
    protected Cursor c;
    protected StopsAdapter stopsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_octranspo);


        stop = findViewById(R.id.search_stops);
        search = findViewById(R.id.Sbutton);
        dbHelper = new BusStopsDatabaseHelpder(this);
        previous = findViewById(R.id.stops);

        db = dbHelper.getWritableDatabase();

        c = db.query(false, dbHelper.TABLE_NAME, new String[] {dbHelper.KEY_ID, dbHelper.KEY_STOP}, null, null, null, null, null, null);
        c.moveToFirst();

        while(!c.isAfterLast()) {
            ArrayList<String> info = new ArrayList<>(100);
            info.add(c.getString(c.getColumnIndex(dbHelper.KEY_STOP)));
            info.add(c.getString(c.getColumnIndex(dbHelper.BUS_NUMBER)));
            previousStops.put(dbHelper.KEY_STOP, info);

            c.moveToNext();
        }

        stopsAdapter = new StopsAdapter(this, previousStops);
        previous.setAdapter(stopsAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = stop.getText().toString();
                ArrayList<String> details = new ArrayList<>(10);
                String[] detail = text.split("[, ]");
                for(String s: detail)
                    details.add(s);

                Bundle bundle = new Bundle();
                bundle.putStringArrayList("info", details);

                previousStops.put(details.get(0), details);

                Intent intent = new Intent(OCTranspo.this, TranspoDetails.class);
                startActivityForResult(intent, 5, bundle);
            }
        });

        previous.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("info", previousStops.get(i));
                Intent intent = new Intent(OCTranspo.this, TranspoDetails.class);
                startActivityForResult(intent, 5, bundle);
            }
        });
    }



    private class StopsAdapter extends BaseAdapter {
        HashMap<String, ArrayList<String>> list;

        public StopsAdapter(Context ctx, HashMap<String, ArrayList<String>> list) {
            super();
            this.list = list;
        }


        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        public ArrayList<String> getItem(String s) {
            return list.get(s);
        }

        public View getView(String key, View convertView, ViewGroup parent) {
            LayoutInflater inflater = OCTranspo.this.getLayoutInflater();
            View result = null;
            result = inflater.inflate(R.layout.previousstops, null);


            TextView stop = result.findViewById(R.id.previousStop);
            TextView bus = result.findViewById(R.id.previousBus);

            stop.setText(list.get(key).get(0));
            bus.setText(list.get(key).get(1));
            return result;
        }

        public long getId(int position) {
            return position;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return false;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 50) {
            String key = data.getStringExtra("info");
            previousStops.remove(key);
            stopsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "It is done", Toast.LENGTH_LONG);
        }
    }
}
