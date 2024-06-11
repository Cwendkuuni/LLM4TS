import static org.junit.Assert.*;
import static net.sf.javaml.core.Utils.*;

import java.util.*;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "ClassA";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testConstructorWithEmptyDoubleArray() {
        DenseInstance denseInstance = new DenseInstance(new double[0]);
        assertEquals(0, denseInstance.noAttributes());
        assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithIntSize() {
        DenseInstance denseInstance = new DenseInstance(5);
        assertEquals(5, denseInstance.noAttributes());
        assertArrayEquals(new double[5], denseInstance.attributes, 0.0);
    }

    @Test
    public void testGetValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.value(5);
    }

    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, denseInstance.value(key).intValue());
            assertEquals(value, denseInstance.value(key), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.get(0), 0.0);
        assertEquals(2.0, denseInstance.get(1), 0.0);
        assertEquals(3.0, denseInstance.get(2), 0.0);
        try {
            denseInstance.get(3);
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());

        denseInstance.clear();
        assertTrue(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(3, keySet.size());
        for (Integer key : keySet) {
            assertEquals(key, denseInstance.value(key).intValue());
        }
    }

    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        double newValue = 4.0;
        DenseInstance denseInstance = new DenseInstance(attributes);
        double oldValue = denseInstance.put(0, newValue);
        assertEquals(newValue, denseInstance.value(0), 0.0);
        assertEquals(1.0, oldValue, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutWithInvalidKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        double newValue = 4.0;
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(4, newValue);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {4.0, 5.0, 6.0};
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        DenseInstance denseInstance = new DenseInstance(attributes1);
        denseInstance.putAll(map);
        assertEquals(6, denseInstance.noAttributes());
        for (int i = 0; i < 6; ++i) {
            assertEquals(i < 3 ? attributes1[i] : attributes2[i - 3], denseInstance.value(i), 0.0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.remove(0);
    }

    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, denseInstance.size());
    }

    @Deprecated
    @Test
    public void testSizeDeprecated() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());
        for (Double value : values) {
            assertEquals(value.doubleValue(), attributes[Arrays.asList(values.toArray()).indexOf(value)], 0.0);
        }
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        assertEquals(2, denseInstance.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0}, denseInstance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        attributes2[0] = 4.0;
        assertNotSame(denseInstance1.hashCode(), denseInstance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertTrue(denseInstance1.equals(denseInstance2));
        assertTrue(denseInstance2.equals(denseInstance1));

        attributes2[0] = 4.0;
        assertFalse(denseInstance1.equals(denseInstance2));
        assertFalse(denseInstance2.equals(denseInstance1));

        DenseInstance denseInstance3 = new DenseInstance();
        assertFalse(denseInstance1.equals(denseInstance3));
        assertFalse(denseInstance3.equals(denseInstance1));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Instance copiedInstance = denseInstance.copy();
        assertEquals(3, copiedInstance.noAttributes());
        assertArrayEquals(attributes, copiedInstance.attributes, 0.0);
        assertEquals(denseInstance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        Set<Integer> indicesToRemove = new HashSet<>();
        indicesToRemove.add(1);
        indicesToRemove.add(3);
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(2, denseInstance.noAttributes());
        assertArrayEquals(new double[]{1.0, 4.0}, denseInstance.attributes, 0.0);
    }
}