package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        Object classValue = "label";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance denseInstance = new DenseInstance();
        assertEquals(0, denseInstance.noAttributes());
        assertArrayEquals(new double[]{}, denseInstance.attributes, 0.0);
        assertNull(denseInstance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance denseInstance = new DenseInstance(size);
        assertEquals(5, denseInstance.noAttributes());
        assertArrayEquals(new double[5], denseInstance.attributes, 0.0);
        assertNull(denseInstance.classValue());
    }

    @Test
    public void testGetValue() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(0.5));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, (int) entry.getKey());
            assertEquals(value, denseInstance.value(key), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.get(0), 0.0);
        assertEquals(2.0, denseInstance.get(1), 0.0);
        assertEquals(3.0, denseInstance.get(2), 0.0);
        assertNull(denseInstance.get(3));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[] {};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.isEmpty());
        double[] nonEmptyAttributes = new double[] { 1.0 };
        DenseInstance nonEmptyInstance = new DenseInstance(nonEmptyAttributes);
        assertFalse(nonEmptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(3, keySet.size());
        for (int key : keySet) {
            assertTrue(denseInstance.containsKey(key));
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        double newValue = 4.0;
        Integer key = 3;
        Double oldValue = denseInstance.put(key, newValue);
        assertEquals(4, denseInstance.noAttributes());
        assertEquals(newValue, denseInstance.value(3), 0.0);
        assertNull(oldValue);
    }

    @Test
    public void testPutAll() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        double[] newAttributes = new double[] { 4.0, 5.0, 6.0 };
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        denseInstance.putAll(map);
        assertEquals(6, denseInstance.noAttributes());
        assertArrayEquals(newAttributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testRemove() {
        throw new UnsupportedOperationException("Cannot unset values from a dense instance.");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttribute() {
        throw new UnsupportedOperationException("Cannot unset values from a dense instance.");
    }

    @Test
    public void testSize() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.size());
        double[] emptyAttributes = new double[] {};
        DenseInstance emptyInstance = new DenseInstance(emptyAttributes);
        assertEquals(0, emptyInstance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());
        for (Double value : values) {
            assertTrue(value.isFinite());
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        double[] emptyAttributes = new double[] {};
        DenseInstance emptyInstance = new DenseInstance(emptyAttributes);
        assertEquals(0, emptyInstance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        String expected = "{" + Arrays.toString(attributes) + ";" + denseInstance.classValue() + "}";
        assertEquals(expected, denseInstance.toString());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
        denseInstance.removeAttributes(indices);
        assertEquals(3, denseInstance.noAttributes());
        assertArraySubset(new double[] { 1.0, 3.0 }, denseInstance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
        double[] attributes2 = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        double[] attributes3 = new double[] { 1.0, 2.0, 4.0 };
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
        double[] attributes2 = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertTrue(denseInstance1.equals(denseInstance2));

        double[] attributes3 = new double[] { 1.0, 2.0, 4.0 };
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertFalse(denseInstance1.equals(denseInstance3));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copy = denseInstance.copy();
        assertEquals(denseInstance, copy);
    }
}