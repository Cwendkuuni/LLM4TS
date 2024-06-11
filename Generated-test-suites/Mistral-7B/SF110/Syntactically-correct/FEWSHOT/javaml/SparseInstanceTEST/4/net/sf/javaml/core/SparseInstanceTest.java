package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertNull(instance.get(1));

        instance = new SparseInstance(3);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.get(0));
        assertNull(instance.get(1));
        assertNull(instance.get(2));

        instance = new SparseInstance(3, 0.5);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.get(0));
        assertNull(instance.get(1));
        assertNull(instance.get(2));
        assertEquals(0.5, instance.defaultValue, 0.001);

        instance = new SparseInstance(3, "classValue");
        assertEquals("classValue", instance.getClassValue());
        assertEquals(3, instance.noAttributes());
        assertNull(instance.get(0));
        assertNull(instance.get(1));
        assertNull(instance.get(2));

        instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.defaultValue, 0.001);

        instance = new SparseInstance(new double[]{1.0, 2.0, 3.0}, 0.5);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.5, instance.defaultValue, 0.001);

        instance = new SparseInstance(new double[]{1.0, 2.0, 3.0}, "classValue");
        assertEquals("classValue", instance.getClassValue());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.defaultValue, 0.001);

        instance = new SparseInstance(new double[]{1.0, 2.0, 3.0}, 0.5, "classValue");
        assertEquals("classValue", instance.getClassValue());
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        assertEquals(0.5, instance.defaultValue, 0.001);
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.clear();
        assertNull(instance.get(1));
        assertNull(instance.get(2));
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        assertFalse(instance.containsKey(1));

        instance.put(1, 1.0);
        assertTrue(instance.containsKey(1));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        assertFalse(instance.containsValue(1.0));

        instance.put(1, 1.0);
        assertTrue(instance.containsValue(1.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertTrue(entries.isEmpty());

        instance.put(1, 1.0);
        entries = instance.entrySet();
        assertEquals(1, entries.size());
        Map.Entry<Integer, Double> entry = entries.iterator().next();
        assertEquals(1, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue().doubleValue(), 0.001);
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0.0, instance.get(1), 0.001);

        instance.put(1, 1.0);
        assertEquals(1.0, instance.get(1), 0.001);
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());

        instance.put(1, 1.0);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        Set<Integer> keys = instance.keySet();
        assertTrue(keys.isEmpty());

        instance.put(1, 1.0);
        keys = instance.keySet();
        assertEquals(1, keys.size());
        assertEquals(1, keys.iterator().next().intValue());
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        assertEquals(1.0, instance.get(1), 0.001);

        instance.put(2, 2.0);
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(2.0, instance.get(2), 0.001);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(1, 1.0);
        map.put(2, 2.0);

        instance.putAll(map);
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(2.0, instance.get(2), 0.001);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        Double value = instance.remove(1);
        assertEquals(1.0, value.doubleValue(), 0.001);
        assertNull(instance.get(1));
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.size());

        instance.put(1, 1.0);
        assertEquals(1, instance.size());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        Collection<Double> values = instance.values();
        assertTrue(values.isEmpty());

        instance.put(1, 1.0);
        values = instance.values();
        assertEquals(1, values.size());
        assertEquals(1.0, values.iterator().next().doubleValue(), 0.001);
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());

        instance.put(1, 1.0);
        assertEquals(1, instance.noAttributes());

        instance.put(2, 2.0);
        assertEquals(2, instance.noAttributes());

        instance.clear();
        assertEquals(-1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.put(3, 3.0);

        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertNull(instance.get(1));

        instance.removeAttribute(2);
        assertEquals(1, instance.noAttributes());
        assertNull(instance.get(2));

        instance.removeAttribute(3);
        assertEquals(0, instance.noAttributes());
        assertNull(instance.get(3));
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        assertEquals("{}", instance.toString());

        instance.put(1, 1.0);
        assertEquals("{1=1.0}", instance.toString());

        instance.put(2, 2.0);
        assertEquals("{1=1.0, 2=2.0}", instance.toString());

        instance.put(3, 3.0);
        assertEquals("{1=1.0, 2=2.0, 3=3.0}", instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance1.put(1, 1.0);
        instance2.put(1, 1.0);

        assertEquals(instance1.hashCode(), instance2.hashCode());

        instance1.put(2, 2.0);
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        instance1.put(1, 1.0);
        instance2.put(1, 1.0);

        assertTrue(instance1.equals(instance2));

        instance2.put(2, 2.0);
        assertFalse(instance1.equals(instance2));

        instance1.put(2, 2.0);
        assertTrue(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);

        SparseInstance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        assertEquals(instance.defaultValue, copiedInstance.defaultValue);
        assertEquals(instance.getClassValue(), copiedInstance.getClassValue());

        for (Map.Entry<Integer, Double> entry : instance.entrySet()) {
            Map.Entry<Integer, Double> copyEntry = copiedInstance.get(entry.getKey());
            assertEquals(entry.getValue().doubleValue(), copyEntry.getValue().doubleValue(), 0.001);
        }
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.put(3, 3.0);

        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);

        assertEquals(2, instance.noAttributes());
        assertNull(instance.get(1));
        assertNull(instance.get(3));
    }
}