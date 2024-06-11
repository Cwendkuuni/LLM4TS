import org.junit.Test;
import static org.junit.Assert.*;

public class AbstractInstanceTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        int expectedID = 1;
        int actualID = instance.getID();
        assertEquals(expectedID, actualID);
    }
}