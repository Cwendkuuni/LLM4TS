import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[0]);
        int expectedID = 0;
        int actualID = instance.getID();
        assertEquals(expectedID, actualID);
        
        AbstractInstance instance2 = new DenseInstance(new double[]{1.0});
        expectedID = 1;
        actualID = instance2.getID();
        assertEquals(expectedID, actualID);
    }
}