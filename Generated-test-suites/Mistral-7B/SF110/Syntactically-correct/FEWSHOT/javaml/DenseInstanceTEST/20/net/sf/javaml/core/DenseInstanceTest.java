package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.noAttributes());
        assertArrayEquals(att, instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] att = {1.0, 2.0, 3.0};
        Object classValue = "classValue";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertEquals(att.length, instance.noAttributes());
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance instance = new DenseInstance();
        assertEquals(0, instance.noAttributes());
        assertNotNull(instance.classValue());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance instance = new DenseInstance(size);
        assertEquals(size, instance.noAttributes());
        assertArrayEquals(new double[size], instance.attributes, 0.0);
    }

    @Test
    public void testValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < att.length; ++i) {
            assertEquals(att[i], instance.value(i), 0.0);
        }
    }

    @Test
    public void testClear() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsKeyNegativeIndex() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.containsKey(-1));
    }

    @Test
    public void testContainsKeyValidIndex() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < att.length; ++i) {
            assertTrue(instance.containsKey(i));
        }
    }

    @Test
    public void testContainsValue() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < att.length; ++i) {
            assertTrue(instance.containsValue(Double.valueOf(att[i])));
        }
        assertFalse(instance.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testGet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < att.length; ++i) {
            assertEquals(att[i], instance.get(Integer.valueOf(i)), 0.0);
        }
    }

    @Test
    public void testIsEmpty() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> expected = new HashSet<Map.Entry<Integer, Double>>();
        for (int i = 0; i < att.length; ++i) {
            expected.add(Map.entry(Integer.valueOf(i), Double.valueOf(att[i])));
        }
        Set<Map.Entry<Integer, Double>> actual = instance.entrySet();
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<Integer, Double> entry : expected) {
            Map.Entry<Integer, Double> entryInSet = actual.iterator().next();
            actual.remove(entryInSet);
            assertEquals(entry, entryInSet);
        }
    }

    @Test
    public void testPut() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        double newValue = 4.0;
        int index = 2;
        instance.put(Integer.valueOf(index), Double.valueOf(newValue));
        assertEquals(newValue, instance.value(index), 0.0);
        assertEquals(att.length, instance.noAttributes());
        assertArrayEquals(att, Arrays.copyOfRange(instance.attributes, 0, index), 0.0);
        assertArrayEquals(new double[]{newValue}, Arrays.copyOfRange(instance.attributes, index, instance.attributes.length), 0.0);
    }

    @Test
    public void testPutAll() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        double[] newAtt = {4.0, 5.0};
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        for (int i = 0; i < newAtt.length; ++i) {
            map.put(Integer.valueOf(att.length + i), Double.valueOf(newAtt[i]));
        }
        instance.putAll(map);
        assertEquals(att.length + newAtt.length, instance.noAttributes());
        for (int i = 0; i < att.length; ++i) {
            assertEquals(att[i], instance.value(i), 0.0);
        }
        for (int i = 0; i < newAtt.length; ++i) {
            assertEquals(newAtt[i], instance.value(att.length + i), 0.0);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        instance.remove(0);
    }

    @Test
    public void testSize() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.size());
    }

    @Test
    public void testValues() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        List<Double> expected = Arrays.asList(att);
        List<Double> actual = new ArrayList<Double>(instance.values());
        assertEquals(expected, actual);
    }

    @Test
    public void testNoAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        int index = 1;
        instance.removeAttribute(index);
        assertEquals(att.length - 1, instance.noAttributes());
        assertArrayEquals(Arrays.copyOfRange(instance.attributes, 0, index), instance.attributes, 0.0);
        assertArrayEquals(Arrays.copyOfRange(instance.attributes, index + 1, instance.attributes.length), instance.attributes, index);
    }

    @Test
    public void testHashCode() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.1, 2.1, 3.1};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = {1.0, 2.0, 3.0};
        double[] att2 = {1.0, 2.0, 3.0};
        double[] att3 = {1.1, 2.1, 3.1};
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance instance3 = new DenseInstance(att3);
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copiedInstance = instance.copy();
        assertEquals(att.length, copiedInstance.noAttributes());
        assertArrayEquals(att, copiedInstance.attributes, 0.0);
        assertEquals(instance.classValue(), copiedInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = {1.0, 2.0, 3.0};
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(att.length - 2, instance.noAttributes());
        assertArrayEquals(att, Arrays.copyOfRange(instance.attributes, 0, 1), 0.0);
        assertArrayEquals(Arrays.copyOfRange(instance.attributes, 3, instance.attributes.length), instance.attributes, 2);
    }
}