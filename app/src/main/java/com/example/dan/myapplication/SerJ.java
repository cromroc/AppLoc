package com.example.dan.myapplication;

import com.google.gson.Gson;

public class SerJ {
 
 public static String serToJson(Object obj) {
        Gson gson = new Gson();
        String j = gson.toJson(obj);
        return j;
    
 }
 
  public static <T> T deserFromJson(String jsonString,Class<T> T) {
         Gson gson = new Gson();
         T sj =  gson.fromJson(jsonString, T);
         return sj;
         
     }
  

}