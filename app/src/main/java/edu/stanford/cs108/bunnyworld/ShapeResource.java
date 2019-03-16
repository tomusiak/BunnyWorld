package edu.stanford.cs108.bunnyworld;


public class ShapeResource {

    private String shapeName;
    private int id;

    public ShapeResource(String name, int id) {
        shapeName = name;
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String getShapeName() { return shapeName; }

}
