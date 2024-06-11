package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithEmptyAttributeArray() {
        DenseInstance denseInstance = new DenseInstance();
        assertEquals(0, denseInstance.noAttributes());
        assertTrue(denseInstance.isEmpty());
        assertNull(denseInstance.classValue());
        assertEquals(0, denseInstance.size());
    }

    @Test
    public void testDenseInstanceWithSingleAttribute() {
        double attribute = 1.0;
        DenseInstance denseInstance = new DenseInstance(new double[] { attribute });
        assertEquals(1, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        assertEquals(attribute, denseInstance.value(0));
        assertEquals(attribute, denseInstance.get(0));
        assertEquals("{" + attribute + "}", denseInstance.toString());
    }

    @Test
    public void testDenseInstanceWithMultipleAttributes() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], denseInstance.value(i));
            assertEquals(attributes[i], denseInstance.get(i));
        }
        assertEquals("{" + Arrays.toString(attributes) + "}", denseInstance.toString());
    }

    @Test
    public void testDenseInstanceWithClassValue() {
        double attribute = 1.0;
        Object classValue = "Class1";
        DenseInstance denseInstance = new DenseInstance(new double[] { attribute }, classValue);
        assertEquals(1, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        assertEquals(attribute, denseInstance.value(0));
        assertEquals(attribute, denseInstance.get(0));
        assertEquals(classValue, denseInstance.classValue());
    }

    @Test
    public void testClear() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.clear();
        assertEquals(0, denseInstance.noAttributes());
        assertTrue(denseInstance.isEmpty());
        assertNull(denseInstance.classValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttribute() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        denseInstance.removeAttribute(0);
    }

    @Test
    public void testContainsKey() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(-1));
        assertFalse(denseInstance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] attributes = { 1.0, 2.0, 3.0, 1.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertTrue(denseInstance.containsValue(1.0));
        assertFalse(denseInstance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Map.Entry<Integer, Double>> entries = denseInstance.entrySet();
        assertEquals(3, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Test
    public void testGet() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        for (int i = 0; i < attributes.length; ++i) {
            assertEquals(attributes[i], denseInstance.get(i));
        }
    }

    @Test
    public void testIsEmpty() {
        DenseInstance denseInstance = new DenseInstance();
        assertTrue(denseInstance.isEmpty());
        double[] attributes = { 1.0 };
        DenseInstance notEmptyInstance = new DenseInstance(attributes);
        assertFalse(notEmptyInstance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        SortedSet<Integer> keys = denseInstance.keySet();
        assertEquals(3, keys.size());
        for (int i = 0; i < attributes.length; ++i) {
            assertTrue(keys.contains(i));
        }
    }

    @Test
    public void testPut() {
        double[] attributes = { 1.0, 2.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        Double oldValue = denseInstance.put(2, 3.0);
        assertEquals(1.0, oldValue.doubleValue());
        assertEquals(3.0, denseInstance.get(2));
    }

    @Test
    public void testPutAll() {
        double[] attributes1 = { 1.0, 2.0 };
        double[] attributes2 = { 3.0, 4.0 };
        DenseInstance denseInstance = new DenseInstance(attributes1);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(2, 3.0);
        map.put(3, 4.0);
        denseInstance.putAll(map);
        assertEquals(4, denseInstance.noAttributes());
        for (int i = 0; i < attributes1.length; ++i) {
            assertEquals(attributes1[i], denseInstance.value(i));
        }
        assertEquals(3.0, denseInstance.value(2));
        assertEquals(4.0, denseInstance.value(3));
    }

    @Test
    public void testRemoveAttributes() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<Integer>();
        indicesToRemove.add(1);
        indicesToRemove.add(2);
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(1, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        assertNull(denseInstance.classValue());
    }

    @Test
    public void testSize() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.size());
    }

    @Test
    public void testValues() {
        double[] attributes = { 1.0, 2.0, 3.0 };
        DenseInstance denseInstance = new DenseInstance(attributes);
        Collection<Double> values = denseInstance.values();
        assertEquals(3, values.size());
        for (Double value : values) {
            assertEquals(value.doubleValue(), attributes[value.intValue()]);
        }
    }

    @Test
    public void testNoAttributes() {
        DenseInstance denseInstance = new DenseInstance();
        assertEquals(0, denseInstance.noAttributes());
        double[] attributes = { 1.0 };
        DenseInstance instanceWithAttributes = new DenseInstance(attributes);
        assertEquals(1, instanceWithAttributes.noAttributes());
    }
}