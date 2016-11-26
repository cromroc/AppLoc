package com.example.dan.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class FindActivity extends AppCompatActivity {

    ProgressDialog progress = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        progress=new ProgressDialog(FindActivity.this);
        progress.setMax(100);
        progress.setMessage("Localizzazione in corso...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);

        Button btnLocal=(Button)findViewById(R.id.btnLocalizza);
        btnLocal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                FindActivity.BackgroundTask bt = new FindActivity.BackgroundTask();
                bt.execute();

            }
        });
    }

    private class BackgroundTask extends AsyncTask<Void, Integer, String> {

        Client client = new Client();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(Void... args) {
            publishProgress(new Integer[]{10});
            RPoint rp = newPoint();
            Collections.sort(rp.AP);
            String jsonrp = SerJ.serToJson(rp);
            boolean b = client.init();
            if (b){
                try{
                    client.pout.println("Trova");
                    client.pout.println(jsonrp);
                    String esito = client.bin.readLine();
                    client.chiudi();
                    return esito;
                }catch (IOException ex){
                    client.chiudi();
                    return "Errore di I-O";
                }
            }
            client.chiudi();
            return "Server OFF";

        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            super.onProgressUpdate(values);
            progress.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            progress.dismiss();
            Toast.makeText(FindActivity.this, result,   Toast.LENGTH_SHORT).show();
        }

        public RPoint newPoint () {
            WifiManager wifi;
            RPoint rp = new RPoint ();
            wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
            wifi.startScan();

            List<ScanResult> wifiScanList = wifi.getScanResults();

            for(int i = 0; i < wifiScanList.size(); i++){
                AccessPoint ap = new AccessPoint();
                rp.AP.add(i,ap);
                rp.AP.get(i).SSID = wifiScanList.get(i).SSID;
                rp.AP.get(i).MAC = wifiScanList.get(i).BSSID;
                rp.AP.get(i).level = wifiScanList.get(i).level;
            }
            rp.AP.trimToSize();
            return rp;
        }

    }
}
