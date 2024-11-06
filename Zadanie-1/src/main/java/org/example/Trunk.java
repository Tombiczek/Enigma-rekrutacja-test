package org.example;

public class Trunk {
    private int height = 5;

    public void grow() {
        height += 2;
        System.out.println("Trunk grows to height: " + height);
    }

    public int getHeight() {
        return height;
    }
}
