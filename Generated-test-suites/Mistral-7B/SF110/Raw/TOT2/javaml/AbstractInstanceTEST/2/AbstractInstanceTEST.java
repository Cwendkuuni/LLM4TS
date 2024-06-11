import static org.junit.Assert.*;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {

    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        assertEquals(0, instance.getID());
        AbstractInstance instance2 = new DenseInstance(new double[2]);
        assertEquals(1, instance2.getID());
    }
}