package com.example.lock0134.mobileguifinalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class searchDetailsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private View view;
    private Button btn;

    private OnFragmentInteractionListener mListener;

    public searchDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn = view.findViewById(R.id.deleteRoute);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "The Route Listing is being deleted", Toast.LENGTH_LONG).show();
                Intent temp = new Intent();
                temp.putExtra("route", savedInstanceState.getStringArrayList("info").get(0));
                getActivity().setResult(Activity.RESULT_OK, temp);
                getActivity().finish();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_details_fragement, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        new SearchQuery().execute(bundle, null, null);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    class SearchQuery extends AsyncTask<Bundle, Void, String> {

        HashMap<String, ArrayList<String>> map = new HashMap<>();

        @Override
        protected String doInBackground(Bundle... b) {
            ArrayList<String> details = b[0].getStringArrayList("info");
            URL url;
            HttpURLConnection connection;
            InputStream is;
            String request;
            boolean summary = false;

            if(details.size() < 2) {
                request = "https://api.octranspo1.com/v1.2/GetRouteSummaryForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo=" + details.get(0);
                summary = true;
            }else
                request = "https://api.octranspo1.com/v1.2/GetNextTripsForStop?appID=223eb5c3&&apiKey=ab27db5b435b8c8819ffb8095328e775&stopNo="+details.get(0)+"&routeNo="+details.get(1);

            try {
                url = new URL(request);
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();

                is = connection.getInputStream();
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(is, null);

                int eventType = parser.getEventType();


                while (eventType != XmlPullParser.END_DOCUMENT){
                    if(eventType != XmlPullParser.START_TAG){
                        eventType = parser.next();
                        continue;
                    }
                    else {
                        if(summary){
                            if(parser.getName().equals("Route")){
                                ArrayList<String> list = new ArrayList<>();
                                list.add(parser.getAttributeValue(null, "RouteNo"));
                                list.add(parser.getAttributeValue(null, "Direction"));
                                list.add(parser.getAttributeValue(null, "RouteHeading"));
                                map.put(parser.getAttributeValue(null, "RouteNo"), list);
                            }
                        }else {
                            if(parser.getName().equals("Trip")) {
                                ArrayList<String> list = new ArrayList<>();
                                list.add(parser.getAttributeValue(null, "TripDestination"));
                                list.add(parser.getAttributeValue(null, "TripStartTime"));
                                list.add(parser.getAttributeValue(null, "AdjustedScheduleTime"));
                                list.add(parser.getAttributeValue(null, "Latitude"));
                                list.add(parser.getAttributeValue(null, "Longitude"));
                                list.add(parser.getAttributeValue(null, "GPSSpeed"));
                                map.put(parser.getAttributeValue(null, "TripStartTime"), list);
                            }
                        }

                    }
                    eventType = parser.next();
                }

                connection.disconnect();

            }catch(MalformedURLException e) {
                e.printStackTrace();
            }catch(IOException e1) {
                e1.printStackTrace();
            }catch(Exception e2) {
                e2.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String args) {
            LinearLayout layout = view.findViewById(R.id.detailsFrame);
            for(Map.Entry<String, ArrayList<String>> entry: map.entrySet()) {
                LinearLayout inner = new LinearLayout(view.getContext());
                inner.setPadding(0, 20, 0, 20);
                for(String s: map.get(entry)) {
                    TextView text = new TextView(view.getContext());
                    switch(s) {
                        case "RouteNo":
                            text.setText("Route Number: "+s);
                            break;
                        case "Direction":
                            text.setText("Direction: "+s);
                            break;
                        case "RouteHeading":
                            text.setText("Route Heading: "+s);
                            break;
                        case "TripDestination":
                            text.setText("Trip Destination: "+s);
                            break;
                        case "TripStartTime":
                            text.setText("Departure Time: "+s);
                            break;
                        case "AdjustedScheduleTime":
                            text.setText("Bus is late by: "+s+ " minutes");
                            break;
                        case "Latitude":
                            text.setText("Latitude: "+s);
                            break;
                        case "Longitude":
                            text.setText("Longitude: "+s);
                            break;
                        case "GPSSpeed":
                            text.setText("Current Speed: "+s);
                            break;
                    }
                    text.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    inner.addView(text);
                }
                layout.addView(inner);
            }
        }
    }
}
