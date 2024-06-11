package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance emptySparseInstance = new SparseInstance();
        Assert.assertEquals(0, emptySparseInstance.noAttributes());
        Assert.assertTrue(emptySparseInstance.isEmpty());
        Assert.assertFalse(emptySparseInstance.containsKey(1));
        Assert.assertEquals(0.0, emptySparseInstance.getDefaultValue(), 0.0);
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        Assert.assertEquals(10, sparseInstance.noAttributes());
        Assert.assertFalse(sparseInstance.isEmpty());
        Assert.assertFalse(sparseInstance.containsKey(11));
        Assert.assertEquals(3.0, sparseInstance.get(0), 3.0);
        Assert.assertEquals(3.0, sparseInstance.value(0));
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance sparseInstance = new SparseInstance(datavector);
        Assert.assertEquals(datavector.length, sparseInstance.noAttributes());
        Assert.assertFalse(sparseInstance.isEmpty());
        Assert.assertEquals(1.0, sparseInstance.get(0), 1.0);
        Assert.assertEquals(1.0, sparseInstance.value(0));
        Assert.assertFalse(sparseInstance.containsKey(5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfAttributes() {
        new SparseInstance(-2);
    }

    @Test(expected = NullPointerException.class)
    public void testNullClassValue() {
        new SparseInstance(10, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullData() {
        new SparseInstance(null);
    }

    @Test
    public void testClear() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        sparseInstance.put(Integer.valueOf(5), Double.valueOf(12.5));
        sparseInstance.clear();
        Assert.assertTrue(sparseInstance.isEmpty());
        Assert.assertEquals(0, sparseInstance.noAttributes());
        Assert.assertFalse(sparseInstance.containsKey(5));
    }

    @Test
    public void testPut() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        sparseInstance.put(Integer.valueOf(5), Double.valueOf(12.5));
        Assert.assertEquals(12.5, sparseInstance.get(5), 12.5);
        Assert.assertEquals(12.5, sparseInstance.value(5));
        Assert.assertFalse(sparseInstance.containsKey(11));
        Assert.assertEquals(11, sparseInstance.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutKeyNull() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        sparseInstance.put(null, Double.valueOf(12.5));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutValueNull() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        sparseInstance.put(Integer.valueOf(5), null);
    }

    @Test
    public void testPutAll() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(5), Double.valueOf(12.5));
        map.put(Integer.valueOf(11), Double.valueOf(25.0));
        sparseInstance.putAll(map);
        Assert.assertEquals(map.size(), sparseInstance.noAttributes());
        Assert.assertEquals(12.5, sparseInstance.get(5), 12.5);
        Assert.assertEquals(25.0, sparseInstance.get(11), 25.0);
    }

    @Test(expected = NullPointerException.class)
    public void testPutAllNullMap() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        sparseInstance.putAll(null);
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        sparseInstance.put(Integer.valueOf(5), Double.valueOf(12.5));
        sparseInstance.removeAttribute(5);
        Assert.assertFalse(sparseInstance.containsKey(5));
        Assert.assertEquals(9, sparseInstance.noAttributes());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttributeInvalidIndex() {
        SparseInstance sparseInstance = new SparseInstance(10, 3.0);
        sparseInstance.removeAttribute(-1);
    }

    @Test
    public void testToString() {
        SparseInstance sparseInstance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        String expected = "{" + Arrays.toString(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}) + ";" + sparseInstance.getClass().getDeclaredField("classValue").get(sparseInstance) + "}";
        Assert.assertEquals(expected, sparseInstance.toString());
    }
}