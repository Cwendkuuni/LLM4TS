package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(attributes, instance.attributes);
    }

    @Test
    public void testConstructorWithDoubleArrayAndObject() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "ClassA";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(attributes, instance.attributes);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance instance = new DenseInstance();
        assertTrue(instance.isEmpty());
        assertNull(instance.classValue());
        assertArrayEquals(new double[0], instance.attributes);
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);
        double[] expectedAttributes = new double[size];
        assertArrayEquals(expectedAttributes, instance.attributes);
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
        assertArrayEquals(new double[0], instance.attributes);
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
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        double value = 2.0;
        assertTrue(instance.containsValue(value));

        double valueNotPresent = 4.0;
        assertFalse(instance.containsValue(valueNotPresent));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Set<Map.Entry<Integer, Double>> expectedEntrySet = new HashSet<>();
        for (int i = 0; i < attributes.length; ++i) {
            expectedEntrySet.add(Map.entry(i, attributes[i]));
        }

        Set<Map.Entry<Integer, Double>> actualEntrySet = instance.entrySet();
        assertEquals(expectedEntrySet, actualEntrySet);
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.isEmpty());

        double[] nonEmptyAttributes = {1.0};
        DenseInstance nonEmptyInstance = new DenseInstance(nonEmptyAttributes);
        assertFalse(nonEmptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        SortedSet<Integer> expectedKeySet = new TreeSet<>();
        for (int i = 0; i < attributes.length; ++i) {
            expectedKeySet.add(i);
        }

        SortedSet<Integer> actualKeySet = instance.keySet();
        assertEquals(expectedKeySet, actualKeySet);
    }

    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        double newValue = 4.0;
        int keyToReplace = 1;
        Double oldValue = instance.put(keyToReplace, newValue);

        assertEquals(newValue, instance.attributes[keyToReplace], 0.0);
        assertEquals(2.0, oldValue, 0.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Map<Integer, Double> mapToMerge = new HashMap<>();
        mapToMerge.put(0, 5.0);
        mapToMerge.put(3, 7.0);

        instance.putAll(mapToMerge);

        assertEquals(5.0, instance.attributes[0], 0.0);
        assertEquals(7.0, instance.attributes[3], 0.0);
        assertArrayEquals(Arrays.copyOfRange(attributes, 1, attributes.length), Arrays.stream(instance.attributes).skip(1).toArray());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
        fail("Remove operation is not supported.");
    }

    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> expectedValues = Arrays.asList(1.0, 2.0, 3.0);
        assertEquals(expectedValues, instance.values());
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = {};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(0, instance.noAttributes());

        double[] nonEmptyAttributes = {1.0};
        DenseInstance nonEmptyInstance = new DenseInstance(nonEmptyAttributes);
        assertEquals(1, nonEmptyInstance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        String expectedString = "{" + Arrays.toString(attributes) + ";ClassA}";
        String actualString = instance.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        int indexToRemove = 1;
        instance.removeAttribute(indexToRemove);

        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);

        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);

        assertTrue(instance1.equals(instance2));

        DenseInstance instance3 = new DenseInstance(attributes1);
        assertTrue(instance1.equals(instance3));

        double[] attributes3 = {1.0, 2.1, 3.0};
        DenseInstance instance4 = new DenseInstance(attributes3);
        assertFalse(instance1.equals(instance4));

        Object nonDenseInstance = new Object();
        assertFalse(instance1.equals(nonDenseInstance));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        DenseInstance copy = instance.copy();
        assertEquals(attributes, copy.attributes);
        assertEquals(instance.getClassValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes);

        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
        instance.removeAttributes(indicesToRemove);

        double[] expectedAttributes = {1.0, 5.0};
        assertArrayEquals(expectedAttributes, instance.attributes);
    }
}