import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithDoubleArray() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
    }

    @Test
    public void testDenseInstanceWithDoubleArrayAndClassValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(classValue, denseInstance.getClassValue());
    }

    // Test other methods with basic JUnit 4 test cases...
}