package org.example;

public class Main {
    public static void main(String[] args) {
        Tree leafyTree = new LeafyTree();
        leafyTree.grow();
        leafyTree.photosynthesis();

        Tree coniferTree = new ConiferTree();
        coniferTree.grow();
        coniferTree.photosynthesis();
    }
}
