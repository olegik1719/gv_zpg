package com.github.olegik1719.godville.arena;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Fights{
    private HashMap<String, Fight> fights;
    public Fights(Collection<String> args) {
        fights = new HashMap<>();
        for (String id:args) {
            if (fights.get(id) == null){
                Fight fight =  new Fight(id);
                if (fight.getTime() != null) {
                    fights.put(id, new Fight(id));
                }
            }
        }
    }

    public int getSize(){
        return fights.size();
    }

    public Collection<String> getGood(){
        return fights.keySet();
    }

    public int getZPGcount(){
        Set<String> set = new HashSet<>();
        for(String id:fights.keySet()){
            if (fights.get(id).isZPG()){
                set.add(id);
            }
        }
        return set.size();
    }

    public int getYoungCount(){
        Set<String> set = new HashSet<>();
        for(String id:fights.keySet()){
            if (fights.get(id).isYoung()){
                set.add(id);
            }
        }
        return set.size();
    }



//    public Fights addFight(String url){
//        String id = Common.getID(url);
//        if (fights.get(id) == null){
//            fights.put(id, new Fight(id));
//        }
//        return this;
//    }
}
