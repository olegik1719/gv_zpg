package com.github.olegik1719.godville.arena;

import java.util.*;

public class Player {
    private final String nikname;
    private Collection<Fight> fights;


    public Player(String nikname){
        this.nikname = nikname;
        fights = new HashSet<>();
    }

    public Player addLog(Fight fight){
        fights.add(fight);
        return this;
    }

//    public Collection<Fight> getFights() {
//        Collection<Fight> result = new ArrayList<>(fights);
//        //result
//        Collections.sort(result,fight -> fight.);
//        return fights;
//    }

    //public Player
}
