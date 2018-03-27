package com.github.olegik1719.godville.arena;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    private static final String ERINOME_PREFIX="https://gv.erinome.net/duels/log/";
    private static final String LOG_ID_REGEXP = "http[s]{0,1}:\\/\\/[^\\/]+\\/duels\\/log\\/([A-Za-z0-9]+).*";//http[s]{0,1}:\/\/[^\/]+\/duels\/log\/([A-Za-z0-9]+).*
    private static final Pattern PATTERN_LOG = Pattern.compile(LOG_ID_REGEXP);
    private static Set<String> error_logs = new TreeSet<>();

    private static HashMap<String, Integer> exchange;
    static {
        exchange = new HashMap<>();
        exchange.put("монет",1);//золотые монеты
        exchange.put("монеты",1);
        exchange.put("золотых монет",1);
        exchange.put("золотые монеты",1);
        exchange.put("золотых",1);
        exchange.put("золотой",1);
        exchange.put("золотых червонца",10);
        exchange.put("золотых червонца",10);
    }

    public static String getID(String url){
        Matcher matcher = PATTERN_LOG.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }

        //System.out.println(url);
        error_logs.add(url);
        return null;
    }

    public static String getLink(String id){
        return ERINOME_PREFIX+id;
    }

    public static int getMoney(int sum, String currency){
        for (String cur:exchange.keySet()) {
            if (currency.equals(cur)) return sum*exchange.get(cur);
        }
        throw new RuntimeException(currency);
    }

    public static void main(String ... args){
        if (args.length > 0){
            try{
                FileInputStream fstream = new FileInputStream(args[0]);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine;
                HashSet<String> ids = new HashSet<>();
                while ((strLine = br.readLine()) != null){
                    String id = getID(strLine);
                    if (id != null)
                        ids.add(id);
                }
                if (args.length > 1){
                    try(FileWriter writer = new FileWriter(args[1],false))
                    {
                        for (String errorLog: error_logs) {
                            writer.write(errorLog);
                            writer.write("\n");
                        }
                    }
                    catch(IOException ex){
                        System.out.println(ex.getMessage());
                    }
                }else {
                    for (String error : error_logs) System.out.println(error);
                }
                Fights fights = new Fights(ids);
                if (args.length > 2){
                    try(FileWriter writer = new FileWriter(args[2],false))
                    {
                        for (String goodlog: fights.getGood()) {
                            writer.write(getLink(goodlog) + "\n");
                        }
                    }
                    catch(IOException ex){
                        System.out.println(ex.getMessage());
                    }
                }

                System.out.println(fights.getSize());
                System.out.println(fights.getZPGcount());
                System.out.println(fights.getYoungCount());
            }catch (IOException e){
                System.out.println("Ошибка");
            }
        }
    }
}
