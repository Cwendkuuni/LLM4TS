package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertArrayEquals(attributes, instance.attributes);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        Object classValue = "Class1";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertArrayEquals(attributes, instance.attributes);
        assertEquals(classValue, instance.getClassValue());
    }

    @Test
    public void testConstructorEmpty() {
        DenseInstance instance = new DenseInstance();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.getClassValue());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);
        double[] attributes = new double[size];
        assertArrayEquals(attributes, instance.attributes);
    }

    @Test
    public void testValue() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueOutOfBounds() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.value(3);
    }

    @Test
    public void testClear() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.getClassValue());
    }

    @Test
    public void testContainsKey() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = {1.0, 1.5, 2.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(1.5));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        int index = 0;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(index++, entry.getKey());
            assertEquals(attributes[index], entry.getValue());
        }
    }

    @Test
    public void testGet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(3));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());

        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> keySet = instance.keySet();
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
        int key = 3;
        Double oldValue = instance.put(key, newValue);
        assertEquals(newValue, instance.attributes[key]);
        assertEquals(oldValue, instance.attributes[3]);
        assertEquals(null, oldValue);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 3.5);
        map.put(2, 4.5);
        instance.putAll(map);
        double[] expectedAttributes = {1.0, 3.5, 2.0, 4.5, 3.0, 5.0};
        assertArrayEquals(expectedAttributes, instance.attributes);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttributes() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
    }

    @Test
    public void testSize() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.size());

        instance.removeAttribute(0);
        assertEquals(2, instance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        List<Double> values = new ArrayList<>();
        for (Double value : instance.values()) {
            values.add(value);
        }
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), values);
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
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals("{" + Arrays.toString(attributes) + ";Class1}", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        double[] expectedAttributes = {1.0, 3.0};
        assertArrayEquals(expectedAttributes, instance.attributes);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        attributes2[0] = 4.0;
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = {1.0, 2.0, 3.0};
        double[] attributes2 = {1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        DenseInstance instance3 = new DenseInstance(attributes1);
        DenseInstance instance4 = new DenseInstance(Arrays.copyOfRange(attributes1, 1, attributes1.length));
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertTrue(instance1.equals(instance3));
        assertFalse(instance1.equals(instance4));
        assertFalse(instance1.equals(null));
        assertFalse(instance1.equals(new Object()));
    }

    @Test
    public void testCopy() {
        double[] attributes = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copy = instance.copy();
        assertArrayEquals(attributes, instance.attributes);
        assertArrayEquals(attributes, copy.attributes);
        assertEquals(instance.getClassValue(), copy.getClassValue());
    }

    @Test
    public void testRemoveAttributesWithSet() {
        double[] attributes = {1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        indices.add(2);
        indices.add(4);
        instance.removeAttributes(indices);
        double[] expectedAttributes = {1.0, 3.0, 5.0};
        assertArrayEquals(expectedAttributes, instance.attributes);
    }
}