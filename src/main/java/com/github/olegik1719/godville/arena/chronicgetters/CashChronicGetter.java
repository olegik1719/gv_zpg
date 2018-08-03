package com.github.olegik1719.godville.arena.chronicgetters;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CashChronicGetter implements ChronicGetter {

    private WebChronicGetter webGetter;
    private FileChronicGetter fileGetter;
    private String disk;
    private String web;

    public CashChronicGetter(){
        webGetter = new WebChronicGetter();
        fileGetter = new FileChronicGetter();
    }

    public void setWeb(String pathOnWeb){
        this.webGetter = new WebChronicGetter(pathOnWeb);
        this.web = pathOnWeb;
        //return this;
    }

    public void setDisk(String pathOnDisk){
        this.fileGetter = new FileChronicGetter(pathOnDisk);
        this.disk = pathOnDisk;
        //return this;
    }


    @Override
    public String getHtml(String chronicleID) {
        String result;
        if ((result = fileGetter.getHtml(chronicleID)).equals(""))
            if ((result = webGetter.getHtml(chronicleID)).equals("")) {
                throw new RuntimeException("https://godville.net/duels/log/"+chronicleID);
            }else
                try (PrintWriter out = new PrintWriter(disk+chronicleID+".html")) {
                    out.println(result);
                    //Thread.sleep(10000);
                } catch (Exception e) {
                    System.out.println("getHTML");
                    e.printStackTrace();
                }
        return result;
    }
}
