package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceEmptyConstruction() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceEmptySetterConstruction() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(0);
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceBasicConstruction() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceBasicConstructionWithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 3.0);
        assertEquals(5, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceBasicConstructionWithClassValue() {
        SparseInstance instance = new SparseInstance(5, "StringClass");
        assertEquals("StringClass", instance.getClassValue());
        assertEquals(5, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceDenseVectorConstruction() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(5, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < 5; ++i) {
            assertEquals(datavector[i], instance.get(i));
        }
    }

    @Test
    public void testSparseInstanceDenseVectorConstructionWithDefaultValue() {
        double[] datavector = {1.0, 0.0, 3.0, 4.0, 5.0};
        SparseInstance instance = new SparseInstance(datavector, 2.0);
        assertEquals(5, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < 5; ++i) {
            if (datavector[i] != 0.0) {
                assertEquals(datavector[i], instance.get(i));
            } else {
                assertEquals(2.0, instance.get(i));
            }
        }
    }

    @Test
    public void testSparseInstanceDenseVectorConstructionWithClassValue() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance instance = new SparseInstance(datavector, "ClassString");
        assertEquals("ClassString", instance.getClassValue());
        assertEquals(5, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < 5; ++i) {
            assertEquals(datavector[i], instance.get(i));
        }
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(1.0, instance.value(0));
        assertEquals(3.0, instance.value(2));
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertTrue(instance.containsKey(Integer.valueOf(0)));
        assertTrue(instance.containsKey(Integer.valueOf(2)));
        assertFalse(instance.containsKey(Integer.valueOf(3)));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1.0, instance.get(0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(3.0, instance.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullKey() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(null, Double.valueOf(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullValue() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), null);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(2, entrySet.size());
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(1.0, instance.get(0));
        assertEquals(3.0, instance.get(2));
        assertEquals(0.0, instance.get(4));
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Set<Integer> keySet = instance.keySet();
        assertEquals(2, keySet.size());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
        assertEquals(3.0, instance.get(1));
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        String expected = "{" + "1.0=" + 0 + ", 3.0=" + 2 + ";" + instance.getClassValue() + "}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance1.put(Integer.valueOf(2), Double.valueOf(3.0));
        SparseInstance instance2 = new SparseInstance();
        instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance2.put(Integer.valueOf(2), Double.valueOf(3.0));
        SparseInstance instance3 = new SparseInstance();
        instance3.put(Integer.valueOf(1), Double.valueOf(1.0));
        instance3.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance1.put(Integer.valueOf(2), Double.valueOf(3.0));
        SparseInstance instance2 = new SparseInstance();
        instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance2.put(Integer.valueOf(2), Double.valueOf(3.0));
        SparseInstance instance3 = new SparseInstance();
        instance3.put(Integer.valueOf(1), Double.valueOf(1.0));
        instance3.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
        SparseInstance instance4 = instance1.copy();
        assertTrue(instance1.equals(instance4));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        SparseInstance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        for (int i = 0; i < instance.noAttributes(); ++i) {
            assertEquals(instance.get(i), copiedInstance.get(i));
        }
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(0);
        indices.add(2);
        instance.removeAttributes(indices);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
        assertFalse(instance.containsKey(Integer.valueOf(2)));
        assertEquals(instance.get(1), instance.get(1));
    }
}