package com.github.olegik1719.godville.input;

public class GettingUrl {
    static final String ERINOME_PREFIX="https://gv.erinome.net/duels/log/";
    private String url;
    public GettingUrl(){
        url="";
    }

    public GettingUrl(String input){
        this();
        this.setUrl(input);
    }

    public GettingUrl setUrl(String input){
        url = input.substring(input.lastIndexOf('/')+1);
        return this;
    }

    public String getUrl() {
        return ERINOME_PREFIX + url;
    }
}
