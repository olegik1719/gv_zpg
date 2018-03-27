package com.github.olegik1719.godville.arena;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class Fights implements Serializable{
    private HashMap<String, Fight> fights;
    public Fights(Collection<String> args) {
        fights = new HashMap<>();
        for (String id:args) {
            if (fights.get(id) == null){
                fights.put(id, new Fight(id));
            }
        }
    }

    public int getSize(){
        return fights.size();
    }

    public Fights addFight(String url){
        String id = Common.getID(url);
        if (fights.get(id) == null){
            fights.put(id, new Fight(id));
        }
        return this;
    }
}
