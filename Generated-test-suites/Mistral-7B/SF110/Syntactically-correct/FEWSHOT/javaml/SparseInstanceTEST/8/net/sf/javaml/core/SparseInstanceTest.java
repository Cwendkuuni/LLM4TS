package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceWithNoArguments() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(10);
        assertEquals(10, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValueAndClassValue() {
        SparseInstance instance = new SparseInstance(10, "classValue");
        assertEquals(10, instance.noAttributes());
        assertEquals("classValue", instance.getClassValue().toString());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithInitialData() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceWithInitialDataAndDefaultValue() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData, 0.5);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.5, instance.get(3), 0.001);
    }

    @Test
    public void testSparseInstanceWithInitialDataAndClassValue() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData, "classValue");
        assertEquals(3, instance.noAttributes());
        assertEquals("classValue", instance.getClassValue().toString());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testValue() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        assertEquals(1.0, instance.value(0));
        assertEquals(2.0, instance.value(1));
        assertEquals(3.0, instance.value(2));
        assertEquals(0.0, instance.value(3), 0.001);
    }

    @Test
    public void testClear() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue().doubleValue(), 0.001);
    }

    @Test
    public void testGet() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
    }

    @Test
    public void testIsEmpty() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        Set<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testPutAll() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        map.put(Integer.valueOf(2), Double.valueOf(3.0));
        instance.putAll(map);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testRemove() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        Double removedValue = instance.remove(Integer.valueOf(1));
        assertEquals(2.0, removedValue.doubleValue(), 0.001);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(1));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullKey() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        instance.remove(null);
    }

    @Test
    public void testSize() {
        @Deprecated
        int size = new SparseInstance().size();
        assertEquals(0, size);
    }

    @Test
    public void testValues() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }

    @Test
    public void testNoAttributes() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        assertEquals(3, instance.noAttributes());
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        instance.removeAttribute(1);
        assertEquals(1, instance.noAttributes());
        instance.removeAttribute(2);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        instance.removeAttribute(1);
        assertEquals(1, instance.noAttributes());
        instance.removeAttribute(2);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        String toString = instance.toString();
        assertTrue(toString.contains("{"));
        assertTrue(toString.contains(";"));
        assertTrue(toString.contains("classValue"));
        assertTrue(toString.contains("[1.0, 2.0, 3.0]"));
    }

    @Test
    public void testHashCode() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(initialData);
        SparseInstance instance2 = new SparseInstance(initialData);
        SparseInstance instance3 = new SparseInstance();
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(initialData);
        SparseInstance instance2 = new SparseInstance(initialData);
        SparseInstance instance3 = new SparseInstance();
        SparseInstance instance4 = new SparseInstance(initialData);
        SparseInstance instance5 = new SparseInstance(new double[]{1.1, 2.1, 3.1});
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(instance4));
        assertFalse(instance1.equals(instance5));
    }

    @Test
    public void testCopy() {
        double[] initialData = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(initialData);
        SparseInstance copiedInstance = instance.copy();
        assertEquals(3, copiedInstance.noAttributes());
        assertEquals(1.0, copiedInstance.get(0), 0.001);
        assertEquals(2.0, copiedInstance.get(1), 0.001);
        assertEquals(3.0, copiedInstance.get(2), 0.001);
    }

    @Test
    public void testRemoveAttributes() {
        double[] initialData = {1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance instance = new SparseInstance(initialData);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(2);
        indices.add(4);
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(2));
        assertFalse(instance.containsKey(4));
    }
}