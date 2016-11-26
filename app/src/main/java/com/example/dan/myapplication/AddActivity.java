package com.example.dan.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AddActivity extends AppCompatActivity {


    public EditText txtNome;
    ProgressDialog progress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        progress=new ProgressDialog(AddActivity.this);
        progress.setMax(100);
        progress.setMessage("Localizzazione in corso...");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setCancelable(false);

        txtNome = (EditText)findViewById(R.id.txtNome);
        Button btnSalva=(Button)findViewById(R.id.btnSalva);
        btnSalva.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                BackgroundTask bt = new BackgroundTask();
                bt.execute(txtNome.getText().toString());
                //RPoint rp = newPoint("Prova");
                //if (!rp.nome.equals("NOTFOUND")) {
                    //boolean b = client.addRP(rp);
                    //if (b)
                      //  txtEsito.setText("RP Salvato con successo!");
                    //else
                      //  txtEsito.setText("Salvataggio non riuscito.");
                //}
            }
        });

        Button btnProva=(Button)findViewById(R.id.btnProva);
        btnProva.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                Prova prova = new Prova ();
                prova.start();
            }
        });
    }



    private class BackgroundTask extends AsyncTask<String, Integer, String> {

        Client client = new Client();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setProgress(0);
            progress.show();
        }

        @Override
        protected String doInBackground(String... nome)
        {
            //try
            //{
            publishProgress(new Integer[]{10});
            //Thread.sleep(5000);
            RPoint rp = newPoint(nome[0]);
            if (rp.nome.equals("NOTFOUND"))
                return "Errore: AP non sufficienti.";
            Collections.sort(rp.AP);
            String jsonrp = SerJ.serToJson(rp);
            boolean b = client.init();
            if (b){
                try{
                    client.pout.println("Salva");
                    client.pout.println(jsonrp);
                    String esito = client.bin.readLine();
                    client.chiudi();
                    if (esito.equals("OK"))
                        return "RefPoint Aggiunto!";
                    else
                        return "Errore con il db";
                }catch (IOException ex){
                    client.chiudi();
                    return "Errore di I-O";
                }
            }
            client.chiudi();
            return "Server OFF";

            //}
            //catch (InterruptedException e)
            //{}

            //return "Lavoro Terminato!";
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
            //txtEsito.setText(result);
            Toast.makeText(AddActivity.this, result,   Toast.LENGTH_SHORT).show();
        }

        public RPoint newPoint (String nome) {
            WifiManager wifi;
            String wifis[];
            RPoint rp = new RPoint ();
            //AccessPoint ap;
            wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
            //wifiReciever = new WifiScanReceiver();
            wifi.startScan();

            List<ScanResult> wifiScanList = wifi.getScanResults();
            if (wifiScanList.size() < 3) {
                rp.nome = "NOTFOUND";
                return rp;
            }
            for (int i = 0; i < wifiScanList.size(); i++) {
                System.out.println(i + ": " + wifiScanList.get(i).toString());
            }

            for(int i = 0; i < wifiScanList.size(); i++){
                AccessPoint ap = new AccessPoint();
                rp.AP.add(i,ap);
                rp.AP.get(i).SSID = wifiScanList.get(i).SSID;
                rp.AP.get(i).MAC = wifiScanList.get(i).BSSID;
                rp.AP.get(i).level = wifiScanList.get(i).level;

            }
            rp.AP.trimToSize();
            rp.nome = nome;

            return rp;
        }

    }
}

class Prova extends Thread {
    Client client = new Client();
    public void run () {

        client.prova();
    }
}