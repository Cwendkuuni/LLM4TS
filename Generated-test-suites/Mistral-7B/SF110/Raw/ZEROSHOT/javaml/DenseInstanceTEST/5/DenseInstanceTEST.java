package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class DenseInstanceTest {

    private DenseInstance denseInstance;

    @Before
    public void setup() {
        double[] attributes = new double[]{1.0, 2.0, 3.0, 4.0};
        denseInstance = new DenseInstance(attributes);
    }

    @Test
    public void testConstructorWithDoubleArray() {
        assertNotNull(denseInstance);
        assertEquals(4, denseInstance.noAttributes());
        assertArrayEquals(denseInstance.attributes, new double[]{1.0, 2.0, 3.0, 4.0}, 0.0);
    }

    @Test
    public void testConstructorWithDoubleArrayAndClassValue() {
        Object classValue = "ClassValue";
        DenseInstance denseInstanceWithClassValue = new DenseInstance(new double[]{1.0, 2.0, 3.0, 4.0}, classValue);
        assertEquals(classValue, denseInstanceWithClassValue.classValue());
    }

    @Test
    public void testEmptyConstructor() {
        DenseInstance emptyDenseInstance = new DenseInstance();
        assertTrue(emptyDenseInstance.isEmpty());
    }

    @Test
    public void testValue() {
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(4.0, denseInstance.value(3), 0.0);
    }

    @Test
    public void testClear() {
        denseInstance.clear();
        assertTrue(denseInstance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        for (int i = 0; i < denseInstance.noAttributes(); ++i) {
            assertTrue(denseInstance.containsKey(i));
        }
        assertFalse(denseInstance.containsKey(-1));
    }

    @Test
    public void testContainsValue() {
        for (int i = 0; i < denseInstance.noAttributes(); ++i) {
            assertTrue(denseInstance.containsValue(denseInstance.value(i)));
        }
        assertFalse(denseInstance.containsValue(5.0));
    }

    @Test
    public void testEntrySet() {
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertEquals(4, entrySet.size());
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(entrySet);
        for (int i = 0; i < denseInstance.noAttributes(); ++i) {
            Map.Entry<Integer, Double> entry = list.get(i);
            assertEquals(i, entry.getKey());
            assertEquals(denseInstance.value(i), entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        for (int i = 0; i < denseInstance.noAttributes(); ++i) {
            assertEquals(denseInstance.value(i), denseInstance.get(i), 0.0);
        }
        assertNull(denseInstance.get(-1));
    }

    @Test
    public void testIsEmpty() {
        assertFalse(denseInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        Set<Integer> keySet = denseInstance.keySet();
        assertEquals(denseInstance.noAttributes(), keySet.size());
        List<Integer> list = new ArrayList<>(keySet);
        for (int i = 0; i < denseInstance.noAttributes(); ++i) {
            assertEquals(i, list.get(i));
        }
    }

    @Test
    public void testPut() {
        double newValue = 10.0;
        denseInstance.put(1, newValue);
        assertEquals(newValue, denseInstance.value(1), 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutThrowsException() {
        denseInstance.put(1, "InvalidValue");
    }

    @Test
    public void testPutAll() {
        DenseInstance anotherDenseInstance = new DenseInstance(new double[]{5.0, 6.0, 7.0, 8.0});
        denseInstance.putAll(anotherDenseInstance.entrySet());
        assertEquals(8, denseInstance.noAttributes());
        for (int i = 0; i < denseInstance.noAttributes(); ++i) {
            if (i < 4) {
                assertEquals(denseInstance.value(i), anotherDenseInstance.value(i), 0.0);
            } else {
                assertEquals(anotherDenseInstance.value(i - 4), denseInstance.value(i), 0.0);
            }
        }
    }

    @Test
    public void testRemoveAttribute() {
        denseInstance.removeAttribute(1);
        assertEquals(3, denseInstance.noAttributes());
        assertNotEquals(1.0, denseInstance.value(1), 0.0);
    }

    @Test
    public void testHashCode() {
        DenseInstance anotherDenseInstance = new DenseInstance(denseInstance.attributes.clone());
        assertEquals(hashCode(), anotherDenseInstance.hashCode());
    }

    @Test
    public void testEquals() {
        DenseInstance anotherDenseInstance = new DenseInstance(denseInstance.attributes.clone());
        assertTrue(denseInstance.equals(anotherDenseInstance));
        DenseInstance differentDenseInstance = new DenseInstance(new double[]{1.1, 2.1, 3.1, 4.1});
        assertFalse(denseInstance.equals(differentDenseInstance));
    }

    @Test
    public void testCopy() {
        DenseInstance copiedDenseInstance = denseInstance.copy();
        assertEquals(denseInstance.noAttributes(), copiedDenseInstance.noAttributes());
        assertArrayEquals(denseInstance.attributes, copiedDenseInstance.attributes, 0.0);
        assertEquals(denseInstance.classValue(), copiedDenseInstance.classValue());
    }

    @Test
    public void testRemoveAttributes() {
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(2, denseInstance.noAttributes());
        assertArraySubsequence(Arrays.copyOfRange(denseInstance.attributes, 0, 2), new double[]{3.0, 4.0});
    }
}