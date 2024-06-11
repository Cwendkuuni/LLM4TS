import static org.junit.Assert.*;
import net.sf.javaml.core.AbstractInstance;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        int expectedID = 0;
        int actualID = instance.getID();
        assertEquals(expectedID, actualID);

        AbstractInstance instance2 = new DenseInstance(new double[]{4.0, 5.0, 6.0});
        int expectedID2 = 1;
        int actualID2 = instance2.getID();
        assertEquals(expectedID2, actualID2);
    }
}