package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(att, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        Object classValue = "setosa";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithSize() {
        DenseInstance instance = new DenseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertArrayEquals(new double[5], instance.attributes);
        assertNull(instance.classValue());
    }

    @Test
    public void testValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
        assertThrows(IndexOutOfBoundsException.class, () -> instance.value(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> instance.value(3));
    }

    @Test
    public void testClear() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes);
        assertNull(instance.classValue());
    }

    @Test
    public void testContainsKey() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertTrue(instance.containsKey(i));
            assertTrue(instance.containsKey((Object) i));
        }
        assertFalse(instance.containsKey(-1));
        assertFalse(instance.containsKey("not an int"));
    }

    @Test
    public void testContainsValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        double value = 2.0;
        assertTrue(instance.containsValue(value));
        for (int i = 0; i < instance.noAttributes(); ++i) {
            if (instance.value(i) == value) {
                assertEquals(i, instance.keyAtValue(value));
                break;
            }
        }
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        int i = 0;
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(i, (int)entry.getKey());
            assertEquals(att[i], entry.getValue().doubleValue(), 0.0);
            ++i;
        }
    }

    @Test
    public void testGet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(att[i], instance.get(i));
            assertEquals(att[i], instance.get((Object) i));
        }
        assertNull(instance.get(-1));
        assertNull(instance.get("not an int"));
    }

    @Test
    public void testIsEmpty() {
        double[] att = new double[0];
        DenseInstance instance = new DenseInstance(att);
        assertTrue(instance.isEmpty());
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance2 = new DenseInstance(att2);
        assertFalse(instance2.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        SortedSet<Integer> keys = instance.keySet();
        int i = 0;
        for (Integer key : keys) {
            assertEquals(i, key.intValue());
            ++i;
        }
    }

    @Test
    public void testPut() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        double newValue = 4.0;
        int index = 2;
        Double oldValue = instance.put(index, newValue);
        assertEquals(newValue, instance.value(index), 0.0);
        assertNull(oldValue);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testPutAll() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        double[] newAtt = new double[]{4.0, 5.0, 6.0};
        Map<Integer, Double> newMap = new HashMap<>();
        for (int i = 0; i < newAtt.length; ++i) {
            newMap.put(i, newAtt[i]);
        }
        instance.putAll(newMap);
        assertEquals(newAtt.length, instance.noAttributes());
        assertArrayEquals(newAtt, instance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(instance.noAttributes(), instance.size());
    }

    @Test
    public void testValues() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Collection<Double> values = instance.values();
        int i = 0;
        for (Double value : values) {
            assertEquals(att[i], value.doubleValue(), 0.0);
            ++i;
        }
    }

    @Test
    public void testNoAttributes() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals("[1.0, 2.0, 3.0;]", instance.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance instance = new DenseInstance(att);
        instance.removeAttribute(2);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 2.0, 3.0}, instance.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        assertEquals(instance1.hashCode(), instance2.hashCode());

        double[] att3 = new double[]{1.0, 2.0, 3.1};
        DenseInstance instance3 = new DenseInstance(att3);
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att1);
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertTrue(instance1.equals(instance3));

        double[] att4 = new double[]{1.0, 2.1, 3.0};
        DenseInstance instance4 = new DenseInstance(att4);
        assertFalse(instance1.equals(instance4));

        Object obj = new Object();
        assertFalse(instance1.equals(obj));
    }

    @Test
    public void testCopy() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = instance.copy();
        assertEquals(instance, copy);
        assertNotSame(instance, copy);
        assertArrayEquals(att, copy.attributes, 0.0);
        assertEquals(instance.classValue(), copy.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0}, instance.attributes, 0.0);
    }
}