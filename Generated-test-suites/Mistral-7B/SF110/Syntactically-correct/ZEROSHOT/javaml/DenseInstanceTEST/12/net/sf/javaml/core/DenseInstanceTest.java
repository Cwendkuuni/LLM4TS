package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstruction() {
        // Test empty instance
        DenseInstance emptyInstance = new DenseInstance();
        assertEquals(0, emptyInstance.noAttributes());

        // Test instance with attributes
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);

        // Test instance with class value
        Object classValue = "classValue";
        DenseInstance instanceWithClassValue = new DenseInstance(attributes, classValue);
        assertEquals(classValue, instanceWithClassValue.getClassValue());
    }

    @Test
    public void testValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), attributes[i], 0.0);
        }
    }

    @Test
    public void testClear() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.clear();
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertTrue(instance.containsKey(i));
            assertFalse(instance.containsKey(i + 10));
        }
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertTrue(instance.containsValue(attributes[i]));
            assertFalse(instance.containsValue(attributes[i] + 0.1));
        }
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(instance.noAttributes(), entrySet.size());

        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
            assertEquals(attributes[entry.getKey()], entry.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.get(i), attributes[i], 0.0);
        }
    }

    @Test
    public void testIsEmpty() {
        DenseInstance instance = new DenseInstance();
        assertTrue(instance.isEmpty());

        double[] attributes = new double[] {1.0, 2.0, 3.0};
        instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        Set<Integer> keySet = instance.keySet();
        assertEquals(instance.noAttributes(), keySet.size());

        for (Integer key : keySet) {
            assertEquals(key, instance.getValue(key).intValue());
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        double newValue = 4.0;
        int key = 3;

        assertEquals(instance.get(key), attributes[key], 0.0);

        instance.put(key, newValue);
        assertEquals(newValue, instance.get(key), 0.0);

        // Test put with existing key
        instance.put(key, 5.0);
        assertEquals(newValue, instance.get(key - 1), 0.0);
        assertEquals(5.0, instance.get(key), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(instance.noAttributes(), instance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        List<Double> values = new ArrayList<>(instance.values());
        Collections.sort(values);

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), values.get(i), 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[0];
        DenseInstance instance = new DenseInstance(attributes);

        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        String expected = String.format("{%s;%s}", Arrays.toString(attributes), instance.getClassValue());
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        instance.removeAttribute(1);

        double[] expected = new double[] {1.0, 3.0};
        assertArrayEquals(expected, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);

        double[] attributes2 = new double[] {1.0, 2.0, 4.0};
        DenseInstance instance2 = new DenseInstance(attributes2);

        int expectedHashCode = Arrays.hashCode(attributes1);
        assertEquals(expectedHashCode, instance1.hashCode());
        assertNotEquals(expectedHashCode, instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);

        double[] attributes2 = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(attributes2);

        double[] attributes3 = new double[] {1.0, 2.0, 4.0};
        DenseInstance instance3 = new DenseInstance(attributes3);

        DenseInstance instance4 = instance1;

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance4));
        assertTrue(instance1.equals(instance2)); // Should pass due to Arrays.equals check

        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);

        DenseInstance copiedInstance = instance.copy();

        assertEquals(attributes, copiedInstance.attributes);
        assertNotSame(instance, copiedInstance);
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[] {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes);

        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));

        double[] expected = new double[] {1.0, 4.0};
        instance.removeAttributes(indicesToRemove);
        assertArrayEquals(expected, instance.attributes, 0.0);
    }
}