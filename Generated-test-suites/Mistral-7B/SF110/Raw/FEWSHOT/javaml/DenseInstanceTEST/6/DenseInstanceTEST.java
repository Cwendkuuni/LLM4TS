package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(attributes, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithEmptyArray() {
        DenseInstance instance = new DenseInstance(new double[0]);
        assertEquals(0, instance.noAttributes());
        assertNotNull(instance.classValue());
        assertArrayEquals(new double[0], instance.attributes);
    }

    @Test
    public void testValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueOutOfBounds() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.value(3);
    }

    @Test
    public void testClear() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.classValue());
        assertArrayEquals(new double[0], instance.attributes);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(0.5));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        int i = 0;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(i, entry.getKey());
            assertEquals(attributes[i], entry.getValue(), 0.0);
            i++;
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.0);
        assertEquals(2.0, instance.get(1), 0.0);
        assertEquals(3.0, instance.get(2), 0.0);
        assertNull(instance.get(-1));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        int i = 0;
        for (Integer key : keySet) {
            assertEquals(i, key);
            i++;
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        double newValue = 4.0;
        int index = 1;
        Double oldValue = instance.put(index, newValue);
        assertEquals(newValue, instance.value(index), 0.0);
        assertEquals(2.0, oldValue, 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPutOutOfBounds() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.put(3, 4.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{4.0, 5.0, 6.0};
        DenseInstance instance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<>();
        map.put(3, 4.0);
        map.put(4, 5.0);
        instance.putAll(map);
        assertEquals(6, instance.noAttributes());
        assertArrayEquals(Arrays.concat(attributes1, attributes2), instance.attributes);
    }

    @Test
    public void testRemove() {
        throw new UnsupportedOperationException("Cannot unset values from a dense instance.");
    }

    @Test
    public void testSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.size());
    }

    @Deprecated
    @Test
    public void testSizeDeprecated() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        List<Double> values = new ArrayList<>();
        for (double value : instance.values()) {
            values.add(value);
        }
        assertEquals(3, values.size());
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), values);
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals("[1.0, 2.0, 3.0;]", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(2);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0, 4.0}, instance.attributes);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        DenseInstance instance3 = new DenseInstance(attributes1);
        DenseInstance instance4 = new DenseInstance(new double[]{1.0, 2.1, 3.0});
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertTrue(instance1.equals(instance3));
        assertFalse(instance1.equals(instance4));
        assertFalse(instance1.equals(new Object()));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        DenseInstance copiedInstance = instance.copy();
        assertEquals(attributes.length, copiedInstance.noAttributes());
        assertArrayEquals(attributes, copiedInstance.attributes);
        assertEquals(instance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 5.0}, instance.attributes);
    }
}