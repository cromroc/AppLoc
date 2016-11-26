package com.example.dan.myapplication;

/**
 * Created by DAN on 05/10/2016.
 */

public class AccessPoint implements Comparable<AccessPoint> {
    protected String SSID;
    protected String MAC;
    protected int level;

    public int compareTo(AccessPoint app) {
        if (level < app.level)
            return 1;
        else if (level == app.level)
            return 0;
        else
            return -1;
    }


}
