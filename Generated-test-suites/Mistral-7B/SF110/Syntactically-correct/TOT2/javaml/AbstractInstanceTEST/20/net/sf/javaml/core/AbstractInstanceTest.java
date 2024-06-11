import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.AbstractInstance;

public class GetIDTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[1]);
        assertEquals(0, instance.getID());
        instance = new DenseInstance(new double[2]);
        assertEquals(1, instance.getID());
    }
}