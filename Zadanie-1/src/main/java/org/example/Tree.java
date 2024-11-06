package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Tree {
    protected Trunk trunk = new Trunk();
    protected List<Branch> branches = new ArrayList<>();

    public Tree() {
        branches.add(new Branch());
    }

    public abstract void grow();

    public void photosynthesis() {
        System.out.println(getClass().getSimpleName() + " is performing photosynthesis");
    }

    public List<Branch> getBranches() {
        return branches;
    }

    public Trunk getTrunk() {
        return trunk;
    }
}
