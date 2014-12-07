package de.jdsoft.nyup.Utils;


public class Vector2i {

    public int x;
    public int y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + this.x + ":" + this.y + "]";
    }

    public Vector2i add(int i, int i1) {
        return new Vector2i(x + i, y + i1);
    }
}
