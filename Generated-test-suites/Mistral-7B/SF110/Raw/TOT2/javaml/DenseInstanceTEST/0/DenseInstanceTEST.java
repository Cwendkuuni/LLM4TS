import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }
}