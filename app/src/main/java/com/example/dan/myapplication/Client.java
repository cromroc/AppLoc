package com.example.dan.myapplication;

import android.os.AsyncTask;

import java.net.*;
import java.util.ArrayList;
import java.io.*;


public class Client {
	private static final String IP = "212.189.207.141";
	private static final int PORT = 8899;
	protected Socket socket;
	protected BufferedReader bin;
	protected PrintWriter pout;





	public boolean init() {
		
		try {
			InetAddress ipp = InetAddress.getByName(IP);
			socket = new Socket (ipp, PORT);
			//System.out.println(socket);
			
			bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public void chiudi() {
		try{
			bin.close();
			pout.close();
			socket.close();
		}catch (Exception e){}
		
	}
	

	public boolean addRP (RPoint rp){
		String jsonrp = SerJ.serToJson(rp);
		boolean b = init();
		if (b){
			try{
				pout.println("Salva");
				pout.println(jsonrp);
				String esito = bin.readLine();
				chiudi();
				if (esito.equals("OK"))
					return true;
				else
					return false;
				}catch (IOException ex){
					chiudi();
					return false;
				}
		}
		chiudi();
		return false; 
	}

	public void prova () {
		boolean b = init();
		if (b){
			try{
				pout.println("Prova");
				pout.println("Messaggio di prova");
				String esito = bin.readLine();
				chiudi();
			}catch (IOException ex){
				chiudi();
				return;
			}
		}
		chiudi();
	}



}
