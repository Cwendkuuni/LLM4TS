package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceWithDefaultConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(0);
        assertEquals(0, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(0, 0.0);
        assertEquals(0, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValueAndClassValue() {
        SparseInstance instance = new SparseInstance(0, 0.0, "ClassValue");
        assertEquals("ClassValue", instance.getClassValue().toString());
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        assertEquals("ClassValue", instance.getClassValue().toString());
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndDefaultValueAndClassValueAndInitiate() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        assertEquals("ClassValue", instance.getClassValue().toString());
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);

        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertEquals(0.0, instance.value(-1), 0.001);
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue().doubleValue(), 0.001);
    }

    @Test
    public void testGet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(-1), 0.001);
    }

    @Test
    public void testIsEmpty() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
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
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance1.put(Integer.valueOf(2), Double.valueOf(3.0));

        SparseInstance instance2 = new SparseInstance();
        instance2.put(Integer.valueOf(3), Double.valueOf(4.0));
        instance2.put(Integer.valueOf(4), Double.valueOf(5.0));

        instance1.putAll(instance2.entrySet());
        assertEquals(5, instance1.noAttributes());
        assertFalse(instance1.isEmpty());
        assertEquals(1.0, instance1.get(0), 0.001);
        assertEquals(2.0, instance1.get(1), 0.001);
        assertEquals(3.0, instance1.get(2), 0.001);
        assertEquals(4.0, instance1.get(3), 0.001);
        assertEquals(5.0, instance1.get(4), 0.001);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));

        Double removedValue = instance.remove(Integer.valueOf(1));
        assertEquals(2.0, removedValue.doubleValue(), 0.001);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertFalse(instance.containsKey(1));
    }

    @Test
    public void testSize() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
        assertEquals(3, instance.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeDeprecated() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        instance.size();
    }

    @Test
    public void testValues() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }

    @Test
    public void testNoAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));

        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(1), 0.001);
    }

    @Test
    public void testToString() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        String expected = "{" + datavector.toString() + ";ClassValue}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector, 0.0, "ClassValue");
        instance1.initiate(datavector);
        SparseInstance instance2 = new SparseInstance(datavector, 0.0, "ClassValue");
        instance2.initiate(datavector);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector, 0.0, "ClassValue");
        instance1.initiate(datavector);
        SparseInstance instance2 = new SparseInstance(datavector, 0.0, "ClassValue");
        instance2.initiate(datavector);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        SparseInstance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        assertEquals(instance.getClassValue().toString(), copiedInstance.getClassValue().toString());
        for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
            assertEquals(entry.getKey().intValue(), copiedInstance.get(entry.getKey().intValue()).intValue());
            assertEquals(entry.getValue().doubleValue(), copiedInstance.get(entry.getKey().intValue()).doubleValue(), 0.001);
        }
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        instance.initiate(datavector);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }
}