package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithDoubleArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(attributes.length, denseInstance.noAttributes());
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance denseInstance = new DenseInstance(attributes, classValue);
        assertEquals(attributes.length, denseInstance.noAttributes());
        assertEquals(classValue, denseInstance.getClassValue());
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance denseInstance = new DenseInstance();
        assertEquals(0, denseInstance.noAttributes());
        assertNull(denseInstance.getClassValue());
        assertArrayEquals(new double[0], denseInstance.attributes);
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance denseInstance = new DenseInstance(size);
        assertEquals(size, denseInstance.noAttributes());
        assertArrayEquals(new double[size], denseInstance.attributes);
    }

    @Test
    public void testValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test
    public void testClear() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        assertArrayEquals(new double[0], denseInstance.attributes);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey("nonIntKey"));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(denseInstance.containsValue(attributes[i]));
        }
        double nonExistingValue = 4.0;
        assertFalse(denseInstance.containsValue(nonExistingValue));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(attributes.length, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], denseInstance.get(i), 0.0);
        }
        assertNull(denseInstance.get("nonIntKey"));
    }

    @Test
    public void testIsEmpty() {
        DenseInstance denseInstance = new DenseInstance();
        assertTrue(denseInstance.isEmpty());
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        denseInstance = new DenseInstance(attributes);
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keySet = denseInstance.keySet();
        assertEquals(attributes.length, keySet.size());
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(keySet.contains(i));
        }
    }

    @Test
    public void testPut() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double newValue = 4.0;
        int key = 3;
        double oldValue = denseInstance.get(key);
        denseInstance.put(key, newValue);
        assertEquals(newValue, denseInstance.get(key), 0.0);
        assertNotEquals(oldValue, denseInstance.get(key), 0.0);
    }

    @Test
    public void testPutAll() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        double[] newAttributes = new double[]{4.0, 5.0, 6.0};
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < newAttributes.length; i++) {
            map.put(i, newAttributes[i]);
        }
        denseInstance.putAll(map);
        assertEquals(newAttributes.length, denseInstance.noAttributes());
        for (int i = 0; i < newAttributes.length; i++) {
            assertEquals(newAttributes[i], denseInstance.value(i), 0.0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.remove(0);
    }

    @Test
    public void testSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(attributes.length, denseInstance.noAttributes());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        List<Double> values = new ArrayList<>();
        for (double value : denseInstance.values()) {
            values.add(value);
        }
        assertEquals(attributes.length, values.size());
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], values.get(i), 0.0);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(attributes.length, denseInstance.noAttributes());
        DenseInstance emptyInstance = new DenseInstance();
        assertEquals(0, emptyInstance.noAttributes());
    }

    @Test
    public void testCopy() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance originalInstance = new DenseInstance(attributes);
        DenseInstance copy = originalInstance.copy();
        assertEquals(attributes.length, copy.noAttributes());
        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], copy.value(i), 0.0);
        }
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(attributes.length - indicesToRemove.size(), denseInstance.noAttributes());
        for (int i = 0; i < attributes.length; i++) {
            if (!indicesToRemove.contains(i)) {
                assertEquals(attributes[i], denseInstance.value(i), 0.0);
            }
        }
    }

    @Test
    public void testHashCode() {
        double[] attributes1 = new double[]{1.0, 2.0, 3.0};
        double[] attributes2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
        double[] attributes3 = new double[]{1.0, 2.1, 3.0};
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
        DenseInstance instance3 = new DenseInstance(new double[]{1.0, 2.1, 3.0});
        assertFalse(instance1.equals(instance3));
    }
}