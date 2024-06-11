import static org.junit.Assert.*;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        int expectedID = 1; // ID of the instance
        int actualID = instance.getID();
        assertEquals(expectedID, actualID);
    }
}