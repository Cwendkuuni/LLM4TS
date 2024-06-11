package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceEmptyConstruction() {
        SparseInstance emptyInstance = new SparseInstance();
        assertEquals(0, emptyInstance.noAttributes());
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceDefaultConstruction() {
        SparseInstance defaultInstance = new SparseInstance();
        defaultInstance.setDefaultValue(1.0);
        assertEquals(0, defaultInstance.noAttributes());
        assertTrue(defaultInstance.isEmpty());
        assertEquals(1.0, defaultInstance.get(null));
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance instance = new SparseInstance(3);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(3, 1.0);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(null));
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        SparseInstance instance = new SparseInstance(3, 1.0, "testClass");
        assertEquals("testClass", instance.getClassValue());
    }

    @Test
    public void testSparseInstanceWithDoubleVector() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(4, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(3.0, instance.get(2));
        assertEquals(4.0, instance.get(3));
        assertEquals(0.0, instance.get(4));
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance(3);
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(1.0, instance.value(0));
        assertEquals(2.0, instance.value(1));
        assertEquals(3.0, instance.value(2));
        assertEquals(4.0, instance.value(3));
        assertEquals(0.0, instance.value(4));
        SparseInstance defaultInstance = new SparseInstance(1);
        assertEquals(0.0, defaultInstance.value(0));
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertTrue(instance.containsKey(3));
        assertFalse(instance.containsKey(4));
        SparseInstance emptyInstance = new SparseInstance();
        assertFalse(emptyInstance.containsKey(0));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsValue(1.0));
        assertTrue(instance.containsValue(2.0));
        assertTrue(instance.containsValue(3.0));
        assertTrue(instance.containsValue(4.0));
        assertFalse(instance.containsValue(0.5));
        SparseInstance defaultInstance = new SparseInstance(1);
        assertTrue(defaultInstance.containsValue(0.0));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        int index = 0;
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(index++, entry.getKey().intValue());
            assertEquals(datavector[index], entry.getValue().doubleValue());
        }
    }

    @Test
    public void testGet() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(3.0, instance.get(2));
        assertEquals(4.0, instance.get(3));
        assertEquals(0.0, instance.get(4));
        SparseInstance defaultInstance = new SparseInstance(1);
        assertEquals(0.0, defaultInstance.get(0));
    }

    @Test
    public void testIsEmpty() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
        SparseInstance emptyInstance = new SparseInstance();
        assertTrue(emptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> keySet = instance.keySet();
        int index = 0;
        for (Integer key : keySet) {
            assertEquals(index++, key.intValue());
        }
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(1));
        assertEquals(2.0, instance.get(2));
        assertEquals(0.0, instance.get(0));
        instance.put(0, 0.5);
        assertEquals(0.5, instance.get(0));
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 0.5);
        map.put(1, 1.0);
        map.put(2, 2.0);
        SparseInstance instance = new SparseInstance();
        instance.putAll(map);
        assertEquals(3, instance.noAttributes());
        assertEquals(0.5, instance.get(0));
        assertEquals(1.0, instance.get(1));
        assertEquals(2.0, instance.get(2));
        assertEquals(0.0, instance.get(3));
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.put(3, 3.0);
        Double removedValue = instance.remove(2);
        assertEquals(2.0, removedValue);
        assertEquals(2, instance.noAttributes());
        assertNull(instance.get(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeDeprecated() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.put(3, 3.0);
        int size = instance.size();
    }

    @Test
    public void testValues() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        Collection<Double> values = instance.values();
        int index = 0;
        for (Double value : values) {
            assertEquals(datavector[index++], value);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(4, instance.noAttributes());
        instance.removeAttribute(0);
        assertEquals(3, instance.noAttributes());
        instance.removeAttribute(2);
        assertEquals(2, instance.noAttributes());
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.removeAttribute(1);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.get(1));
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        assertNull(instance.get(0));
    }

    @Test
    public void testToString() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        String expected = "{" + Arrays.toString(datavector) + ";testClass}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance1 = new SparseInstance(datavector);
        SparseInstance instance2 = new SparseInstance(datavector);
        SparseInstance instance3 = new SparseInstance();
        instance1.setClassValue("testClass1");
        instance2.setClassValue("testClass2");
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] datavector1 = {1.0, 2.0, 3.0, 4.0};
        double[] datavector2 = {1.0, 2.0, 3.0, 4.0};
        double[] datavector3 = {1.0, 2.1, 3.0, 4.0};
        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance(datavector2);
        SparseInstance instance3 = new SparseInstance(datavector3);
        SparseInstance instance4 = new SparseInstance(datavector1);
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance4));
        assertFalse(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        SparseInstance copy = instance.copy();
        assertEquals(datavector.length, copy.noAttributes());
        for (int i = 0; i < datavector.length; ++i) {
            assertEquals(datavector[i], copy.get(i));
        }
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
        assertNull(instance.get(1));
        assertNull(instance.get(3));
    }
}