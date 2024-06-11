package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithEmptyArray() {
        DenseInstance instance = new DenseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertNull(instance.classValue());
    }

    @Test
    public void testDenseInstanceWithValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertNull(instance.classValue());

        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);

        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue().doubleValue(), 0.0);

        Set<Integer> keySet = instance.keySet();
        Integer key = keySet.iterator().next();
        assertEquals(0, key.intValue());

        Double value = instance.get(0);
        assertEquals(1.0, value.doubleValue(), 0.0);

        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());

        Set<Integer> keys = new HashSet<Integer>();
        keys.add(0);
        keys.add(1);
        instance.putAll(Collections.singletonMap(keys.iterator().next(), 3.0));
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(3.0, instance.value(1), 0.0);

        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertNull(instance.value(0));
    }

    @Test
    public void testDenseInstanceWithNullClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att, null);
        assertNull(instance.classValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCantRemoveValueFromDenseInstance() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Integer key = 0;
        instance.remove(key);
    }

    @Test
    public void testHashCodeAndEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);

        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertTrue(instance1.equals(instance2));

        double[] att3 = {1.1, 2.0, 3.0};
        DenseInstance instance3 = new DenseInstance(att3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
        assertFalse(instance1.equals(instance3));

        double[] att4 = {1.0, 2.0, 3.1};
        DenseInstance instance4 = new DenseInstance(att4);
        assertNotEquals(instance1.hashCode(), instance4.hashCode());
        assertFalse(instance1.equals(instance4));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = instance.copy();

        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.hashCode(), copy.hashCode());
        assertTrue(instance.equals(copy));
        assertTrue(Arrays.equals(instance.attributes, copy.attributes));

        instance.removeAttribute(0);
        copy.removeAttribute(0);
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertTrue(instance.isEmpty());
        assertTrue(copy.isEmpty());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertNull(instance.value(0));
        assertNull(instance.value(2));
    }
}