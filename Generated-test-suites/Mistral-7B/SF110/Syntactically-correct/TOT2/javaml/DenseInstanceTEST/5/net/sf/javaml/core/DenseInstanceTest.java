import static org.junit.Assert.assertEquals;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {
    @Test
    public void testValue() {
        DenseInstance denseInst = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, denseInst.value(0), 0.001);
        assertEquals(2.0, denseInst.value(1), 0.001);
        assertEquals(3.0, denseInst.value(2), 0.001);
    }
}