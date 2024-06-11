package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {
    @Test
    public void testSparseInstanceEmptyConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(0);
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(1, 0.0);
        assertEquals(1, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        SparseInstance instance = new SparseInstance(1, 0.0, "classValue");
        assertEquals("classValue", instance.getClassValue());
        assertEquals(1, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.2, instance.get(1), 0.001);
        assertEquals(0.3, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3));
    }

    @Test
    public void testSparseInstanceWithDefaultValueAndClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector, 0.5, "classValue");
        assertEquals("classValue", instance.getClassValue());
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.2, instance.get(1), 0.001);
        assertEquals(0.3, instance.get(2), 0.001);
        assertEquals(0.5, instance.get(3));
    }

    @Test
    public void testSparseInstanceSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSparseInstanceNegativeNumberOfAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(-1);
    }

    @Test
    public void testSparseInstanceClear() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceContainsKey() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testSparseInstanceContainsValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsValue(0.1));
        assertTrue(instance.containsValue(0.2));
        assertTrue(instance.containsValue(0.3));
        assertFalse(instance.containsValue(0.4));
    }

    @Test
    public void testSparseInstanceEntrySet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(3, entries.size());
        Map.Entry<Integer, Double> entry = entries.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(0.1, entry.getValue().doubleValue(), 0.001);
    }

    @Test
    public void testSparseInstanceGet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.2, instance.get(1), 0.001);
        assertEquals(0.3, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3));
    }

    @Test
    public void testSparseInstanceIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(0, 0.1);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceKeySet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> keys = instance.keySet();
        assertEquals(3, keys.size());
        assertTrue(keys.contains(0));
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
        assertFalse(keys.contains(3));
    }

    @Test
    public void testSparseInstancePut() {
        SparseInstance instance = new SparseInstance();
        instance.put(0, 0.1);
        instance.put(1, 0.2);
        instance.put(2, 0.3);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.2, instance.get(1), 0.001);
        assertEquals(0.3, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3));
    }

    @Test
    public void testSparseInstancePutAll() {
        double[] datavector1 = {0.1, 0.2, 0.3};
        double[] datavector2 = {0.4, 0.5, 0.6};
        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance();
        instance2.putAll(instance1.data);
        assertEquals(3, instance2.noAttributes());
        assertFalse(instance2.isEmpty());
        assertEquals(0.1, instance2.get(0), 0.001);
        assertEquals(0.2, instance2.get(1), 0.001);
        assertEquals(0.3, instance2.get(2), 0.001);
        assertEquals(0.0, instance2.get(3));
    }

    @Test
    public void testSparseInstanceRemove() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Double value = instance.remove(1);
        assertNotNull(value);
        assertEquals(0.2, value.doubleValue(), 0.001);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.3, instance.get(1), 0.001);
    }

    @Test
    public void testSparseInstanceSize() {
        @Deprecated
        int size = new SparseInstance(new double[0]).size();
        assertEquals(0, size);
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        int size1 = instance.size();
        assertEquals(3, size1);
        instance.clear();
        int size2 = instance.size();
        assertEquals(0, size2);
    }

    @Test
    public void testSparseInstanceNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testSparseInstanceRemoveAttribute() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.3, instance.get(1), 0.001);
    }

    @Test
    public void testSparseInstanceToString() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        String toString = instance.toString();
        assertTrue(toString.contains("{"));
        assertTrue(toString.contains(";"));
        assertTrue(toString.contains("classValue"));
        assertTrue(toString.contains("[0.1, 0.2, 0.3]"));
    }

    @Test
    public void testSparseInstanceHashCode() {
        double[] datavector1 = {0.1, 0.2, 0.3};
        double[] datavector2 = {0.1, 0.2, 0.3};
        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance(datavector2);
        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testSparseInstanceEquals() {
        double[] datavector1 = {0.1, 0.2, 0.3};
        double[] datavector2 = {0.1, 0.2, 0.3};
        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance(datavector2);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testSparseInstanceCopy() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        SparseInstance copy = instance.copy();
        assertEquals(instance.hashCode(), copy.hashCode());
        assertEquals(instance.noAttributes(), copy.noAttributes());
        for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
            assertEquals(entry.getKey(), copy.get(entry.getKey()));
            assertEquals(entry.getValue(), copy.get(entry.getKey()), 0.001);
        }
    }

    @Test
    public void testSparseInstanceRemoveAttributes() {
        double[] datavector = {0.1, 0.2, 0.3, 0.4, 0.5};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
        assertEquals(0.2, instance.get(0), 0.001);
        assertEquals(0.4, instance.get(2), 0.001);
    }
}