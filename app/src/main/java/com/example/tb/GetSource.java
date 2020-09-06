package com.example.tb;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.alibaba.fastjson.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GetSource {
    ArrayList<String> epidemic;
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int c;
        while((c = rd.read()) != -1){
            sb.append((char) c);
        }
        return sb.toString();
    }

    public ArrayList<String> getEpidemic(){
        return epidemic;
    }


    public String getRaw(){
        epidemic = new ArrayList<>();
        try{
            URL cs = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
            HttpURLConnection tc = (HttpURLConnection) cs.openConnection();
            InputStreamReader in = new InputStreamReader(tc.getInputStream());
            String json = readAll(in);
//            System.out.println(json.length());
//            System.out.println(json.substring(0,100));
            JSONObject obj = JSONObject.parseObject(json);
            System.out.println(obj.size());
            for(String k: obj.keySet()){
                JSONArray timestep = (JSONArray)((JSONObject)obj.get(k)).get("data");
                String item = k + timestep.get(timestep.size()-1);
                epidemic.add(item);
            }
            return json;
        } catch(IOException e){
            System.out.println(e);
        }
        return "";
    }

    public ArrayList<String> parse(String json){
        JSONObject obj = JSONObject.parseObject(json);
        System.out.println(obj.size());
        for(String k: obj.keySet()){
            JSONArray timestep = (JSONArray)((JSONObject)obj.get(k)).get("data");
            String item = k + timestep.get(timestep.size()-1);
            epidemic.add(item);
        }
        return epidemic;
    }


    public ArrayList<String> pullEpidemic(){
        epidemic = new ArrayList<>();
        try{
            URL cs = new URL("https://covid-dashboard.aminer.cn/api/dist/epidemic.json");
            URLConnection tc = cs.openConnection();
            InputStreamReader in = new InputStreamReader(tc.getInputStream());
            String json = readAll(in);
//            System.out.println(json.length());
//            System.out.println(json.substring(0,100));
            JSONObject obj = JSONObject.parseObject(json);
            //System.out.println(obj.size());
            for(String k: obj.keySet()){
                JSONArray timestep = (JSONArray)((JSONObject)obj.get(k)).get("data");
                String item = k + timestep.get(timestep.size()-1);
                epidemic.add(item);
            }
        } catch(IOException e){
            System.out.println(e);
        }
        return epidemic;
    }
}
