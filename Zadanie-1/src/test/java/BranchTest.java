import org.example.Branch;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BranchTest {

    @Test
    public void testInitialLength() {
        Branch branch = new Branch();
        assertEquals(1, branch.getLength());
    }

    @Test
    public void testGrow() {
        Branch branch = new Branch();
        branch.grow();
        assertEquals(2, branch.getLength());
    }
}
