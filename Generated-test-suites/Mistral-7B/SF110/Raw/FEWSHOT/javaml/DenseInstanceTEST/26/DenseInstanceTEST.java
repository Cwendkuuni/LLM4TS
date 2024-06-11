package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "label1";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
        assertEquals("label1", denseInstance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance denseInstance = new DenseInstance();
        assertEquals(0, denseInstance.noAttributes());
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance denseInstance = new DenseInstance(size);
        assertEquals(5, denseInstance.noAttributes());
        for (int i = 0; i < size; ++i) {
            assertEquals(0.0, denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(denseInstance.containsValue(attributes[i]));
        }
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
        int i = 0;
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(i, entry.getKey().intValue());
            assertEquals(attributes[i], entry.getValue().doubleValue());
            ++i;
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], denseInstance.get(i));
        }
        assertNull(denseInstance.get(-1));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keys = denseInstance.keySet();
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(keys.contains(i));
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double newValue = 4.0;
        int key = 3;
        double oldValue = denseInstance.put(key, newValue);
        assertEquals(newValue, denseInstance.value(key), 0.0);
        assertEquals(3.0, oldValue, 0.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double[] newAttributes = new double[]{4.0, 5.0, 6.0};
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < newAttributes.length; ++i) {
            map.put(i, newAttributes[i]);
        }
        denseInstance.putAll(map);
        for (int i = 0; i < newAttributes.length; ++i) {
            assertEquals(newAttributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.remove(0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        List<Double> values = new ArrayList<>();
        for (double value : denseInstance.values()) {
            values.add(value);
        }
        assertEquals(3, values.size());
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], values.get(i), 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals("{" + Arrays.toString(attributes) + ";" + denseInstance.classValue() + "}", denseInstance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(2);
        assertEquals(3, denseInstance.noAttributes());
        assertNotEquals(3.0, denseInstance.value(2), 0.0);
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        double[] attributes3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertEquals(denseInstance1.hashCode(), denseInstance1.hashCode());
        assertEquals(denseInstance1.hashCode(), denseInstance1.copy().hashCode());
        assertNotEquals(denseInstance1.hashCode(), denseInstance2.hashCode());
        assertNotEquals(denseInstance1.hashCode(), denseInstance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance1 = new DenseInstance(attributes1);
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance2 = new DenseInstance(attributes2);
        double[] attributes3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance denseInstance3 = new DenseInstance(attributes3);
        assertTrue(denseInstance1.equals(denseInstance1));
        assertTrue(denseInstance1.equals(denseInstance1.copy()));
        assertTrue(denseInstance1.equals(denseInstance2));
        assertFalse(denseInstance1.equals(denseInstance3));
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        DenseInstance copy = denseInstance.copy();
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, copy.noAttributes());
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
            assertEquals(attributes[i], copy.value(i), 0.0);
        }
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        denseInstance.removeAttributes(indices);
        assertEquals(2, denseInstance.noAttributes());
        assertNotEquals(2.0, denseInstance.value(1), 0.0);
        assertNotEquals(3.0, denseInstance.value(2), 0.0);
    }
}