package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceConstruction() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(5, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testDenseInstanceWithClassValue() {
        double[] attributes = new double[5];
        Object classValue = "Label1";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(5, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.getClassValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDenseInstanceEmptyConstructor() {
        new DenseInstance();
    }

    @Test
    public void testDenseInstanceWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);
        assertEquals(5, instance.noAttributes());
        assertArrayEquals(new double[5], instance.attributes, 0.0);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testDenseInstanceValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testDenseInstanceClear() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
        assertNull(instance.getClassValue());
    }

    @Test
    public void testDenseInstanceContainsKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testDenseInstanceContainsValue() {
        double[] attributes = new double[]{1.0, 1.5, 2.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(1.5));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testDenseInstanceEntrySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(3, entries.size());
        Map.Entry<Integer, Double> entry = entries.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue().doubleValue(), 0.0);
    }

    @Test
    public void testDenseInstanceGet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        try {
            instance.get(3);
            fail();
        } catch (IndexOutOfBoundsException e) {
            //Expected
        }
    }

    @Test
    public void testDenseInstanceIsEmpty() {
        double[] attributes = new double[0];
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.isEmpty());
        double[] nonEmptyAttributes = new double[]{1.0, 2.0, 3.0};
        instance = new DenseInstance(nonEmptyAttributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testDenseInstanceKeySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> keys = instance.keySet();
        assertEquals(3, keys.size());
        assertTrue(keys.contains(0));
        assertTrue(keys.contains(1));
        assertTrue(keys.contains(2));
        assertFalse(keys.contains(3));
    }

    @Test
    public void testDenseInstancePut() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Double oldValue = instance.put(0, 4.0);
        assertEquals(1.0, oldValue.doubleValue(), 0.0);
        assertEquals(4.0, instance.get(0), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDenseInstanceUnsetValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }

    @Test
    public void testDenseInstanceSize() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(5, instance.noAttributes());
        double[] smallerAttributes = new double[3];
        instance = new DenseInstance(smallerAttributes);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testDenseInstanceValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }

    @Test
    public void testDenseInstanceToString() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals("{1.0, 2.0, 3.0;null}", instance.toString());
    }

    @Test
    public void testDenseInstanceRemoveAttribute() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
    }

    @Test
    public void testDenseInstanceHashCode() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(attributes2);
        double[] attributes3 = new double[]{1.0, 2.1, 3.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testDenseInstanceEquals() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(attributes2);
        double[] attributes3 = new double[]{1.0, 2.1, 3.0};
        DenseInstance instance3 = new DenseInstance(attributes3);
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals("String"));
    }

    @Test
    public void testDenseInstanceCopy() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        assertArrayEquals(instance.attributes, copiedInstance.attributes, 0.0);
        assertEquals(instance.getClassValue(), copiedInstance.getClassValue());
    }

    @Test
    public void testDenseInstanceRemoveAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
        instance.removeAttributes(indicesToRemove);
        assertEquals(1, instance.noAttributes());
        assertArrayEquals(new double[]{1.0}, instance.attributes, 0.0);
    }
}