import static org.junit.Assert.*;
import org.junit.Test;
import net.sf.javaml.core.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        // Test other method assertions here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullArray() {
        DenseInstance denseInstance = new DenseInstance(null);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "SomeClass";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(classValue, denseInstance.getClassValue());
        // Test other method assertions here
    }

    @Test
    public void testConstructorWithEmptyArray() {
        DenseInstance denseInstance = new DenseInstance(new double[0]);
        assertEquals(0, denseInstance.noAttributes());
        // Test other method assertions here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeSize() {
        DenseInstance denseInstance = new DenseInstance(-1);
    }

    @Test
    public void testValue() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        double value = denseInstance.value(1);
        assertEquals(2.0, value, 1.0E-8);
        // Test other method assertions here
    }

    @Test
    public void testClear() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        // Test other method assertions here
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsKeyWithNegativeKey() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(denseInstance.containsKey(-1));
    }

    @Test
    public void testContainsValue() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertTrue(denseInstance.containsValue(2.0));
        // Test other method assertions here
    }

    @Test
    public void testEntrySet() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        // Test other method assertions here
    }

    // Add other test methods for the remaining methods here

    @Test
    public void testPut() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Double oldValue = denseInstance.put(0, 4.0);
        assertEquals(1.0, oldValue.doubleValue(), 1.0E-8);
        // Test other edge cases and exception handling scenarios here
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        denseInstance.remove(0);
    }

    // Add test methods for other methods here

    @Test
    public void testRemoveAttributesWithNullSet() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = null;
        denseInstance.removeAttributes(indices);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveAttributesWithNegativeIndices() {
        DenseInstance denseInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> indices = new HashSet<Integer>() {{
            add(-1);
            add(-2);
        }};
        denseInstance.removeAttributes(indices);
    }
}