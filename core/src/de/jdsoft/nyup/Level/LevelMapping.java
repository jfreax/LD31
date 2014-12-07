package de.jdsoft.nyup.Level;


import java.util.ArrayList;
import java.util.List;

public class LevelMapping {

    private static final List<LevelRule> map;

    static  {
        map = new ArrayList<LevelRule>();
        map.add(new Level000());
    }
}
