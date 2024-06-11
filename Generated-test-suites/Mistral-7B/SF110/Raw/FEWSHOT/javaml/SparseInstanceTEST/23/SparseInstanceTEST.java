package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testConstructorWithNoArguments() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructorWithNumberOfAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructorWithDefaultValueAndNumberOfAttributes() {
        SparseInstance instance = new SparseInstance(5, 0.1);
        assertEquals(5, instance.noAttributes());
        assertEquals(0.1, instance.getDefaultValue(), 0.001);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructorWithDefaultValueClassValueAndNumberOfAttributes() {
        SparseInstance instance = new SparseInstance(5, 0.1, "StringClass");
        assertEquals(5, instance.noAttributes());
        assertEquals(0.1, instance.getDefaultValue(), 0.001);
        assertEquals("StringClass", instance.getClassValue());
    }

    @Test
    public void testConstructorWithDataAndDefaultValue() {
        double[] data = new double[]{1.1, 2.2, 3.3};
        SparseInstance instance = new SparseInstance(data, 0.1);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.1, instance.getDefaultValue(), 0.001);
        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.put(0, 1.1);
        expectedData.put(1, 2.2);
        expectedData.put(2, 3.3);
        assertEquals(expectedData, instance.getData());
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructorWithDataDefaultValueAndClassValue() {
        double[] data = new double[]{1.1, 2.2, 3.3};
        SparseInstance instance = new SparseInstance(data, 0.1, "StringClass");
        assertEquals(3, instance.noAttributes());
        assertEquals(0.1, instance.getDefaultValue(), 0.001);
        assertEquals("StringClass", instance.getClassValue());
        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.put(0, 1.1);
        expectedData.put(1, 2.2);
        expectedData.put(2, 3.3);
        assertEquals(expectedData, instance.getData());
    }

    @Test
    public void testInitiateMethod() {
        double[] data = new double[]{1.1, 0.0, 2.2, 3.3};
        SparseInstance instance = new SparseInstance(data, 0.1);
        instance.initiate(data);
        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.put(0, 1.1);
        expectedData.put(2, 2.2);
        expectedData.put(3, 3.3);
        assertEquals(expectedData, instance.getData());
    }

    @Test
    public void testSizeMethod() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());

        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        assertEquals(3, instance.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClearMethod() {
        Map<Integer, Double> data = new HashMap<>();
        SparseInstance instance = new SparseInstance(data);
        instance.clear();
    }

    @Test
    public void testContainsKeyMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        SparseInstance instance = new SparseInstance(data);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testContainsValueMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        SparseInstance instance = new SparseInstance(data);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySetMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        SparseInstance instance = new SparseInstance(data);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        List<Map.Entry<Integer, Double>> expectedEntries = new ArrayList<>(Arrays.asList(Arrays.asList(0, 1.0), Arrays.asList(1, 2.0)));
        assertEquals(expectedEntries, entries);
    }

    @Test
    public void testGetMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        SparseInstance instance = new SparseInstance(data);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(0.1, instance.get(-1), 0.001);
    }

    @Test
    public void testIsEmptyMethod() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());

        instance.put(0, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        SparseInstance instance = new SparseInstance(data);
        Set<Integer> keys = instance.keySet();
        List<Integer> expectedKeys = new ArrayList<>(Arrays.asList(0, 1));
        assertEquals(expectedKeys, keys);
    }

    @Test
    public void testPutMethod() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);

        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.put(0, 1.0);
        expectedData.put(1, 2.0);
        expectedData.put(2, 3.0);
        assertEquals(expectedData, instance.getData());
    }

    @Test
    public void testPutAllMethod() {
        Map<Integer, Double> data1 = new HashMap<>();
        data1.put(0, 1.0);
        data1.put(1, 2.0);

        Map<Integer, Double> data2 = new HashMap<>();
        data2.put(2, 3.0);
        data2.put(3, 4.0);

        SparseInstance instance = new SparseInstance();
        instance.putAll(data1);
        instance.putAll(data2);

        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.put(0, 1.0);
        expectedData.put(1, 2.0);
        expectedData.put(2, 3.0);
        expectedData.put(3, 4.0);
        assertEquals(expectedData, instance.getData());
    }

    @Test
    public void testRemoveMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        SparseInstance instance = new SparseInstance(data);

        Double removedValue = instance.remove(0);
        assertTrue(removedValue != null);
        assertEquals(1.0, removedValue.doubleValue(), 0.001);

        instance.remove(1);
        assertEquals(0, instance.size());
    }

    @Test
    public void testNoAttributesMethod() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());

        instance.put(0, 1.0);
        instance.put(1, 2.0);
        assertEquals(2, instance.noAttributes());

        instance.clear();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttributeMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        data.put(2, 3.0);

        SparseInstance instance = new SparseInstance(data);
        instance.removeAttribute(1);

        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.put(0, 1.0);
        expectedData.put(2, 3.0);
        assertEquals(expectedData, instance.getData());
    }

    @Test
    public void testToStringMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        data.put(2, 3.0);
        SparseInstance instance = new SparseInstance(data);

        String expectedString = "{" + data.toString() + ";" + instance.getClassValue() + "}";
        assertEquals(expectedString, instance.toString());
    }

    @Test
    public void testHashCodeMethod() {
        Map<Integer, Double> data1 = new HashMap<>();
        data1.put(0, 1.0);
        data1.put(1, 2.0);

        Map<Integer, Double> data2 = new HashMap<>();
        data2.put(0, 1.0);
        data2.put(1, 2.0);

        SparseInstance instance1 = new SparseInstance(data1);
        SparseInstance instance2 = new SparseInstance(data2);

        assertEquals(instance1.hashCode(), instance2.hashCode());

        Map<Integer, Double> data3 = new HashMap<>();
        data3.put(0, 1.0);
        data3.put(2, 2.0);

        SparseInstance instance3 = new SparseInstance(data3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        Map<Integer, Double> data1 = new HashMap<>();
        data1.put(0, 1.0);
        data1.put(1, 2.0);

        Map<Integer, Double> data2 = new HashMap<>();
        data2.put(0, 1.0);
        data2.put(1, 2.0);

        SparseInstance instance1 = new SparseInstance(data1);
        SparseInstance instance2 = new SparseInstance(data2);

        SparseInstance instance3 = new SparseInstance();
        SparseInstance instance4 = new SparseInstance(instance1.getData());

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertTrue(instance1.equals(instance4));
    }

    @Test
    public void testCopyMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        SparseInstance instance = new SparseInstance(data);

        SparseInstance copiedInstance = instance.copy();

        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.putAll(instance.getData());
        assertEquals(expectedData, copiedInstance.getData());
        assertEquals(instance.getClassValue(), copiedInstance.getClassValue());
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
    }

    @Test
    public void testRemoveAttributesMethod() {
        Map<Integer, Double> data = new HashMap<>();
        data.put(0, 1.0);
        data.put(1, 2.0);
        data.put(2, 3.0);

        SparseInstance instance = new SparseInstance(data);

        Set<Integer> indices = new HashSet<Integer>(Arrays.asList(1, 2));
        instance.removeAttributes(indices);

        Map<Integer, Double> expectedData = new HashMap<>();
        expectedData.put(0, 1.0);
        assertEquals(expectedData, instance.getData());
    }
}