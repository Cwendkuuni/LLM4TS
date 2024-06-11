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
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(classValue, denseInstance.classValue());
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithEmptyArray() {
        DenseInstance denseInstance = new DenseInstance(new double[0]);
        assertEquals(0, denseInstance.noAttributes());
        assertArrayEquals(new double[0], denseInstance.attributes, 0.0);
    }

    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
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
        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (double value : attributes) {
            assertTrue(denseInstance.containsValue(value));
        }
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
        assertEquals(3, entries.size());
        int i = 0;
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(i++, entry.getKey());
            assertEquals(attributes[i], entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.get(0), 0.0);
        assertEquals(2.0, denseInstance.get(1), 0.0);
        assertEquals(3.0, denseInstance.get(2), 0.0);
        assertNull(denseInstance.get(-1));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());

        DenseInstance emptyInstance = new DenseInstance();
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keys = denseInstance.keySet();
        assertEquals(3, keys.size());
        for (Integer key : keys) {
            assertTrue(denseInstance.containsKey(key));
            assertEquals(key.intValue(), key.intValue());
        }
    }

    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Double oldValue = denseInstance.put(0, 4.0);
        assertEquals(1.0, oldValue, 0.0);
        assertEquals(4.0, denseInstance.value(0), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutThrowsException() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.put(0, 4.0);
        denseInstance.put(0, 5.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {4.0, 5.0, 6.0};
        DenseInstance denseInstance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        map.put(3, 6.0);
        denseInstance.putAll(map);
        assertEquals(6, denseInstance.noAttributes());
        for (int i = 0; i < 6; ++i) {
            if (i < 3) {
                assertEquals(attributes1[i], denseInstance.attributes[i], 0.0);
            } else {
                assertEquals(attributes2[i - 3], denseInstance.attributes[i], 0.0);
            }
        }
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(2);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(new double[] {1.0, 2.0, 4.0}, denseInstance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributeThrowsException() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(-1);
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<Integer>(Arrays.asList(1, 2));
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(2, denseInstance.noAttributes());
        assertArrayEquals(new double[] {1.0, 3.0}, denseInstance.attributes, 0.0);
    }

    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.size());
        DenseInstance emptyInstance = new DenseInstance();
        assertEquals(0, emptyInstance.size());
    }

    @Deprecated
    @Test
    public void testSizeDeprecated() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        List<Double> values = new ArrayList<Double>(Arrays.asList(1.0, 2.0, 3.0));
        assertEquals(values, denseInstance.values());
        Collections.sort(values);
        assertEquals(values, denseInstance.values());
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertEquals(denseInstance1.hashCode(), denseInstance2.hashCode());

        double[] attributes3 = {1.1, 2.1, 3.1};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        assertTrue(denseInstance1.equals(denseInstance2));

        double[] attributes3 = {1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertFalse(denseInstance1.equals(denseInstance3));

        Object other = new Object();
        assertFalse(denseInstance1.equals(other));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        assertEquals(denseInstance, copiedInstance);
        assertArrayEquals(denseInstance.attributes, copiedInstance.attributes, 0.0);
    }
}