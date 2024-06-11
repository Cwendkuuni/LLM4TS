
package net.sf.javaml.core;

import static org.junit.Assert.*;
import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testValue() {
        DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
        for (int i = 0; i < denseInstance.noAttributes(); i++) {
            double expectedValue = denseInstance.attributes[i];
            double actualValue = denseInstance.value(i);
            assertEquals("Unexpected value for position " + i, expectedValue, actualValue, 0.0);
        }
    }

    @Test
    public void testClear() {
        DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
        denseInstance.clear();
        assertArrayEquals("Unexpected attributes after clearing", new double[0], denseInstance.attributes);
    }

    @Test
    public void testContainsValue() {
        DenseInstance denseInstance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
        Double valueToFind = 3.0;
        boolean containsValue = denseInstance.containsValue(valueToFind);
        assertTrue("Expected the dense instance to contain value: " + valueToFind, containsValue);

        DenseInstance denseInstanceEmpty = new DenseInstance();
        containsValue = denseInstanceEmpty.containsValue(valueToFind);
        assertFalse("Expected the empty dense instance not to contain value: " + valueToFind, containsValue);
    }

    // ... other test methods for various methods in the DenseInstance class
}
