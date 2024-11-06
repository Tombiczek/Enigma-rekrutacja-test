import org.example.Branch;
import org.example.ConiferTree;
import org.example.Needle;
import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConiferTreeTest {

    @Test
    public void testInitialSetup() {
        ConiferTree tree = new ConiferTree();

        assertNotNull(tree.getTrunk());
        assertEquals(5, tree.getTrunk().getHeight());

        List<Branch> branches = tree.getBranches();
        assertEquals(1, branches.size());
        assertEquals(1, branches.getFirst().getLength());

        List<Needle> needles = tree.getNeedles();
        assertEquals(1, needles.size());
    }

    @Test
    public void testGrow() {
        ConiferTree tree = new ConiferTree();
        tree.grow();

        assertEquals(7, tree.getTrunk().getHeight());

        List<Branch> branches = tree.getBranches();
        assertEquals(1, branches.size());
        assertEquals(2, branches.getFirst().getLength());

        List<Needle> needles = tree.getNeedles();
        assertEquals(2, needles.size());
    }
}
