package edu.stanford.cs108.bunnyworld;

import java.util.ArrayList;

public class Page {

    ArrayList<Shape> pageState = new ArrayList<Shape>();

    public ArrayList<Shape> getList() {
        return pageState;
    }

    public void updateList(ArrayList<Shape> newState) {
        pageState = newState;
    }
}
