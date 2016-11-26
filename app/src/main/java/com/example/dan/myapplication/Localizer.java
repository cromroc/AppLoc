package com.example.dan.myapplication;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import java.util.List;

/**
 * Created by DAN on 05/10/2016.
 */

public class Localizer extends AppCompatActivity{
    WifiManager wifi;
    String wifis[];
    //WifiScanReceiver wifiReciever;

    protected void onCreate(Bundle savedInstanceState){}

    public RPoint newPoint (String nome) {
        RPoint rp = new RPoint ();
        AccessPoint ap = new AccessPoint ();
        wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        //wifiReciever = new WifiScanReceiver();
        wifi.startScan();
        List<ScanResult> wifiScanList = wifi.getScanResults();
        if (wifiScanList.size() < 3) {
            rp.nome = "NOTFOUND";
            return rp;
        }
        for(int i = 0; i < wifiScanList.size(); i++){
            ap.SSID = wifiScanList.get(i).SSID;
            ap.MAC = wifiScanList.get(i).BSSID;
            rp.AP.add(ap);
        }
        rp.nome = nome;
        return rp;
    }


}

