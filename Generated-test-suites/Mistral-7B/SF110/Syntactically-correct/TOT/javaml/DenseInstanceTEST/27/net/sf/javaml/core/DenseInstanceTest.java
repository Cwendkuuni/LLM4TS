package net.sf.javaml.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance instance = new DenseInstance(attributes, classValue);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        DenseInstance instance = new DenseInstance(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueWithInvalidIndex() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.value(-1);
    }

    @Test
    public void testClear() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testContainsKey() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> expected = instance.entrySet();
        Set<Map.Entry<Integer, Double>> actual = new HashSet<>();
        for (Map.Entry<Integer, Double> entry : expected) {
            actual.add(new SimpleEntry<>(entry.getKey(), entry.getValue()));
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
        assertEquals(3.0, instance.get(2), 0.001);
        try {
            instance.get(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testIsEmpty() {
        double[] attributes = new double[0];
        DenseInstance instance = new DenseInstance(attributes);
        assertTrue(instance.isEmpty());
        double[] attributes2 = new double[]{1.0};
        DenseInstance instance2 = new DenseInstance(attributes2);
        assertFalse(instance2.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        SortedSet<Integer> expected = instance.keySet();
        SortedSet<Integer> actual = new TreeSet<>();
        for (int i = 0; i < attributes.length; ++i) {
            actual.add(i);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testPut() {
        double[] attributes = new double[]{1.0, 2.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        instance.put(2, 3.0);
        assertEquals(3.0, instance.value(2), 0.001);
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = new double[]{1.0, 2.0};
        DenseInstance instance1 = new DenseInstance(attributes1);
        double[] attributes2 = new double[]{3.0, 4.0};
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 3.0);
        map.put(1, 4.0);
        instance1.putAll(map);
        assertEquals(4, instance1.noAttributes());
        assertEquals(3.0, instance1.value(0), 0.001);
        assertEquals(4.0, instance1.value(1), 0.001);
        assertEquals(1.0, instance1.value(2), 0.001);
        assertEquals(2.0, instance1.value(3), 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testValues() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        Collection<Double> expected = instance.values();
        Collection<Double> actual = new ArrayList<>();
        for (double value : expected) {
            actual.add(value);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveAttribute() {
        double[] attributes = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(attributes);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(3.0, instance.value(1), 0.001);
    }
}