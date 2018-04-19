package com.github.olegik1719.godville.arena;

public class AnyChronicGetter implements ChronicGetter {

    private WebChronicGetter webGetter;
    private FileChronicGetter fileGetter;

    public AnyChronicGetter(){
        webGetter = new WebChronicGetter();
        fileGetter = new FileChronicGetter();
    }

    public AnyChronicGetter setWeb(String pathOnWeb){
        this.webGetter = new WebChronicGetter(pathOnWeb);
        return this;
    }

    public AnyChronicGetter setDisk(String pathOnDisk){
        this.fileGetter = new FileChronicGetter(pathOnDisk);
        return this;
    }


    @Override
    public String getHtml(String chronicleID) {
        String result;
        if ((result = fileGetter.getHtml(chronicleID)).equals(""))
            if ((result = webGetter.getHtml(chronicleID)).equals(""))
                return "";
        return result;
    }
}
