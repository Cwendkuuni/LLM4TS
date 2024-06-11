package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testSparseInstance() {
        SparseInstance si = new SparseInstance();
        assertEquals(-1, si.noAttributes());
        assertNull(si.get(0));
        
        si = new SparseInstance(5);
        assertEquals(5, si.noAttributes());
        assertNull(si.get(0));
        assertNull(si.get(1));
        assertNull(si.get(2));
        assertNull(si.get(3));
        assertNull(si.get(4));
        
        SparseInstance siWithData = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(5, siWithData.noAttributes());
        assertEquals(1.0, siWithData.get(0), 0.001);
        assertEquals(2.0, siWithData.get(1), 0.001);
        assertEquals(3.0, siWithData.get(2), 0.001);
        assertEquals(4.0, siWithData.get(3), 0.001);
        assertEquals(5.0, siWithData.get(4), 0.001);
        
        SparseInstance siWithDefaultValue = new SparseInstance(5, 0.5);
        assertEquals(5, siWithDefaultValue.noAttributes());
        assertNull(siWithDefaultValue.get(0));
        assertEquals(0.5, siWithDefaultValue.get(1), 0.001);
        assertNull(siWithDefaultValue.get(2));
        assertNull(siWithDefaultValue.get(3));
        assertNull(siWithDefaultValue.get(4));
        
        SparseInstance siWithClassValue = new SparseInstance(5, "ClassValue");
        assertEquals("ClassValue", siWithClassValue.getClassValue());
        assertEquals(5, siWithClassValue.noAttributes());
        assertNull(siWithClassValue.get(0));
        assertNull(siWithClassValue.get(1));
        assertNull(siWithClassValue.get(2));
        assertNull(siWithClassValue.get(3));
        assertNull(siWithClassValue.get(4));
        
        SparseInstance siWithDataAndClassValue = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, "ClassValue");
        assertEquals("ClassValue", siWithDataAndClassValue.getClassValue());
        assertEquals(5, siWithDataAndClassValue.noAttributes());
        assertEquals(1.0, siWithDataAndClassValue.get(0), 0.001);
        assertEquals(2.0, siWithDataAndClassValue.get(1), 0.001);
        assertEquals(3.0, siWithDataAndClassValue.get(2), 0.001);
        assertEquals(4.0, siWithDataAndClassValue.get(3), 0.001);
        assertEquals(5.0, siWithDataAndClassValue.get(4), 0.001);
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance si = new SparseInstance();
        si.setNoAttributes(5);
        assertEquals(5, si.noAttributes());
    }

    @Test
    public void testClear() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        si.clear();
        assertEquals(-1, si.noAttributes());
        assertNull(si.get(0));
        assertNull(si.get(1));
        assertNull(si.get(2));
        assertNull(si.get(3));
        assertNull(si.get(4));
    }

    @Test
    public void testContainsKey() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertTrue(si.containsKey(0));
        assertTrue(si.containsKey(1));
        assertTrue(si.containsKey(2));
        assertTrue(si.containsKey(3));
        assertTrue(si.containsKey(4));
        assertFalse(si.containsKey(5));
    }

    @Test
    public void testContainsValue() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertTrue(si.containsValue(1.0));
        assertTrue(si.containsValue(2.0));
        assertTrue(si.containsValue(3.0));
        assertTrue(si.containsValue(4.0));
        assertTrue(si.containsValue(5.0));
        assertFalse(si.containsValue(6.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Set<Map.Entry<Integer, Double>> set = si.entrySet();
        assertEquals(5, set.size());
        for (Map.Entry<Integer, Double> entry : set) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, entry.getKey());
            assertEquals(value, entry.getValue());
            assertEquals(value, si.get(key));
        }
    }

    @Test
    public void testGet() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        for (int i = 0; i < 5; i++) {
            assertEquals(Double.valueOf(si.get(i)), Double.valueOf(si.data.get(i)));
        }
    }

    @Test
    public void testIsEmpty() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertFalse(si.isEmpty());

        SparseInstance emptySi = new SparseInstance();
        assertTrue(emptySi.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Set<Integer> set = si.keySet();
        assertEquals(5, set.size());
        for (int key : set) {
            assertEquals(key, si.get(key).intValue());
        }
    }

    @Test
    public void testPut() {
        SparseInstance si = new SparseInstance();
        si.put(Integer.valueOf(0), Double.valueOf(1.0));
        si.put(Integer.valueOf(1), Double.valueOf(2.0));
        si.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(3, si.noAttributes());
        assertEquals(1.0, si.get(0), 0.001);
        assertEquals(2.0, si.get(1), 0.001);
        assertEquals(3.0, si.get(2), 0.001);
        assertNull(si.get(3));

        try {
            si.put(null, Double.valueOf(1.0));
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPutAll() {
        SparseInstance si = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        map.put(Integer.valueOf(2), Double.valueOf(3.0));
        si.putAll(map);
        assertEquals(3, si.noAttributes());
        assertEquals(1.0, si.get(0), 0.001);
        assertEquals(2.0, si.get(1), 0.001);
        assertEquals(3.0, si.get(2), 0.001);
        assertNull(si.get(3));
    }

    @Test
    public void testRemove() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Double removedValue = si.remove(1);
        assertEquals(4.0, removedValue, 0.001);
        assertEquals(4, si.noAttributes());
        assertNull(si.get(1));
    }

    @Test
    public void testSize() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(5, si.noAttributes());
        assertEquals(5, si.size());

        si.clear();
        assertEquals(0, si.size());
    }

    @Test
    public void testValues() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Collection<Double> values = si.values();
        assertEquals(5, values.size());
        for (Double value : values) {
            assertTrue(value instanceof Double);
            assertEquals(value, si.get(values.indexOf(value)));
        }
    }

    @Test
    public void testNoAttributes() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(5, si.noAttributes());

        SparseInstance siWithEmptyData = new SparseInstance();
        assertEquals(0, siWithEmptyData.noAttributes());

        SparseInstance siWithSingleAttribute = new SparseInstance(1, 1.0);
        assertEquals(1, siWithSingleAttribute.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        si.removeAttribute(2);
        assertEquals(4, si.noAttributes());
        assertNull(si.get(2));
    }

    @Test
    public void testToString() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        String toString = si.toString();
        assertTrue(toString.contains("{"));
        assertTrue(toString.contains(";"));
        assertTrue(toString.contains("ClassValue"));
        for (int i = 0; i < 5; i++) {
            String attribute = String.format("%d=%.4f", i, si.get(i));
            assertTrue(toString.contains(attribute));
        }
    }

    @Test
    public void testHashCode() {
        SparseInstance si1 = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        SparseInstance si2 = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(si1.hashCode(), si2.hashCode());

        SparseInstance si3 = new SparseInstance(5, new double[]{1.1, 2.0, 3.0, 4.0, 5.0});
        assertNotEquals(si1.hashCode(), si3.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance si1 = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        SparseInstance si2 = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertTrue(si1.equals(si2));

        SparseInstance si3 = new SparseInstance(5, new double[]{1.1, 2.0, 3.0, 4.0, 5.0});
        assertFalse(si1.equals(si3));

        SparseInstance si4 = new SparseInstance();
        assertFalse(si1.equals(si4));
    }

    @Test
    public void testCopy() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        SparseInstance copy = si.copy();
        assertEquals(si, copy);

        si.put(Integer.valueOf(5), Double.valueOf(6.0));
        assertNotEquals(si, copy);
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance si = new SparseInstance(5, new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        Set<Integer> indices = new HashSet<>();
        indices.add(2);
        indices.add(3);
        si.removeAttributes(indices);
        assertEquals(3, si.noAttributes());
        assertNull(si.get(2));
        assertNull(si.get(3));
    }
}