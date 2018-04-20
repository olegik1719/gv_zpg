package com.github.olegik1719.godville.arena.chronicgetters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebChronicGetter implements ChronicGetter {

    private final String URL_PREFIX;

    public WebChronicGetter(String URL){
        this.URL_PREFIX= URL;
    }

    WebChronicGetter(){
        this.URL_PREFIX= "https://gv.erinome.net/duels/log/";
    }

    @Override
    public String getHtml(String chronicleID){
        if (chronicleID.length() == 0) return "";
        StringBuilder chronicleHTML = new StringBuilder();
        URL chronicleURL;
        try {
            chronicleURL = new URL(URL_PREFIX+chronicleID);
            HttpURLConnection http = (HttpURLConnection)chronicleURL.openConnection();
            int statusCode = http.getResponseCode();
            if (statusCode == 200)
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(chronicleURL.openStream()))){
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        chronicleHTML.append(inputLine);
                    }
                }
            else return "";
        } catch (IOException e) {
            return "";
        }
        return chronicleHTML.toString();
    }
}

//
//    URL url = new URL(address);
//
//    InputStream in = url.openStream();
//    int i;
//    while((i=in.read())!=-1){
//            out.write(i);
//            }
//
//            in.close();