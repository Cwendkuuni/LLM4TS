package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

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
        Object classValue = "Class1";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertArrayEquals(attributes, denseInstance.attributes, 0.0);
        assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testConstructorEmpty() {
        DenseInstance denseInstance = new DenseInstance();
        assertTrue(denseInstance.isEmpty());
        assertEquals(0, denseInstance.noAttributes());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance denseInstance = new DenseInstance(size);
        assertEquals(size, denseInstance.noAttributes());
        assertArrayEquals(new double[size], denseInstance.attributes, 0.0);
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
        assertTrue(denseInstance.isEmpty());
        assertEquals(0, denseInstance.noAttributes());
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
        double value = 2.0;
        assertTrue(denseInstance.containsValue(value));
        double otherValue = 4.0;
        assertFalse(denseInstance.containsValue(otherValue));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> expectedEntrySet = denseInstance.entrySet();
        Set<Map.Entry<Integer, Double>> actualEntrySet = new HashSet<Map.Entry<Integer, Double>>();
        for (int i = 0; i < attributes.length; i++) {
            actualEntrySet.add(Map.entry(i, (double) i));
        }
        assertEquals(expectedEntrySet.size(), actualEntrySet.size());
        assertTrue(expectedEntrySet.containsAll(actualEntrySet));
        assertTrue(actualEntrySet.containsAll(expectedEntrySet));
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.get(0), 0.0);
        assertEquals(2.0, denseInstance.get(1), 0.0);
        assertEquals(3.0, denseInstance.get(2), 0.0);
        assertNull(denseInstance.get(-1));
        assertNull(denseInstance.get(3));
    }

    @Test
    public void testIsEmpty() {
        DenseInstance denseInstance = new DenseInstance();
        assertTrue(denseInstance.isEmpty());
        double[] attributes = {1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> expectedKeys = new TreeSet<Integer>();
        expectedKeys.addAll(Arrays.asList(0, 1, 2));
        SortedSet<Integer> actualKeys = denseInstance.keySet();
        assertEquals(expectedKeys.size(), actualKeys.size());
        assertTrue(expectedKeys.containsAll(actualKeys));
        assertTrue(actualKeys.containsAll(expectedKeys));
    }

    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double newValue = 4.0;
        Integer key = 3;
        denseInstance.put(key, newValue);
        assertEquals(newValue, denseInstance.get(key), 0.0);
        double[] expectedAttributes = {1.0, 2.0, newValue};
        assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = {4.0, 5.0, 6.0};
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.putAll(denseInstance1.entrySet());
        map.putAll(denseInstance2.entrySet());
        DenseInstance denseInstance3 = new DenseInstance();
        denseInstance3.putAll(map);
        assertEquals(6, denseInstance3.noAttributes());
        assertArrayEquals(Arrays.concat(attributes1, attributes2), denseInstance3.attributes, 0.0);
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
        assertEquals(3, denseInstance.size());
        int newSize = 5;
        denseInstance = new DenseInstance(newSize);
        assertEquals(newSize, denseInstance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0);
        Collection<Double> actualValues = denseInstance.values();
        assertEquals(expectedValues.size(), actualValues.size());
        assertTrue(expectedValues.containsAll(actualValues));
        assertTrue(actualValues.containsAll(expectedValues));
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        double[] emptyAttributes = {};
        DenseInstance emptyDenseInstance = new DenseInstance(emptyAttributes);
        assertEquals(0, emptyDenseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        String expectedString = "{" + Arrays.toString(attributes) + ";Class1}";
        String actualString = denseInstance.toString();
        assertEquals(expectedString, actualString);

        Object classValue = "Class2";
        denseInstance.setClassValue(classValue);
        expectedString = "{" + Arrays.toString(attributes) + ";" + classValue + "}";
        actualString = denseInstance.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(1);
        double[] expectedAttributes = {1.0, 3.0, 4.0};
        assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        DenseInstance denseInstance3 = new DenseInstance(attributes1);
        assertEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
        assertNotEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        double[] attributes3 = {1.0, 2.1, 3.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        Object otherObject = new Object();
        assertTrue(denseInstance1.equals(denseInstance1));
        assertTrue(denseInstance1.equals(denseInstance2));
        assertFalse(denseInstance1.equals(denseInstance3));
        assertFalse(denseInstance1.equals(otherObject));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copiedInstance = denseInstance.copy();
        assertEquals(attributes.length, copiedInstance.noAttributes());
        assertArrayEquals(attributes, copiedInstance.attributes, 0.0);
        assertEquals(denseInstance.getClassValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<Integer>(Arrays.asList(1, 2));
        denseInstance.removeAttributes(indicesToRemove);
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, denseInstance.attributes, 0.0);
    }
}