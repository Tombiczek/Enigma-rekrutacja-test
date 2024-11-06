package org.example;

public class Leaf {
    private final String color;

    public Leaf(String color) {
        this.color = color;
    }

    public void photosynthesis() {
        System.out.println(color + " leaf is performing photosynthesis");
    }

    public String getColor() {
        return color;
    }
}
