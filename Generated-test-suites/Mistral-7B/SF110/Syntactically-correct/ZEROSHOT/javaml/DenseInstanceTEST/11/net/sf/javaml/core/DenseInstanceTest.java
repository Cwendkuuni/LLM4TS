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
        assertNotNull(instance.classValue());

        DenseInstance instanceWithClassValue = new DenseInstance(attributes, "classValue");
        assertEquals("classValue", instanceWithClassValue.classValue());

        DenseInstance emptyInstance = new DenseInstance();
        assertEquals(0, emptyInstance.noAttributes());
        assertNull(emptyInstance.classValue());
    }

    @Test
    public void testValueGetting() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes, "classValue");

        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], instance.value(i), 0.0);
        }

        DenseInstance instanceWithClassValue = new DenseInstance(attributes, "classValue");
        assertEquals("classValue", instanceWithClassValue.classValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueGettingOutOfBounds() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        instance.value(-1);
    }

    @Test
    public void testClear() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.classValue());
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; i++) {
            assertTrue(instance.containsKey(i));
        }

        assertFalse(instance.containsKey(5));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], instance.value(i), 0.0);
            assertTrue(instance.containsValue(attributes[i]));
        }

        assertFalse(instance.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);

        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(5, entrySet.size());

        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, (int) entry.getKey());
            assertEquals(value, instance.value(key), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);

        for (int i = 0; i < attributes.length; i++) {
            assertEquals(attributes[i], instance.get(i), 0.0);
        }

        assertNull(instance.get(5));
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        assertFalse(instance.isEmpty());

        DenseInstance emptyInstance = new DenseInstance();
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        Set<Integer> keySet = instance.keySet();
        assertEquals(5, keySet.size());

        for (int i = 0; i < attributes.length; i++) {
            assertTrue(keySet.contains(i));
        }

        assertFalse(keySet.contains(5));
    }

    @Test
    public void testPut() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);

        double newValue = 2.0;
        int index = 1;
        Double oldValue = instance.put(index, newValue);
        assertEquals(newValue, instance.value(index), 0.0);
        assertEquals(1.0, oldValue, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutUnsupported() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);
        instance.put(0, null);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = new double[] {1.0, 2.0, 3.0};
        double[] attributes2 = new double[] {4.0, 5.0, 6.0};
        DenseInstance instance = new DenseInstance(attributes1);
        instance.putAll(new HashMap<Integer, Double>() {
            {
                put(1, 7.0);
                put(3, 8.0);
            }
        });

        assertEquals(6, instance.noAttributes());
        assertEquals(2.0, instance.value(0), 0.0);
        assertEquals(1.0, instance.value(1), 0.0);
        assertEquals(7.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
        assertEquals(8.0, instance.value(3), 0.0);
        assertEquals(3.0, instance.value(4), 0.0);
        assertEquals(4.0, instance.value(5), 0.0);
        assertEquals(5.0, instance.value(6), 0.0);
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);

        instance.removeAttribute(2);
        assertEquals(4, instance.noAttributes());

        for (int i = 0; i < attributes.length; i++) {
            if (i != 2) {
                assertEquals(attributes[i], instance.value(i), 0.0);
            } else {
                assertNull(instance.value(i));
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveValueUnsupported() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(1);
    }

    @Test
    public void testSize() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(5, instance.size());
    }

    @Deprecated
    @Test
    public void testSizeDeprecated() {
        double[] attributes = new double[5];
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(5, instance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[5];
        Arrays.fill(attributes, 1.0);
        DenseInstance instance = new DenseInstance(attributes);

        Collection<Double> values = instance.values();
        assertEquals(5, values.size());

        for (Double value : values) {
            assertEquals(value, instance.value(Arrays.asList(instance.keySet()).indexOf(value.intValue())), 0.0);
        }
    }

    @Test
    public void testToString() {
        double[] attributes = new double[] {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        String expected = "{" + Arrays.toString(attributes) + ";classValue}";
        assertEquals(expected, instance.toString());
    }
}