package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Before
    public void setup() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        denseInstance = new DenseInstance(attributes);
    }

    @Test
    public void testConstructorWithArray() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        assertNotNull(instance);
        assertEquals(4, instance.noAttributes());
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, "ClassValue");
        assertNotNull(instance);
        assertEquals("ClassValue", instance.classValue());
        assertEquals(4, instance.noAttributes());
    }

    @Test
    public void testConstructorWithSize() {
        DenseInstance instance = new DenseInstance(5);
        assertNotNull(instance);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testValue() {
        double value = denseInstance.value(0);
        assertEquals(1.0, value, 0.0);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsKeyWithNegativeNumber() {
        denseInstance.containsKey(-1);
    }

    @Test
    public void testContainsKeyWithInteger() {
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertTrue(denseInstance.containsKey(3));
        assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        assertTrue(denseInstance.containsValue(1.0));
        assertTrue(denseInstance.containsValue(2.0));
        assertTrue(denseInstance.containsValue(3.0));
        assertTrue(denseInstance.containsValue(4.0));
        assertFalse(denseInstance.containsValue(5.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
        assertEquals(4, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, (int) value);
        }
    }

    @Test
    public void testGet() {
        double value = denseInstance.get(0);
        assertEquals(1.0, value, 0.0);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        Set<Integer> keys = denseInstance.keySet();
        assertEquals(4, keys.size());
        for (int key : keys) {
            assertTrue(denseInstance.containsKey((Object)key));
        }
    }

    @Test
    public void testPut() {
        double newValue = 5.0;
        double oldValue = denseInstance.put(0, newValue);
        assertEquals(1.0, oldValue, 0.0);
        assertEquals(5.0, denseInstance.value(0), 0.0);
    }

    @Test
    public void testPutAll() {
        DenseInstance anotherInstance = new DenseInstance(new double[]{5.0, 6.0, 7.0, 8.0});
        denseInstance.putAll(anotherInstance.entrySet());
        assertEquals(8, denseInstance.noAttributes());
        for (int i = 0; i < 8; ++i) {
            assertEquals(i < 4 ? anotherInstance.value(i) : denseInstance.value(i - 4), denseInstance.value(i), 0.0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        denseInstance.remove(0);
    }

    @Test
    public void testSize() {
        assertEquals(4, denseInstance.size());
    }

    @Test
    public void testValues() {
        List<Double> values = new ArrayList<>(denseInstance.values());
        assertEquals(Arrays.asList(new Double[]{1.0, 2.0, 3.0, 4.0}), values);
    }

    @Test
    public void testNoAttributes() {
        assertEquals(4, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        String toString = denseInstance.toString();
        String expected = "{" + Arrays.toString(denseInstance.attributes) + ";" + denseInstance.classValue() + "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        assertEquals(3, denseInstance.noAttributes());
        assertFalse(denseInstance.containsKey(1));
    }

    @Test
    public void testHashCode() {
        DenseInstance anotherInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        assertEquals(denseInstance.hashCode(), anotherInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance anotherInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        assertTrue(denseInstance.equals(anotherInstance));
    }

    @Test
    public void testCopy() {
        DenseInstance copiedInstance = denseInstance.copy();
        assertEquals(denseInstance, copiedInstance);
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(2, denseInstance.noAttributes());
        assertFalse(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(2));
    }
}