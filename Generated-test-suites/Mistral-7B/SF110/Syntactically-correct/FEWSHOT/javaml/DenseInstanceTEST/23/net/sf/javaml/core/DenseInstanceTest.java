package net.sf.javaml.core;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertNotNull(instance);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.getAttributes(), 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "Class Label";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertNotNull(instance);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.getAttributes(), 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance instance = new DenseInstance();
        assertNotNull(instance);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);
        assertNotNull(instance);
        assertEquals(size, instance.noAttributes());
        double[] attributes = new double[size];
        assertArrayEquals(attributes, instance.getAttributes(), 0.0);
    }

    @Test
    public void testValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClearMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.getAttributes(), 0.0);
    }

    @Test
    public void testContainsKeyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(instance.containsKey(i));
        }
        assertFalse(instance.containsKey(4));
    }

    @Test
    public void testContainsValueMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(instance.containsValue(attributes[i]));
        }
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(attributes.length, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getKey());
            assertEquals(attributes[entry.getKey()], entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], instance.get(i), 0.0);
        }
        assertNull(instance.get(4));
    }

    @Test
    public void testIsEmptyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = instance.keySet();
        assertEquals(attributes.length, keySet.size());
        for (Integer key : keySet) {
            assertEquals(key, key);
        }
    }

    @Test
    public void testPutMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Double newValue = 4.0;
        int index = 3;
        instance.put(index, newValue);
        double[] newAttributes = new double[attributes.length + 1];
        System.arraycopy(attributes, 0, newAttributes, 0, attributes.length);
        newAttributes[attributes.length] = newValue;
        assertEquals(newAttributes, instance.getAttributes(), 0.0);
    }

    @Test
    public void testPutAllMethod() {
        double[] attributesA = new double[]{1.0, 2.0, 3.0};
        double[] attributesB = new double[]{4.0, 5.0, 6.0};
        DenseInstance instanceA = new DenseInstance(attributesA);
        DenseInstance instanceB = new DenseInstance(attributesB);
        Map<Integer, Double> map = new HashMap<>();
        map.put(3, 4.0);
        map.put(6, 5.0);
        instanceA.putAll(map);
        double[] newAttributes = new double[attributesA.length + 2];
        System.arraycopy(attributesA, 0, newAttributes, 0, attributesA.length);
        newAttributes[attributesA.length] = 4.0;
        newAttributes[attributesA.length + 1] = 5.0;
        assertEquals(newAttributes, instanceA.getAttributes(), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }

    @Test
    public void testSizeMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(attributes.length, instance.noAttributes());
    }

    @Test
    public void testValuesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> values = instance.values();
        assertEquals(attributes.length, values.size());
        for (Double value : values) {
            assertEquals(value, value);
        }
    }

    @Test
    public void testNoAttributesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(attributes.length, instance.noAttributes());
    }

    @Test
    public void testToStringMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals("{" + Arrays.toString(attributes) + ";Class Label}", instance.toString());
    }

    @Test
    public void testRemoveAttributeMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(2);
        double[] newAttributes = new double[attributes.length - 1];
        System.arraycopy(attributes, 0, newAttributes, 0, 2);
        System.arraycopy(attributes, 3, newAttributes, 2, attributes.length - 3);
        assertEquals(newAttributes, instance.getAttributes(), 0.0);
    }

    @Test
    public void testHashCodeMethod() {
        double[] attributesA = new double[]{1.0, 2.0, 3.0};
        double[] attributesB = new double[]{1.0, 2.0, 3.0};
        DenseInstance instanceA = new DenseInstance(attributesA);
        DenseInstance instanceB = new DenseInstance(attributesB);
        assertEquals(instanceA.hashCode(), instanceB.hashCode());

        double[] attributesC = new double[]{1.0, 2.0, 4.0};
        DenseInstance instanceC = new DenseInstance(attributesC);
        assertNotEquals(instanceA.hashCode(), instanceC.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        double[] attributesA = new double[]{1.0, 2.0, 3.0};
        double[] attributesB = new double[]{1.0, 2.0, 3.0};
        DenseInstance instanceA = new DenseInstance(attributesA);
        DenseInstance instanceB = new DenseInstance(attributesB);
        assertTrue(instanceA.equals(instanceB));

        double[] attributesC = new double[]{1.0, 2.1, 3.0};
        DenseInstance instanceC = new DenseInstance(attributesC);
        assertFalse(instanceA.equals(instanceC));

        Object other = new Object();
        assertFalse(instanceA.equals(other));

        DenseInstance instanceD = instanceA.copy();
        assertTrue(instanceA.equals(instanceD));
    }

    @Test
    public void testCopyMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copy = instance.copy();
        assertNotNull(copy);
        assertEquals(attributes, copy.getAttributes(), 0.0);
        assertEquals(instance.getClassValue(), copy.getClassValue());
    }

    @Test
    public void testRemoveAttributesMethod() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<Integer>();
        indicesToRemove.add(2);
        indicesToRemove.add(4);
        instance.removeAttributes(indicesToRemove);
        double[] newAttributes = new double[attributes.length - 2];
        System.arraycopy(attributes, 0, newAttributes, 0, 1);
        System.arraycopy(attributes, 3, newAttributes, 1, attributes.length - 3);
        assertEquals(newAttributes, instance.getAttributes(), 0.0);
    }
}