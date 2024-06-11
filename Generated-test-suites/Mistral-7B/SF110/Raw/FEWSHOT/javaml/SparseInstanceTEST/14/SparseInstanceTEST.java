package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceWithDefaultConstructor() {
        SparseInstance sparseInstance = new SparseInstance();
        assertEquals(-1, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance(0);
        assertEquals(0, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        SparseInstance sparseInstance = new SparseInstance(0, 0.0);
        assertEquals(0, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndClassValue() {
        SparseInstance sparseInstance = new SparseInstance(0, "ClassValue");
        assertEquals("ClassValue", sparseInstance.getClassValue());
        assertEquals(0, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesDefaultValueAndClassValue() {
        SparseInstance sparseInstance = new SparseInstance(0, 0.0, "ClassValue");
        assertEquals("ClassValue", sparseInstance.getClassValue());
        assertEquals(0, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(3, sparseInstance.noAttributes());
        assertFalse(sparseInstance.isEmpty());
        assertEquals(1.0, sparseInstance.value(0), 0.01);
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(3.0, sparseInstance.value(2), 0.01);
    }

    @Test
    public void testSparseInstanceWithDatavectorDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector, 0.0);
        assertEquals(3, sparseInstance.noAttributes());
        assertFalse(sparseInstance.isEmpty());
        assertEquals(1.0, sparseInstance.value(0), 0.01);
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(3.0, sparseInstance.value(2), 0.01);
    }

    @Test
    public void testSparseInstanceWithDatavectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector, "ClassValue");
        assertEquals("ClassValue", sparseInstance.getClassValue());
        assertEquals(3, sparseInstance.noAttributes());
        assertFalse(sparseInstance.isEmpty());
        assertEquals(1.0, sparseInstance.value(0), 0.01);
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(3.0, sparseInstance.value(2), 0.01);
    }

    @Test
    public void testSparseInstanceWithDatavectorDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector, 0.0, "ClassValue");
        assertEquals("ClassValue", sparseInstance.getClassValue());
        assertEquals(3, sparseInstance.noAttributes());
        assertFalse(sparseInstance.isEmpty());
        assertEquals(1.0, sparseInstance.value(0), 0.01);
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(3.0, sparseInstance.value(2), 0.01);
    }

    @Test
    public void testSparseInstanceWithDatavectorDefaultValueAndClassValueInitialization() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.initiate(datavector);
        assertEquals(3, sparseInstance.noAttributes());
        assertFalse(sparseInstance.isEmpty());
        assertEquals(1.0, sparseInstance.value(0), 0.01);
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(3.0, sparseInstance.value(2), 0.01);
    }

    @Test
    public void testSparseInstanceGet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(1.0, sparseInstance.get(0), 0.01);
        assertEquals(2.0, sparseInstance.get(1), 0.01);
        assertEquals(3.0, sparseInstance.get(2), 0.01);
        assertEquals(0.0, sparseInstance.get(-1), 0.01);
    }

    @Test
    public void testSparseInstancePut() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(1.0, sparseInstance.value(0), 0.01);
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(3.0, sparseInstance.value(2), 0.01);
        assertEquals(0.0, sparseInstance.value(-1), 0.01);
    }

    @Test
    public void testSparseInstancePutAll() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance1.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance1.put(Integer.valueOf(2), Double.valueOf(3.0));
        instance2.putAll(instance1.entrySet());
        assertEquals(1.0, instance2.value(0), 0.01);
        assertEquals(2.0, instance2.value(1), 0.01);
        assertEquals(3.0, instance2.value(2), 0.01);
        assertEquals(0.0, instance2.value(-1), 0.01);
    }

    @Test
    public void testSparseInstanceClear() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
        sparseInstance.clear();
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
        assertTrue(sparseInstance.containsKey(Integer.valueOf(1)));
        assertTrue(sparseInstance.containsKey(Integer.valueOf(2)));
        assertFalse(sparseInstance.containsKey(Integer.valueOf(3)));
        assertFalse(sparseInstance.containsKey(new Integer(4)));
    }

    @Test
    public void testSparseInstanceContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
        assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
        assertTrue(sparseInstance.containsValue(Double.valueOf(3.0)));
        assertFalse(sparseInstance.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testSparseInstanceEntrySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
        assertEquals(3, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            Integer key = entry.getKey();
            Double value = entry.getValue();
            assertEquals(key.intValue(), (int) entry.getKey());
            assertEquals(value.doubleValue(), entry.getValue().doubleValue());
        }
    }

    @Test
    public void testSparseInstanceNoAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(3, sparseInstance.noAttributes());
    }

    @Test
    public void testSparseInstanceRemoveAttribute() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        sparseInstance.removeAttribute(0);
        assertEquals(2, sparseInstance.noAttributes());
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(3.0, sparseInstance.value(2), 0.01);
        assertEquals(0.0, sparseInstance.value(0), 0.01);
    }

    @Test
    public void testSparseInstanceToString() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        String toString = sparseInstance.toString();
        assertTrue(toString.contains("{"));
        assertTrue(toString.contains(";"));
        assertTrue(toString.contains("}"));
    }

    @Test
    public void testSparseInstanceHashCode() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector);
        SparseInstance instance2 = new SparseInstance(datavector);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testSparseInstanceEquals() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector);
        SparseInstance instance2 = new SparseInstance(datavector);
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals("Test"));
    }

    @Test
    public void testSparseInstanceCopy() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector);
        SparseInstance instance2 = instance1.copy();
        assertEquals(instance1.noAttributes(), instance2.noAttributes());
        assertEquals(instance1.getClassValue(), instance2.getClassValue());
        for (int i = 0; i < instance1.noAttributes(); ++i) {
            assertEquals(instance1.value(i), instance2.value(i), 0.01);
        }
    }

    @Test
    public void testSparseInstanceRemoveAttributes() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<>();
        indices.add(2);
        indices.add(4);
        sparseInstance.removeAttributes(indices);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(1.0, sparseInstance.value(0), 0.01);
        assertEquals(2.0, sparseInstance.value(1), 0.01);
        assertEquals(5.0, sparseInstance.value(2), 0.01);
    }
}