package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testDefaultConstructor() {
        DenseInstance instance = new DenseInstance();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);
        assertEquals(size, instance.noAttributes());
        assertArrayEquals(new double[size], instance.attributes, 0.0);
    }

    @Test
    public void testGetValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Double value = 2.0;
        assertTrue(instance.containsValue(value));
        Double notValue = 4.0;
        assertFalse(instance.containsValue(notValue));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, entry.getKey().intValue());
            assertEquals(value, instance.value(key), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance();
        assertTrue(instance.isEmpty());
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        for (int key : keySet) {
            assertTrue(instance.containsKey(key));
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[]{1.0, 2.0};
        DenseInstance instance = new DenseInstance(attributes);
        Double newValue = 3.0;
        int key = 3;
        instance.put(key, newValue);
        assertEquals(4, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, newValue}, instance.attributes, 0.0);
    }

    @Test
    public void testPutAll() {
        double[] baseAttributes = new double[]{1.0, 2.0, 3.0};
        double[] newAttributes = new double[]{4.0, 5.0, 6.0};
        DenseInstance instance = new DenseInstance(baseAttributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(4, 4.0);
        map.put(5, 5.0);
        map.put(6, 6.0);
        instance.putAll(map);
        assertEquals(6, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0}, instance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(1);
    }

    @Test
    public void testSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.size());
        DenseInstance instance2 = new DenseInstance();
        assertEquals(0, instance2.size());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        for (Double value : values) {
            assertEquals(value, instance.attributes[Collections.indexOf(instance.attributes, value)], 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        DenseInstance instance2 = new DenseInstance();
        assertEquals(0, instance2.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        String expected = "{" + Arrays.toString(attributes) + ";" + instance.classValue() + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        double[] attributes3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertTrue(instance1.equals(instance2));

        double[] attributes3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertFalse(instance1.equals(instance3));

        DenseInstance instance4 = instance1.copy();
        assertTrue(instance1.equals(instance4));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copiedInstance = instance.copy();
        assertEquals(instance, copiedInstance);
        assertNotSame(instance, copiedInstance);
        assertArrayEquals(attributes, copiedInstance.attributes, 0.0);
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttributes(indicesToRemove);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0, 5.0}, instance.attributes, 0.0);
    }
}