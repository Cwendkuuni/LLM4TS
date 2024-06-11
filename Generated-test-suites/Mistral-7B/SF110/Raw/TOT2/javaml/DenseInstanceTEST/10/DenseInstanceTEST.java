import static org.junit.Assert.*;

import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstructionWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertNotNull(denseInstance);
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testDenseInstanceConstructionWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "label";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertNotNull(denseInstance);
        assertEquals(classValue, denseInstance.getClassValue());
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testDenseInstanceConstructionWithEmptyDoubleArray() {
        DenseInstance denseInstance = new DenseInstance(new double[0]);
        assertNotNull(denseInstance);
        assertEquals(0, denseInstance.noAttributes());
    }

    // Test methods for getters and setters, isEmpty, and keySet()

}