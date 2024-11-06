package org.example;

public class Branch {
    private int length = 1;

    public void grow() {
        length += 1;
        System.out.println("Branch grows to length " + length);
    }

    public int getLength() {
        return length;
    }
}
