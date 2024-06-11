import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.AbstractInstance;

public class GetIDTest {
    @Test
    public void testGetID() {
        AbstractInstance instance = new DenseInstance(new double[] {1.0, 2.0});
        assertEquals(0, instance.getID());
        instance = new DenseInstance(new double[] {1.0, 2.0});
        AbstractInstance.nextID = 1;
        instance = new AbstractInstance(new Double(3.0));
        assertEquals(1, instance.getID());
    }
}