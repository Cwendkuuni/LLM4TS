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
    public void testSparseInstanceDefaultValueConstruction() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < 5; i++) {
            assertEquals(0.0, instance.get(i));
        }
    }

    @Test
    public void testSparseInstanceDefaultValueAndClassValueConstruction() {
        SparseInstance instance = new SparseInstance(5, "ClassValue");
        assertEquals(5, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals("ClassValue", instance.classValue());
        for (int i = 0; i < 5; i++) {
            assertEquals(0.0, instance.get(i));
        }
    }

    @Test
    public void testSparseInstanceDataVectorConstruction() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < 3; i++) {
            assertEquals(dataVector[i], instance.get(i));
        }
    }

    @Test
    public void testSparseInstanceDataVectorDefaultValueConstruction() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector, 0.5);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < 3; i++) {
            if (dataVector[i] != 0.5) {
                assertEquals(dataVector[i], instance.get(i));
            } else {
                assertEquals(0.5, instance.get(i));
            }
        }
    }

    @Test
    public void testSparseInstanceDataVectorDefaultValueAndClassValueConstruction() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector, 0.5, "ClassValue");
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals("ClassValue", instance.classValue());
        for (int i = 0; i < 3; i++) {
            if (dataVector[i] != 0.5) {
                assertEquals(dataVector[i], instance.get(i));
            } else {
                assertEquals(0.5, instance.get(i));
            }
        }
    }

    @Test
    public void testSparseInstanceValue() {
        Double value = 4.5;
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), value);
        assertEquals(value, instance.value(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSparseInstanceOutOfBoundsValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.value(-1);
    }

    @Test
    public void testSparseInstanceClear() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceContainsKey() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        assertTrue(instance.containsKey(Integer.valueOf(0)));
        assertTrue(instance.containsKey(Integer.valueOf(1)));
        assertFalse(instance.containsKey(Integer.valueOf(2)));
    }

    @Test
    public void testSparseInstanceContainsValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testSparseInstanceEntrySet() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertTrue(entrySet.size() == 2);
    }

    @Test
    public void testSparseInstanceGet() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(0.0, instance.get(2));
    }

    @Test
    public void testSparseInstanceIsEmpty() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceKeySet() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        Set<Integer> keySet = instance.keySet();
        assertTrue(keySet.size() == 2);
    }

    @Test
    public void testSparseInstancePut() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        Double value = 4.5;
        instance.put(Integer.valueOf(0), value);
        assertEquals(value, instance.get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testSparseInstancePutNullKey() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(null, 1.0);
    }

    @Test(expected = NullPointerException.class)
    public void testSparseInstancePutNullValue() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), null);
    }

    @Test
    public void testSparseInstancePutAll() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), 1.0);
        map.put(Integer.valueOf(1), 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
    }

    @Test
    public void testSparseInstanceRemove() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        Double removedValue = instance.remove(Integer.valueOf(0));
        assertEquals(1.0, removedValue);
        assertEquals(2.0, instance.get(0));
    }

    @Test
    public void testSparseInstanceRemoveAttribute() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
    }

    @Test
    public void testSparseInstanceToString() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        String toString = instance.toString();
        assertTrue(toString.contains("{"));
        assertTrue(toString.contains(";"));
        assertTrue(toString.contains("ClassValue") || toString.isEmpty());
    }

    @Test
    public void testSparseInstanceHashCode() {
        SparseInstance instance1 = new SparseInstance(5, 0.0);
        instance1.put(Integer.valueOf(0), 1.0);
        instance1.put(Integer.valueOf(1), 2.0);

        SparseInstance instance2 = new SparseInstance(5, 0.0);
        instance2.put(Integer.valueOf(0), 1.0);
        instance2.put(Integer.valueOf(1), 2.0);

        SparseInstance instance3 = new SparseInstance(5, 0.0);
        instance3.put(Integer.valueOf(1), 2.0);
        instance3.put(Integer.valueOf(2), 3.0);

        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testSparseInstanceEquals() {
        SparseInstance instance1 = new SparseInstance(5, 0.0);
        instance1.put(Integer.valueOf(0), 1.0);
        instance1.put(Integer.valueOf(1), 2.0);

        SparseInstance instance2 = new SparseInstance(5, 0.0);
        instance2.put(Integer.valueOf(0), 1.0);
        instance2.put(Integer.valueOf(1), 2.0);

        SparseInstance instance3 = new SparseInstance(5, 0.0);
        instance3.put(Integer.valueOf(1), 2.0);
        instance3.put(Integer.valueOf(2), 3.0);

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testSparseInstanceCopy() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        SparseInstance copy = instance.copy();
        assertEquals(instance.hashCode(), copy.hashCode());
        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.getClassValue(), copy.getClassValue());
        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.get(i), copy.get(i));
        }
    }

    @Test
    public void testSparseInstanceRemoveAttributes() {
        SparseInstance instance = new SparseInstance(5, 0.0);
        instance.put(Integer.valueOf(0), 1.0);
        instance.put(Integer.valueOf(1), 2.0);
        Set<Integer> indices = new HashSet<>();
        indices.add(0);
        instance.removeAttributes(indices);
        assertEquals(4, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
    }
}