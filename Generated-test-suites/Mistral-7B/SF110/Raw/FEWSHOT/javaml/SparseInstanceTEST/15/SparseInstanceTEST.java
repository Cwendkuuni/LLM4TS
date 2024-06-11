package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceDefaultConstructor() {
        SparseInstance sparseInstance = new SparseInstance();
        assertEquals(-1, sparseInstance.noAttributes());
        assertNull(sparseInstance.getClassValue());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance(0);
        assertEquals(0, sparseInstance.noAttributes());
        assertNull(sparseInstance.getClassValue());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance sparseInstance = new SparseInstance(0, 0.0);
        assertEquals(0, sparseInstance.noAttributes());
        assertNull(sparseInstance.getClassValue());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithClassValue() {
        SparseInstance sparseInstance = new SparseInstance(0, "ClassValue");
        assertEquals(0, sparseInstance.noAttributes());
        assertEquals("ClassValue", sparseInstance.getClassValue());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndData() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(3, sparseInstance.noAttributes());
        assertNull(sparseInstance.getClassValue());
        assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDefaultValue() {
        double[] datavector = {};
        SparseInstance sparseInstance = new SparseInstance(datavector, 0.0);
        assertEquals(0, sparseInstance.noAttributes());
        assertNull(sparseInstance.getClassValue());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndClassValue() {
        double[] datavector = {};
        SparseInstance sparseInstance = new SparseInstance(datavector, "ClassValue");
        assertEquals(0, sparseInstance.noAttributes());
        assertEquals("ClassValue", sparseInstance.getClassValue());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDataAndDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0, 0.0};
        SparseInstance sparseInstance = new SparseInstance(datavector, 0.0);
        assertEquals(4, sparseInstance.noAttributes());
        assertNull(sparseInstance.getClassValue());
        assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithNoAttributesAndDataAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0, 0.0};
        SparseInstance sparseInstance = new SparseInstance(datavector, "ClassValue");
        assertEquals(4, sparseInstance.noAttributes());
        assertEquals("ClassValue", sparseInstance.getClassValue());
        assertFalse(sparseInstance.isEmpty());
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance(0);
        sparseInstance.setNoAttributes(5);
        assertEquals(5, sparseInstance.noAttributes());
    }

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(1.0, sparseInstance.value(0), 0.001);
        assertEquals(2.0, sparseInstance.value(1), 0.001);
        assertEquals(3.0, sparseInstance.value(2), 0.001);
        assertEquals(0.0, sparseInstance.value(3), 0.001);
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        sparseInstance.clear();
        assertEquals(0, sparseInstance.noAttributes());
        assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
        assertTrue(sparseInstance.containsKey(Integer.valueOf(1)));
        assertTrue(sparseInstance.containsKey(Integer.valueOf(2)));
        assertFalse(sparseInstance.containsKey(Integer.valueOf(3)));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
        assertTrue(sparseInstance.containsValue(Double.valueOf(2.0)));
        assertTrue(sparseInstance.containsValue(Double.valueOf(3.0)));
        assertFalse(sparseInstance.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
        assertEquals(3, entries.size());
        Map.Entry<Integer, Double> entry = entries.iterator().next();
        assertEquals(Integer.valueOf(0), entry.getKey());
        assertEquals(Double.valueOf(1.0), entry.getValue());
    }

    @Test
    public void testGet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
        assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
        assertEquals(3.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
        assertEquals(0.0, sparseInstance.get(Integer.valueOf(3)), 0.001);
        assertEquals(0.0, sparseInstance.get(null), 0.001);
    }

    @Test
    public void testIsEmpty() {
        double[] datavector = {};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertTrue(sparseInstance.isEmpty());
        double[] datavectorWithData = {1.0, 2.0, 3.0};
        SparseInstance sparseInstanceWithData = new SparseInstance(datavectorWithData);
        assertFalse(sparseInstanceWithData.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        Set<Integer> keys = sparseInstance.keySet();
        assertEquals(3, keys.size());
        assertTrue(keys.contains(Integer.valueOf(0)));
        assertTrue(keys.contains(Integer.valueOf(1)));
        assertTrue(keys.contains(Integer.valueOf(2)));
    }

    @Test
    public void testPut() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        assertEquals(2, sparseInstance.noAttributes());
        assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
        assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
        assertEquals(0.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
    }

    @Test
    public void testPutAll() {
        SparseInstance sparseInstance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        sparseInstance.putAll(map);
        assertEquals(2, sparseInstance.noAttributes());
        assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.001);
        assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
        assertEquals(0.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
    }

    @Test
    public void testRemove() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        Double removedValue = sparseInstance.remove(Integer.valueOf(0));
        assertEquals(1.0, removedValue.doubleValue(), 0.001);
        assertEquals(1, sparseInstance.noAttributes());
        assertFalse(sparseInstance.containsKey(Integer.valueOf(0)));
        assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
    }

    @Test
    public void testSize() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(3, sparseInstance.noAttributes());
        assertEquals(3, sparseInstance.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeDeprecated() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        sparseInstance.size();
    }

    @Test
    public void testValues() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        Collection<Double> values = sparseInstance.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(Double.valueOf(1.0)));
        assertTrue(values.contains(Double.valueOf(2.0)));
        assertTrue(values.contains(Double.valueOf(3.0)));
    }

    @Test
    public void testNoAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        assertEquals(3, sparseInstance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        sparseInstance.removeAttribute(0);
        assertEquals(2, sparseInstance.noAttributes());
        assertFalse(sparseInstance.containsKey(Integer.valueOf(0)));
        assertEquals(1.0, sparseInstance.get(Integer.valueOf(1)), 0.001);
        assertEquals(2.0, sparseInstance.get(Integer.valueOf(2)), 0.001);
    }

    @Test
    public void testToString() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        String toString = sparseInstance.toString();
        String expected = "{" + datavector.toString() + ";" + sparseInstance.getClassValue() + "}";
        assertEquals(expected, toString);
    }

    @Test
    public void testHashCode() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        int hashCode = sparseInstance.hashCode();
        int expectedHashCode = 31 * ((datavector == null ? 0 : datavector.hashCode()) ^ (datavector.hashCode() >>> 32))
                + (int) (Double.doubleToLongBits(sparseInstance.defaultValue) ^ (Double.doubleToLongBits(sparseInstance.defaultValue) >>> 32));
        assertEquals(expectedHashCode, hashCode);
    }

    @Test
    public void testEquals() {
        double[] datavector1 = {1.0, 2.0, 3.0};
        double[] datavector2 = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance1 = new SparseInstance(datavector1);
        SparseInstance sparseInstance2 = new SparseInstance(datavector2);
        assertTrue(sparseInstance1.equals(sparseInstance2));
        SparseInstance sparseInstance3 = new SparseInstance(datavector1);
        sparseInstance3.put(Integer.valueOf(0), Double.valueOf(4.0));
        assertFalse(sparseInstance1.equals(sparseInstance3));
        SparseInstance sparseInstance4 = new Object() {};
        assertFalse(sparseInstance1.equals(sparseInstance4));
    }

    @Test
    public void testCopy() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        SparseInstance copy = sparseInstance.copy();
        assertEquals(3, copy.noAttributes());
        assertEquals(1.0, copy.get(Integer.valueOf(0)), 0.001);
        assertEquals(2.0, copy.get(Integer.valueOf(1)), 0.001);
        assertEquals(3.0, copy.get(Integer.valueOf(2)), 0.001);
        assertFalse(copy.equals(sparseInstance));
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(2);
        sparseInstance.removeAttributes(indices);
        assertEquals(1, sparseInstance.noAttributes());
        assertFalse(sparseInstance.containsKey(Integer.valueOf(1)));
        assertFalse(sparseInstance.containsKey(Integer.valueOf(2)));
        assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
    }
}