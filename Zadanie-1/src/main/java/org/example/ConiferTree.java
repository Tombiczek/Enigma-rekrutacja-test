package org.example;

import java.util.ArrayList;
import java.util.List;

public class ConiferTree extends Tree {
    private final List<Needle> needles = new ArrayList<>();

    public ConiferTree() {
        needles.add(new Needle());
    }

    @Override
    public void grow() {
        System.out.println("Conifer Tree grows...");
        trunk.grow();
        branches.forEach(Branch::grow);
        needles.add(new Needle());
    }

    @Override
    public void photosynthesis() {
        super.photosynthesis();
        needles.forEach(Needle::photosynthesis);
    }

    public List<Needle> getNeedles() {
        return needles;
    }
}
