import org.example.Leaf;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LeafTest {

    @Test
    public void testLeafColor() {
        Leaf leaf = new Leaf("Green");
        assertEquals("Green", leaf.getColor());
    }
}
