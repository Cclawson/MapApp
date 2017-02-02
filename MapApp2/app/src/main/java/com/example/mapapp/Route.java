package com.example.mapapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Kyle on 1/30/2017.
 */

public class Route {
    private int m_distance = 0;
    private int m_duration = 0;

    private LatLng m_startLatLng;
    private LatLng m_endLatLng;

    private String m_distanceStr;
    private String m_durationStr;

    private String m_origin;
    private String m_destination;

    ArrayList<LatLng> m_points;




    //ArrayList<String> m_waypoints;



    public void SetDistance(String s, int d)
    {
        m_distanceStr = s;
        m_distance = d;
    }
    public void SetDuration(String s, int d)
    {
        m_durationStr = s;
        m_duration = d;
    }
    public int GetDistance()
    {
        return m_distance;
    }
    public int GetDuration()
    {
        return m_duration;
    }

    void SetStartLatLng(LatLng pos)
    {
        m_startLatLng = pos;
    }

    void SetEndLatLng(LatLng pos)
    {
        m_endLatLng = pos;
    }

    public String GetOrigin()
    {
        return  m_origin;
    }
    public String GetDestination()
    {
        return  m_destination;
    }

    public String GetDistanceString()
    {
        return  m_distanceStr;
    }
    public String GetDurationString()
    {
        return  m_durationStr;
    }

    public void SetOrigin(String s)
    {
        m_origin = s;
    }

    public void SetDestination(String s)
    {
        m_destination = s;
    }

    public void SetPoints(ArrayList<LatLng> points)
    {
        m_points = points;
    }

    public ArrayList<LatLng> GetPoints() {
        return m_points;
    }
}
