package de.jdsoft.nyup.Level;


import java.util.ArrayList;
import java.util.List;

public class LevelMapping {

    public static final List<LevelRule> map;

    static  {
        map = new ArrayList<LevelRule>();
        map.add(new Level000());
        map.add(new Level001());
        map.add(new Level002());
        map.add(new Level003());
    }
}
