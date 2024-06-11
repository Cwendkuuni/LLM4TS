import static org.junit.Assert.*;
import net.sf.javaml.core.*;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        assertEquals(0, instance.getID());
    }

    @Test
    public void testIterator() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0});
        Iterator<Double> it = instance.iterator();
        assertEquals(1.0, it.next());
        assertEquals(2.0, it.next());
        assertFalse(it.hasNext());
    }

    // ... Add test cases for the remaining methods here
}