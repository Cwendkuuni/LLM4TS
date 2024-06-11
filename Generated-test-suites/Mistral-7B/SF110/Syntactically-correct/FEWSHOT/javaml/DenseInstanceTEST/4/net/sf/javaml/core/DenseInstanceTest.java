package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithEmptyAttributeArray() {
        DenseInstance instance = new DenseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testDenseInstanceWithOneAttribute() {
        double[] attributes = new double[] { 1.0 };
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.value(0), 0.0);
    }

    @Test
    public void testDenseInstanceWithMultipleAttributes() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testDenseInstanceWithClassValue() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        Object classValue = "Class Value";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testGetAttributeValueByIndex() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testSetAttributeValueByIndex() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        instance.put(0, 5.0);
        assertEquals(5.0, instance.value(0), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributeByIndex() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.containsValue(4.0));
        assertFalse(instance.containsValue(2.5));
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        try {
            instance.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[] {};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.isEmpty());

        attributes = new double[] { 1.0 };
        instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        for (Integer key : keySet) {
            assertTrue(instance.containsKey(key));
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(attributes);
        instance.put(0, 5.0);
        assertEquals(5.0, instance.value(0), 0.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = new double[] { 1.0, 2.0, 3.0 };
        double[] attributes2 = new double[] { 4.0, 5.0, 6.0 };
        DenseInstance instance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        map.put(3, 6.0);
        instance.putAll(map);
        assertEquals(6, instance.noAttributes());
        assertEquals(4.0, instance.value(1), 0.0);
        assertEquals(5.0, instance.value(2), 0.0);
        assertEquals(6.0, instance.value(3), 0.0);
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[] { 1.0, 2.0, 3.0, 4.0 };
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
        instance.removeAttributes(indicesToRemove);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(3));
    }
}