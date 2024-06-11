package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

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
        Object classValue = "Class1";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDoubleArrayInConstructor() {
        new DenseInstance(new double[0]);
    }

    @Test
    public void testConstructorWithSize() {
        DenseInstance instance = new DenseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertArrayEquals(new double[5], instance.attributes, 0.0);
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
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(instance.containsKey(i));
        }
        assertFalse(instance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(instance.containsValue(Double.valueOf(attributes[i])));
        }
        assertFalse(instance.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(attributes.length, entries.size());
        for (int i = 0; i < attributes.length; ++i) {
            Map.Entry<Integer, Double> entry = entries.iterator().next();
            assertEquals(i, entry.getKey().intValue());
            assertEquals(attributes[i], entry.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], instance.get(Integer.valueOf(i)), 0.0);
        }
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.isEmpty());
        attributes = new double[5];
        instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> keys = instance.keySet();
        assertEquals(attributes.length, keys.size());
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(keys.contains(Integer.valueOf(i)));
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueof(2), Double.valueOf(3.0));
        instance.put(Integer.valueOf(3), Double.valueOf(4.0));
        instance.put(Integer.valueOf(4), Double.valueOf(5.0));

        assertEquals(5, instance.noAttributes());
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(Double.valueOf(i + 1.0), instance.get(Integer.valueOf(i)), 0.0);
        }
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {4.0, 5.0, 6.0};
        DenseInstance instance = new DenseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        map.put(Integer.valueOf(2), Double.valueOf(3.0));
        map.putAll(instance);

        assertEquals(6, instance.noAttributes());
        for (int i = 0; i < attributes1.length; ++i) {
            assertEquals(Double.valueOf(i + 1.0), instance.get(Integer.valueOf(i)), 0.0);
        }
        for (int i = 0; i < attributes2.length; ++i) {
            assertEquals(Double.valueOf(i + 4.0), instance.get(Integer.valueOf(attributes1.length + i)), 0.0);
        }
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
        assertEquals(3, instance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> values = instance.values();
        assertEquals(attributes.length, values.size());
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(Double.valueOf(attributes[i]), values.iterator().next(), 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = {};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "Class1";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        String expected = "{" + Arrays.toString(attributes) + ";" + classValue + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertArrayEquals(new double[] {1.0, 3.0}, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        attributes2[0] = 4.0;
        assertEquals(instance1.hashCode(), instance2.hashCode());

        attributes1[1] = 5.0;
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertTrue(instance1.equals(instance2));

        instance2.setClassValue("Class2");
        assertFalse(instance1.equals(instance2));

        double[] attributes3 = {1.0, 2.1, 3.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copy = instance.copy();
        assertEquals(attributes.length, copy.noAttributes());
        assertArrayEquals(attributes, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        BitSet bitSet = new BitSet(attributes.length);
        for (int i = 0; i < attributes.length; ++i) {
            if (!indices.contains(i)) {
                bitSet.set(i);
            }
        }
        assertArrayEquals(attributes, instance.attributes, bitSet.toArray());
    }
}