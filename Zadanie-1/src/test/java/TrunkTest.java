import org.example.Trunk;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrunkTest {

    @Test
    public void testInitialHeight() {
        Trunk trunk = new Trunk();
        assertEquals(5, trunk.getHeight());
    }

    @Test
    public void testGrow() {
        Trunk trunk = new Trunk();
        trunk.grow();
        assertEquals(7, trunk.getHeight());
    }
}
