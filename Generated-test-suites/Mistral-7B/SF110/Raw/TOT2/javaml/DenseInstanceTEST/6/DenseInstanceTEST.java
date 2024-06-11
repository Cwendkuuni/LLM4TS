import static org.junit.Assert.*;
import net.sf.javaml.core.DenseInstance;

public class DenseInstanceTest {
    @Test
    public void testValue() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        double actual = denseInstance.value(1);
        double expected = 2.0;
        assertEquals(expected, actual);
    }
}