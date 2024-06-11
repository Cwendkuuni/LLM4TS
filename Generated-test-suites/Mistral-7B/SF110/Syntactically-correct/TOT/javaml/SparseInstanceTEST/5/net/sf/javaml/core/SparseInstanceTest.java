package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance emptySparseInstance = new SparseInstance();
        Assert.assertEquals(0, emptySparseInstance.noAttributes());
        Assert.assertTrue(emptySparseInstance.isEmpty());
        Assert.assertNull(emptySparseInstance.getValue(0));
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance sparseInstance = new SparseInstance(5, 0.0);
        Assert.assertEquals(5, sparseInstance.noAttributes());
        Assert.assertTrue(sparseInstance.isEmpty());
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(0.0, sparseInstance.getValue(i), 0.0);
        }
    }

    @Test
    public void testSparseInstanceWithData() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        Assert.assertEquals(2, sparseInstance.noAttributes());
        Assert.assertFalse(sparseInstance.isEmpty());
        Assert.assertEquals(1.0, sparseInstance.getValue(0), 0.0);
        Assert.assertEquals(2.0, sparseInstance.getValue(1), 0.0);
        Assert.assertNull(sparseInstance.getValue(2));
    }

    @Test
    public void testSparseInstanceWithInvalidKey() {
        SparseInstance sparseInstance = new SparseInstance();
        try {
            sparseInstance.put(null, Double.valueOf(3.0));
            Assert.fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSparseInstanceWithNegativeNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        try {
            sparseInstance.setNoAttributes(-1);
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSparseInstanceWithNullClassValue() {
        SparseInstance sparseInstance = new SparseInstance();
        try {
            sparseInstance.setClassValue(null);
            Assert.fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSparseInstanceWithDuplicateKeys() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        try {
            sparseInstance.put(Integer.valueOf(0), Double.valueOf(2.0));
            Assert.fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSparseInstanceClear() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        sparseInstance.clear();
        Assert.assertEquals(0, sparseInstance.noAttributes());
        Assert.assertTrue(sparseInstance.isEmpty());
        for (int i = 0; i < 5; i++) {
            Assert.assertEquals(0.0, sparseInstance.getValue(i), 0.0);
        }
    }

    @Test
    public void testSparseInstancePutAll() {
        SparseInstance sparseInstance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        sparseInstance.putAll(map);
        Assert.assertEquals(2, sparseInstance.noAttributes());
        Assert.assertFalse(sparseInstance.isEmpty());
        Assert.assertEquals(1.0, sparseInstance.getValue(0), 0.0);
        Assert.assertEquals(2.0, sparseInstance.getValue(1), 0.0);
        Assert.assertNull(sparseInstance.getValue(2));
    }

    @Test
    public void testSparseInstanceContainKeyAndValue() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        Assert.assertTrue(sparseInstance.containsKey(Integer.valueOf(0)));
        Assert.assertTrue(sparseInstance.containsValue(Double.valueOf(1.0)));
        Assert.assertFalse(sparseInstance.containsKey(Integer.valueOf(1)));
        Assert.assertFalse(sparseInstance.containsValue(Double.valueOf(2.0)));
    }

    @Test
    public void testSparseInstanceEntrySet() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        Set<Map.Entry<Integer, Double>> entries = sparseInstance.entrySet();
        Assert.assertEquals(2, entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            Assert.assertEquals(entry.getKey(), entry.getValue().getKey());
            Assert.assertEquals(entry.getValue().getValue(), entry.getValue());
        }
    }

    @Test
    public void testSparseInstanceGet() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        Assert.assertEquals(1.0, sparseInstance.get(Integer.valueOf(0)), 0.0);
        Assert.assertEquals(2.0, sparseInstance.get(Integer.valueOf(1)), 0.0);
        Assert.assertEquals(0.0, sparseInstance.get(Integer.valueOf(2)), 0.0);
    }

    @Test
    public void testSparseInstanceNoAttributes() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        Assert.assertEquals(2, sparseInstance.noAttributes());
        sparseInstance.removeAttribute(0);
        Assert.assertEquals(1, sparseInstance.noAttributes());
        sparseInstance.removeAttribute(1);
        Assert.assertEquals(0, sparseInstance.noAttributes());
        Assert.assertTrue(sparseInstance.isEmpty());
    }

    @Test
    public void testSparseInstanceRemoveAttribute() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
        sparseInstance.removeAttribute(1);
        Assert.assertEquals(3, sparseInstance.noAttributes());
        Assert.assertFalse(sparseInstance.isEmpty());
        Assert.assertEquals(1.0, sparseInstance.getValue(0), 0.0);
        Assert.assertEquals(3.0, sparseInstance.getValue(2), 0.0);
        Assert.assertNull(sparseInstance.getValue(1));
    }

    @Test
    public void testSparseInstanceRemoveAttributesBySet() {
        SparseInstance sparseInstance = new SparseInstance();
        sparseInstance.put(Integer.valueOf(0), Double.valueOf(1.0));
        sparseInstance.put(Integer.valueOf(1), Double.valueOf(2.0));
        sparseInstance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2));
        sparseInstance.removeAttributes(indices);
        Assert.assertEquals(1, sparseInstance.noAttributes());
        Assert.assertTrue(sparseInstance.isEmpty());
        Assert.assertEquals(1.0, sparseInstance.getValue(0), 0.0);
        Assert.assertNull(sparseInstance.getValue(1));
        Assert.assertNull(sparseInstance.getValue(2));
    }
}