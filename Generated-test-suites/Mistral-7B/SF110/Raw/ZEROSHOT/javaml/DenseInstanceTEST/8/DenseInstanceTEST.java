package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(null, denseInstance.getClassValue());
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(classValue, denseInstance.getClassValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance denseInstance = new DenseInstance(size);
        assertEquals(size, denseInstance.noAttributes());
        assertArrayEquals(new double[size], denseInstance.attributes, 0.0);
        assertEquals(null, denseInstance.getClassValue());
    }

    @Test
    public void testValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(3, entrySet.size());
        int index = 0;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(index++, entry.getKey());
            assertEquals(attributes[index], entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.get(0), 0.0);
        assertEquals(2.0, denseInstance.get(1), 0.0);
        assertEquals(3.0, denseInstance.get(2), 0.0);
        try {
            denseInstance.get(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[0];
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.isEmpty());
        double[] nonEmptyAttributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance nonEmptyDenseInstance = new DenseInstance(nonEmptyAttributes);
        assertFalse(nonEmptyDenseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(3, keySet.size());
        int index = 0;
        for (Integer key : keySet) {
            assertEquals(index++, key);
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double newValue = 4.0;
        int key = 3;
        double oldValue = denseInstance.put(key, newValue);
        assertEquals(4.0, denseInstance.value(3), 0.0);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
        assertEquals(1.0, oldValue, 0.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        denseInstance.putAll(map);
        assertEquals(6, denseInstance.noAttributes());
        assertArrayEquals(new double[] {1.0, 4.0, 5.0, 3.0, 6.0}, denseInstance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.remove(0);
    }

    @Test
    public void testSize() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.size());
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());
        int index = 0;
        for (Double value : values) {
            assertEquals(attributes[index++], value, 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        String expected = "{" + Arrays.toString(attributes) + ";" + denseInstance.getClassValue() + "}";
        assertEquals(expected, denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(new double[] {1.0, 3.0, 4.0}, denseInstance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        double[] attributes2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        double[] attributes3 = new double[] {1.0, 2.0, 3.1};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        double[] attributes2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertTrue(denseInstance1.equals(denseInstance2));

        double[] attributes3 = new double[] {1.0, 2.1, 3.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertFalse(denseInstance1.equals(denseInstance3));

        double[] attributes4 = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance4 = new DenseInstance(attributes4);
        DenseInstance denseInstance5 = new DenseInstance(attributes4);
        assertTrue(denseInstance1.equals(denseInstance4));
        assertTrue(denseInstance4.equals(denseInstance5));
        assertTrue(denseInstance1.equals(denseInstance5));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        assertEquals(3, copiedInstance.noAttributes());
        assertArrayEquals(attributes, copiedInstance.attributes, 0.0);
        assertEquals(denseInstance.getClassValue(), copiedInstance.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0};
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(2, denseInstance.noAttributes());
        assertArrayEquals(new double[] {1.0, 3.0}, denseInstance.attributes, 0.0);
    }
}