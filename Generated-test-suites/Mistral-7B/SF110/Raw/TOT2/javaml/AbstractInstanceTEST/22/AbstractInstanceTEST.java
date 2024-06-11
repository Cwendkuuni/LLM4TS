import static org.junit.Assert.*;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[5]);
        assertEquals(0, instance.getID());
        instance = new DenseInstance(new double[5], 1);
        assertEquals(1, instance.getID());
    }
}