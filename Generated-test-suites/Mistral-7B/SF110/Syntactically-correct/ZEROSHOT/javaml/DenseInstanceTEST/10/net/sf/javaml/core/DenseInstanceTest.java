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
        DenseInstance instance = new DenseInstance(attributes);
        assertArrayEquals(attributes, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        Object classValue = "ClassValue";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 3;
        DenseInstance instance = new DenseInstance(size);
        double[] expectedAttributes = new double[size];
        assertArrayEquals(expectedAttributes, instance.attributes, 0.0);
    }

    @Test
    public void testValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> expectedEntrySet = new HashSet<>();
        expectedEntrySet.add(Map.entry(0, 1.0));
        expectedEntrySet.add(Map.entry(1, 2.0));
        expectedEntrySet.add(Map.entry(2, 3.0));
        Set<Map.Entry<Integer, Double>> actualEntrySet = instance.entrySet();
        assertEquals(expectedEntrySet, actualEntrySet);
    }

    @Test
    public void testGet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
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
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        SortedSet<Integer> expectedKeySet = new TreeSet<>();
        expectedKeySet.addAll(Arrays.asList(0, 1, 2));
        SortedSet<Integer> actualKeySet = instance.keySet();
        assertEquals(expectedKeySet, actualKeySet);
    }

    @Test
    public void testPut() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        double newValue = 4.0;
        instance.put(0, newValue);
        assertEquals(newValue, instance.value(0), 0.0);
        assertArrayEquals(new double[] {newValue, 2.0, 3.0}, instance.attributes, 0.0);
    }

    @Test
    public void testPutAll() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        instance.putAll(map);
        assertEquals(5.0, instance.value(1), 0.0);
        assertEquals(4.0, instance.value(0), 0.0);
        assertArrayEquals(new double[] {4.0, 5.0, 3.0}, instance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.remove(0);
    }

    @Test
    public void testSize() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testValues() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        Collection<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0);
        Collection<Double> actualValues = instance.values();
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testNoAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        String expectedString = "[1.0, 2.0, 3.0;ClassValue]";
        String actualString = instance.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testRemoveAttribute() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        assertArrayEquals(new double[] {1.0, 3.0}, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance instance1 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance instance3 = new DenseInstance(new double[] {1.1, 2.1, 3.1});
        DenseInstance instance4 = instance1;
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(instance4));
        assertFalse(instance1.equals(null));
    }

    @Test
    public void testCopy() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0});
        DenseInstance copy = instance.copy();
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertArrayEquals(instance.attributes, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[] {1.0, 2.0, 3.0, 4.0});
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertArrayEquals(new double[] {1.0, 4.0}, instance.attributes, 0.0);
    }
}