package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SparseInstanceTest {
    private SparseInstance instance;

    @Before
    public void setup() {
        instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
    }

    @Test
    public void testSetNoAttributes() {
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance emptyInstance = new SparseInstance();
        assertEquals(0, emptyInstance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance emptyInstance = new SparseInstance(0.0);
        assertEquals(0, emptyInstance.noAttributes());
    }

    @Test
    public void testConstructorWithNoAttributesAndClassValue() {
        SparseInstance emptyInstance = new SparseInstance("SomeClass");
        assertEquals("SomeClass", instance.classValue());
        assertEquals(0, emptyInstance.noAttributes());
    }

    @Test
    public void testConstructorWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instanceFromDatavector = new SparseInstance(datavector);
        assertEquals(3, instanceFromDatavector.noAttributes());
        assertEquals(datavector, instanceFromDatavector.toArray());
    }

    @Test
    public void testConstructorWithDatavectorAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instanceFromDatavector = new SparseInstance(datavector, 0.0);
        assertEquals(3, instanceFromDatavector.noAttributes());
        assertEquals(datavector, instanceFromDatavector.toArray());
    }

    @Test
    public void testConstructorWithDatavectorAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instanceFromDatavector = new SparseInstance(datavector, "SomeClass");
        assertEquals("SomeClass", instanceFromDatavector.classValue());
        assertEquals(3, instanceFromDatavector.noAttributes());
        assertEquals(datavector, instanceFromDatavector.toArray());
    }

    @Test
    public void testConstructorWithDatavectorAndDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instanceFromDatavector = new SparseInstance(datavector, 0.0, "SomeClass");
        assertEquals("SomeClass", instanceFromDatavector.classValue());
        assertEquals(3, instanceFromDatavector.noAttributes());
        assertEquals(datavector, instanceFromDatavector.toArray());
    }

    @Test
    public void testValue() {
        assertEquals(1.0, instance.value(0));
        assertEquals(2.0, instance.value(1));
        assertEquals(3.0, instance.value(2));
    }

    @Test
    public void testClear() {
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        assertTrue(instance.containsKey(Integer.valueOf(0)));
        assertTrue(instance.containsKey(Integer.valueOf(1)));
        assertTrue(instance.containsKey(Integer.valueOf(2)));
        assertFalse(instance.containsKey(Integer.valueOf(3)));
    }

    @Test
    public void testContainsValue() {
        assertTrue(instance.containsValue(Double.valueOf(1.0)));
        assertTrue(instance.containsValue(Double.valueOf(2.0)));
        assertTrue(instance.containsValue(Double.valueOf(3.0)));
        assertFalse(instance.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(3, entries.size());
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entries);
        assertEquals(1.0, entryList.get(0).getValue().doubleValue());
        assertEquals(0, entryList.get(0).getKey().intValue());
        assertEquals(2.0, entryList.get(1).getValue().doubleValue());
        assertEquals(1, entryList.get(1).getKey().intValue());
        assertEquals(3.0, entryList.get(2).getValue().doubleValue());
        assertEquals(2, entryList.get(2).getKey().intValue());
    }

    @Test
    public void testGet() {
        assertEquals(1.0, instance.get(Integer.valueOf(0)));
        assertEquals(2.0, instance.get(Integer.valueOf(1)));
        assertEquals(3.0, instance.get(Integer.valueOf(2)));
        assertEquals(0.0, instance.get(Integer.valueOf(3)));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        Set<Integer> keys = instance.keySet();
        assertEquals(3, keys.size());
        List<Integer> keyList = new ArrayList<>(keys);
        assertEquals(0, keyList.get(0).intValue());
        assertEquals(1, keyList.get(1).intValue());
        assertEquals(2, keyList.get(2).intValue());
    }

    @Test
    public void testPut() {
        instance.put(Integer.valueOf(3), Double.valueOf(4.0));
        assertEquals(4, instance.noAttributes());
        assertEquals(4.0, instance.get(Integer.valueOf(3)));
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(3), Double.valueOf(4.0));
        map.put(Integer.valueOf(4), Double.valueOf(5.0));
        instance.putAll(map);
        assertEquals(6, instance.noAttributes());
        assertEquals(5.0, instance.get(Integer.valueOf(4)));
    }

    @Test
    public void testRemove() {
        Double removedValue = instance.remove(Integer.valueOf(1));
        assertEquals(2.0, removedValue.doubleValue());
        assertEquals(2, instance.noAttributes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeDeprecated() {
        int size = instance.size();
    }

    @Test
    public void testValues() {
        List<Double> values = new ArrayList<>(instance.values());
        assertEquals(3, values.size());
        assertEquals(1.0, values.get(0).doubleValue());
        assertEquals(2.0, values.get(1).doubleValue());
        assertEquals(3.0, values.get(2).doubleValue());
    }

    @Test
    public void testNoAttributes() {
        assertEquals(-1, instance.noAttributes());
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        assertEquals(2.0, instance.value(0));
    }

    @Test
    public void testToString() {
        String toString = instance.toString();
        assertTrue(toString.contains("{"));
        assertTrue(toString.contains(";"));
        assertTrue(toString.contains(" SomeClass"));
        assertTrue(toString.contains("="));
    }

    @Test
    public void testHashCode() {
        int hashcode = instance.hashCode();
        SparseInstance copy = instance.copy();
        assertEquals(hashcode, copy.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance copy = instance.copy();
        assertSame(instance, copy);
        SparseInstance other = new SparseInstance();
        assertFalse(instance.equals(other));
    }

    @Test
    public void testCopy() {
        SparseInstance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        assertEquals(instance.data, copiedInstance.data);
        assertEquals(instance.defaultValue, copiedInstance.defaultValue);
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indices = new TreeSet<>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertNull(instance.value(1));
        assertNull(instance.value(2));
    }
}