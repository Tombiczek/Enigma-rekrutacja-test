import org.example.Branch;
import org.example.Leaf;
import org.example.LeafyTree;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LeafyTreeTest {

    @Test
    public void testInitialSetup() {
        LeafyTree tree = new LeafyTree();

        assertNotNull(tree.getTrunk());
        assertEquals(5, tree.getTrunk().getHeight());

        List<Branch> branches = tree.getBranches();
        assertEquals(1, branches.size());
        assertEquals(1, branches.getFirst().getLength());

        List<Leaf> leaves = tree.getLeaves();
        assertEquals(1, leaves.size());
        assertEquals("Green", leaves.getFirst().getColor());
    }

    @Test
    public void testGrow() {
        LeafyTree tree = new LeafyTree();
        tree.grow();

        assertEquals(7, tree.getTrunk().getHeight());

        List<Branch> branches = tree.getBranches();
        assertEquals(1, branches.size());
        assertEquals(2, branches.getFirst().getLength());

        List<Leaf> leaves = tree.getLeaves();
        assertEquals(2, leaves.size());
        assertEquals("New Green", leaves.get(1).getColor());
    }
}
