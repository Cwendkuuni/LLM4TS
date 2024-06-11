package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "Class1";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithEmptyDoubleArray() {
        DenseInstance instance = new DenseInstance(new double[0]);
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 1.5, 2.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(1.5));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        int i = 0;
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(i++, entry.getKey());
            assertEquals(attributes[i], entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        try {
            instance.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());

        DenseInstance emptyInstance = new DenseInstance();
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> keys = instance.keySet();
        int i = 0;
        for (Integer key : keys) {
            assertEquals(i++, key);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.put(4, 4.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<>();
        map.put(3, 3.0);
        map.put(4, 4.0);
        map.put(5, 5.0);
        instance.putAll(map);
        int i = 0;
        for (double attribute : instance.values()) {
            assertEquals(attributes2[i++], attribute, 0.0);
        }
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        double[] attributes3 = {1.0, 2.1, 3.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertTrue(instance1.equals(instance2));

        double[] attributes3 = {1.0, 2.1, 3.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertFalse(instance1.equals(instance3));

        DenseInstance instance4 = instance1.copy();
        assertTrue(instance1.equals(instance4));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        assertArrayEquals(instance.attributes, copiedInstance.attributes, 0.0);
        assertEquals(instance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<Integer>(Arrays.asList(1, 3));
        instance.removeAttributes(indicesToRemove);
        double[] expectedAttributes = {1.0, 4.0};
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }
}