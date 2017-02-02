package com.example.mapapp;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * Created by Kyle on 1/30/2017.
 */

public class MapControl {

    //https://maps.googleapis.com/maps/api/directions/json?mode=walking&origin=Disneyland&destination=Universal+Studios+Hollywood4&key=AIzaSyDWfYqPCgbqI8n6c-xqp74ujbRZKVhYY5Y

    private final String BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?mode=walking&";
    private final String API_KEY = "AIzaSyDWfYqPCgbqI8n6c-xqp74ujbRZKVhYY5Y";

    private final String m_originBase = "origin=ORIGIN_HERE";
    private String m_origin = "";
    private String m_destination = "";
    private ArrayList<String> m_waypoints = new ArrayList<String>();

    private final String m_periodChar = "%2";

    private MapActivity m_activity;


  //  private DownloadRawData m_dataLoader = new DownloadRawData();

    public MapControl(MapActivity map)
    {
        m_activity = map;
    }

    private Route m_route;

    public Route CreateRoute(String from, String to)
    {
        m_origin = from;
        m_destination = to;

        try{
            String url = CreateURL();

            new DownloadRawData().execute(url);



        }
        catch (MalformedURLException e)
        {
            //Do error display here
            e.printStackTrace();

        }
        catch (java.io.IOException e)
        {
            //Do error display here
            e.printStackTrace();
        }
        //catch (org.json.JSONException e)
        //{
        //    e.printStackTrace();
        //}

        return null;

    }


    private String CreateURL() throws UnsupportedEncodingException, MalformedURLException
    {
        String urlOrigin = URLEncoder.encode(m_origin, "utf-8");
        String urlDestination = URLEncoder.encode(m_destination, "utf-8");

        //Add origin
        String returnURL = BASE_URL + "origin=" + urlOrigin;

        //Add waypoints
        //for (String w : m_waypoints)
        //{
        //
        //}

        //Add destination
        returnURL += "&destination=" + urlDestination;

        //Add key
        returnURL += "&key=" + API_KEY;

        return returnURL;
    }


    private class DownloadRawData extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... params)
        {
            String link = params[0];

            try{
                URL url = new URL(link);
                InputStream is = url.openConnection().getInputStream();
                StringBuffer buffer = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while((line = reader.readLine()) != null)
                {
                    buffer.append(line+"\n");
                }

                return buffer.toString();
            }
            catch (UnsupportedEncodingException e)
            {
                //Do error display here
                e.printStackTrace();
            }
            catch (MalformedURLException e)
            {
                //Do error display here
                e.printStackTrace();

            }
            catch (java.io.IOException e)
            {
                //Do error display here
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String res)
        {
            try {
                ParseJson(res);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

    private void ParseJson(String data) throws JSONException
    {
        //Bad data
        if(data == null) return;

        Route route = new Route();


        //Create arraylist here
        JSONObject jsonData = new JSONObject(data);

        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int j = 0; j < jsonRoutes.length(); j++) {

            JSONObject jsonRoute = jsonRoutes.getJSONObject(j);

            JSONObject overview_polyline = jsonRoute.getJSONObject("overview_polyline");

            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);

            JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
            JSONObject jsonDuration = jsonLeg.getJSONObject("duration");

            JSONObject jsonStartLoc = jsonLeg.getJSONObject("end_location");
            JSONObject jsonEndLoc = jsonLeg.getJSONObject("end_location");

            route.SetOrigin(jsonLeg.getString("start_address"));
            route.SetDestination(jsonLeg.getString("end_address"));

            route.SetStartLatLng(new LatLng(jsonStartLoc.getDouble("lat"),  jsonStartLoc.getDouble("lng")));
            route.SetEndLatLng  (new LatLng(jsonEndLoc.getDouble("lat"),    jsonEndLoc.getDouble("lng")));

            route.SetDistance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
            route.SetDuration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
            route.SetPoints(DecodePolyline(overview_polyline.getString("points")));


        }

        m_activity.ProcessRoute(route);
}


    private ArrayList<LatLng> DecodePolyline(final String encoded)
    {


        ArrayList<LatLng> decodedPolyLine = new ArrayList<LatLng>();

        //int index = 0, len = polyLine.length();
        //int lat = 0, lng = 0;

        //while (index < len) {
        //    int b, shift = 0, result = 0;
        //    do {
        //        b = polyLine.charAt(index++) - 63;
        //        result |= (b & 0x1f) << shift;
        //        shift += 5;
        //    } while (b >= 0x20);
        //    int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        //    lat += dlat;

        //    shift = 0;
        //    result = 0;
        //    do {
        //        b = polyLine.charAt(index++) - 63;
        //        result |= (b & 0x1f) << shift;
        //        shift += 5;
        //    } while (b >= 0x20);
        //    int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        //    lng += dlng;

        //    LatLng p = new LatLng((int) (((double) lat / 1E5) * 1E6),
        //            (int) (((double) lng / 1E5) * 1E6));
        //    decodedPolyLine.add(p);
        //}



        int ind = 0;
        int len = encoded.length();
        int lat = 0;
        int lng = 0;

        while (ind < len)
        {
            int b;
            int shift = 0;
            int result = 0;

            do {
                b = encoded.charAt(ind++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;

            }while(b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(ind++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;

            }while(b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decodedPolyLine.add(new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5))));

       }

        return decodedPolyLine;

    }


}
