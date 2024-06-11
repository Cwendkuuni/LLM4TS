package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
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
        assertNull(instance.classValue());

        DenseInstance instanceWithClassValue = new DenseInstance(attributes, "Class Value");
        assertEquals("Class Value", instanceWithClassValue.classValue());
    }

    @Test
    public void testDenseInstanceValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
        try {
            instance.value(-1);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testDenseInstanceClear() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testDenseInstanceContainsKey() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertTrue(instance.containsKey(i));
            assertFalse(instance.containsKey(Double.NaN));
            assertFalse(instance.containsKey("key"));
        }
    }

    @Test
    public void testDenseInstanceContainsValue() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.attributes[i], instance.get(i), 0.0);
            assertTrue(instance.containsValue(instance.attributes[i]));
            assertFalse(instance.containsValue(instance.attributes[i] + 1.0));
        }
    }

    @Test
    public void testDenseInstanceEntrySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(instance.noAttributes(), entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
            assertEquals(instance.attributes[entry.getKey()], entry.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testDenseInstanceGet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.attributes[i], instance.get(i), 0.0);
            assertEquals(instance.attributes[i], instance.get(new Integer(i)), 0.0);
        }
    }

    @Test
    public void testDenseInstanceIsEmpty() {
        DenseInstance instance = new DenseInstance(new double[0]);
        assertTrue(instance.isEmpty());
        DenseInstance nonEmptyInstance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertFalse(nonEmptyInstance.isEmpty());
    }

    @Test
    public void testDenseInstanceKeySet() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Set<Integer> keySet = instance.keySet();
        assertEquals(instance.noAttributes(), keySet.size());
        for (Integer key : keySet) {
            assertTrue(instance.containsKey(key));
            assertEquals(instance.attributes[key], instance.get(key), 0.0);
        }
    }

    @Test
    public void testDenseInstancePut() {
        DenseInstance instance = new DenseInstance(new double[5]);
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(4, 3.0);
        assertEquals(1.0, instance.attributes[0], 0.0);
        assertEquals(2.0, instance.attributes[1], 0.0);
        assertEquals(3.0, instance.attributes[4], 0.0);
        try {
            instance.put(5, 4.0);
            fail("Exception expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testDenseInstancePutAll() {
        DenseInstance instance = new DenseInstance(new double[5]);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        map.put(4, 3.0);
        instance.putAll(map);
        assertEquals(1.0, instance.attributes[0], 0.0);
        assertEquals(2.0, instance.attributes[1], 0.0);
        assertEquals(3.0, instance.attributes[4], 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDenseInstanceRemove() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        instance.remove(0);
    }

    @Test
    public void testDenseInstanceSize() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testDenseInstanceValues() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        for (Double value : values) {
            assertEquals(instance.attributes[Arrays.asList(instance.keySet()).get(values.indexOf(value)).intValue()], value.doubleValue(), 0.0);
        }
    }

    @Test
    public void testDenseInstanceCopy() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance copiedInstance = instance.copy();
        assertEquals(instance, copiedInstance);
        assertNotSame(instance, copiedInstance);
        assertArrayEquals(instance.attributes, copiedInstance.attributes, 0.0);
    }

    @Test
    public void testDenseInstanceRemoveAttributes() {
        DenseInstance instance = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 3));
        instance.removeAttributes(indicesToRemove);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 4.0, 5.0}, instance.attributes, 0.0);
    }

    @Test
    public void testDenseInstanceHashcodeEquals() {
        DenseInstance instance1 = new DenseInstance(new double[]{1.0, 2.0, 3.0});
        DenseInstance instance2 = new DenseInstance(instance1);
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertEquals(instance1, instance2);

        DenseInstance instance3 = new DenseInstance(new double[]{2.0, 2.0, 3.0});
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
        assertNotEquals(instance1, instance3);
    }
}