package org.example;

import java.util.ArrayList;
import java.util.List;

public class LeafyTree extends Tree {
    private final List<Leaf> leaves = new ArrayList<>();

    public LeafyTree() {
        leaves.add(new Leaf("Green"));
    }

    @Override
    public void grow() {
        System.out.println("Leafy Tree grows...");
        trunk.grow();
        branches.forEach(Branch::grow);
        leaves.add(new Leaf("New Green"));
    }

    @Override
    public void photosynthesis() {
        super.photosynthesis();
        leaves.forEach(Leaf::photosynthesis);
    }

    public List<Leaf> getLeaves() {
        return leaves;
    }
}
