package saxpath;
import static org.junit.Assert.*;
import org.junit.Test;

public class AxisTest {
    @Test
    public void testLookupByAxisNumber() {
        // Test valid cases
        assertEquals("child", Axis.lookup(1));
        assertEquals("descendant", Axis.lookup(2));
        assertEquals("parent", Axis.lookup(3));
        assertEquals("ancestor", Axis.lookup(4));
        assertEquals("following-sibling", Axis.lookup(5));
        assertEquals("preceding-sibling", Axis.lookup(6));
        assertEquals("following", Axis.lookup(7));
        assertEquals("preceding", Axis.lookup(8));
        assertEquals("attribute", Axis.lookup(9));
        assertEquals("namespace", Axis.lookup(10));
        assertEquals("self", Axis.lookup(11));
        assertEquals("descendant-or-self", Axis.lookup(12));
        assertEquals("ancestor-or-self", Axis.lookup(13));

        // Test invalid case
        assertNull(Axis.lookup(0));
    }
}