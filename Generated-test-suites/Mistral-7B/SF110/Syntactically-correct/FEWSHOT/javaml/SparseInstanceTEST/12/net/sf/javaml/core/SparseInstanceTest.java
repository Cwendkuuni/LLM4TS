package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceEmpty() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertNull(instance.get(0));
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsValue(0.0));
        assertEquals(0, instance.size());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(10);
        assertEquals(10, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertNull(instance.get(0));
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsValue(0.0));
        assertEquals(0, instance.size());
    }

    @Test
    public void testSparseInstanceWithData() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0), 0.001);
        assertTrue(instance.containsKey(0));
        assertFalse(instance.containsValue(1.0));
        assertTrue(instance.containsValue(0.5));
        assertEquals(3, instance.size());
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        instance.setNoAttributes(7);
        assertEquals(7, instance.noAttributes());
    }

    @Test
    public void testValue() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(0.5, instance.value(3));
        assertEquals(0.5, instance.value(-1));
    }

    @Test
    public void testClear() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        instance.clear();
        assertEquals(-1, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertNull(instance.get(0));
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsValue(0.0));
        assertEquals(0, instance.size());
    }

    @Test
    public void testContainsKey() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(0.5));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        int count = 0;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(count++, entry.getKey());
            assertEquals(instance.value(count), entry.getValue());
        }
    }

    @Test
    public void testGet() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(0.5, instance.get(3));
        assertEquals(0.5, instance.get(-1));
    }

    @Test
    public void testIsEmpty() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        TreeSet<Integer> keySet = instance.keySet();
        int count = 0;
        for (Integer key : keySet) {
            assertEquals(count++, key);
        }
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1.0, instance.get(0), 0.001);
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        assertEquals(2.0, instance.get(1), 0.001);
        assertNull(instance.get(2));
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
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
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        Double removedValue = instance.remove(1);
        assertEquals(2.0, removedValue.doubleValue());
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeDeprecated() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        instance.size();
    }

    @Test
    public void testValues() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        Collection<Double> values = instance.values();
        int count = 0;
        for (Double value : values) {
            assertEquals(instance.value(count), value.doubleValue());
            ++count;
        }
    }

    @Test
    public void testNoAttributes() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        assertEquals(3, instance.noAttributes());
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        instance.removeAttribute(2);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(0));
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
    }

    @Test
    public void testToString() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        String expected = "{1.0=1.0, 2.0=2.0, 3.0=3.0;Class=Object}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(data, 0.5);
        SparseInstance instance2 = new SparseInstance(data, 0.5);
        SparseInstance instance3 = new SparseInstance(data, 0.5);
        instance3.removeAttribute(0);
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] data1 = new double[]{1.0, 2.0, 3.0};
        double[] data2 = new double[]{1.0, 2.0, 3.0};
        double[] data3 = new double[]{1.0, 2.1, 3.0};
        SparseInstance instance1 = new SparseInstance(data1, 0.5);
        SparseInstance instance2 = new SparseInstance(data2, 0.5);
        SparseInstance instance3 = new SparseInstance(data1, 0.5);
        SparseInstance instance4 = new SparseInstance(data3, 0.5);
        SparseInstance instance5 = new SparseInstance();
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance3));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance4));
        assertFalse(instance1.equals(instance5));
        assertFalse(instance1.equals(null));
    }

    @Test
    public void testCopy() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        SparseInstance copy = instance.copy();
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.defaultValue, copy.defaultValue);
        assertEquals(instance.data.keySet(), copy.data.keySet());
        assertEquals(instance.data.values(), copy.data.values());
    }

    @Test
    public void testRemoveAttributes() {
        double[] data = new double[]{1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(data, 0.5);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(2));
    }
}