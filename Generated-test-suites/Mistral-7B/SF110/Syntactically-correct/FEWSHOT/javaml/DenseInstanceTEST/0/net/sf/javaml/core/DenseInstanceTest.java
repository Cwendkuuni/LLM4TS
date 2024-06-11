package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "label";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
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
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.5));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        int index = 0;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(index++, entry.getKey());
            assertEquals(attributes[index], entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(-1));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());

        DenseInstance emptyInstance = new DenseInstance();
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = instance.keySet();
        int index = 0;
        for (Integer key : keySet) {
            assertEquals(index++, key);
        }
    }

    @Test
    public void testPut() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Double newValue = 4.0;
        Integer newKey = 3;
        Double oldValue = instance.put(newKey, newValue);
        assertEquals(newValue, instance.get(newKey), 0.0);
        assertNull(oldValue);
    }

    @Test
    public void testPutAll() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 4.0);
        map.put(2, 5.0);
        instance.putAll(map);
        Double value1 = instance.get(1);
        Double value2 = instance.get(2);
        assertEquals(4.0, value1, 0.0);
        assertEquals(5.0, value2, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> values = instance.values();
        int index = 0;
        for (Double value : values) {
            assertEquals(attributes[index++], value, 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "label";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        String expected = "{" + Arrays.toString(attributes) + ";" + classValue + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        int indexToRemove = 1;
        instance.removeAttribute(indexToRemove);
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
        assertEquals(2, instance.noAttributes());
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        double[] attributes3 = {1.0, 2.1, 3.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertTrue(instance1.equals(instance2));

        double[] attributes3 = {1.0, 2.1, 3.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertFalse(instance1.equals(instance3));

        DenseInstance instance4 = instance1.copy();
        assertTrue(instance1.equals(instance4));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copy = instance.copy();
        assertEquals(attributes, copy.attributes);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttributes(indicesToRemove);
        assertArrayEquals(new double[]{1.0}, instance.attributes, 0.0);
        assertEquals(1, instance.noAttributes());
    }
}