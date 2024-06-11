import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.0);
    }

    @Test
    public void testDenseInstanceWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(classValue, instance.classValue());
    }

    // Test other basic JUnit test cases here...

    // Test edge cases and exception handling tests here...

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyConstructorThrowsIllegalArgumentException() {
        new DenseInstance();
    }

    // Test for negative size constructor
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSizeConstructorThrowsIllegalArgumentException() {
        new DenseInstance(-3);
    }

    // Test for out of bounds index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueOutOfBoundsThrowsIndexOutOfBoundsException() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.value(-1);
    }

    // Test for null key in containsKey
    @Test
    public void testContainsKeyWithNullKeyReturnsFalse() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsKey(null));
    }

    // Test for null value in containsValue
    @Test
    public void testContainsValueWithNullValueReturnsFalse() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsValue(null));
    }

    // Test for null key in get
    @Test(expected = NullPointerException.class)
    public void testGetWithNullKeyThrowsNullPointerException() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.get(null);
    }

    // Test for removeAttribute with out of bounds index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttributeOutOfBoundsThrowsIndexOutOfBoundsException() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(3);
    }

    // Test for null Set in removeAttributes
    @Test(expected = NullPointerException.class)
    public void testRemoveAttributesWithNullSetThrowsNullPointerException() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttributes(null);
    }

    // Test for remove key that is not present in the instance
    @Test
    public void testRemoveKeyNotPresentReturnsNull() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertNull(instance.remove(4));
    }

    // Test for equals with same instance
    @Test
    public void testEqualsWithSameInstanceReturnsTrue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = instance1;
        assertTrue(instance1.equals(instance2));
    }

    // Test for equals with different instance but same data
    @Test
    public void testEqualsWithDifferentInstanceButSameDataReturnsTrue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes);
        DenseInstance instance2 = new DenseInstance(attributes);
        assertTrue(instance1.equals(instance2));
    }

    // Test for equals with different instance and different data
    @Test
    public void testEqualsWithDifferentInstanceAndDifferentDataReturnsFalse() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{4.0, 5.0, 6.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertFalse(instance1.equals(instance2));
    }

}