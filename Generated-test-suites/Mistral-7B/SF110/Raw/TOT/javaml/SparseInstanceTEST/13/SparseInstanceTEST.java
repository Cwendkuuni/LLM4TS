package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance instance = new SparseInstance();
        Assert.assertEquals(0, instance.noAttributes());
        Assert.assertTrue(instance.isEmpty());
        Assert.assertFalse(instance.containsKey(0));
        Assert.assertEquals(0.0, instance.value(0), 0.0);
    }

    @Test
    public void testSparseInstanceWithSingleAttribute() {
        SparseInstance instance = new SparseInstance(1);
        instance.put(0, 1.0);
        Assert.assertEquals(1, instance.noAttributes());
        Assert.assertFalse(instance.isEmpty());
        Assert.assertTrue(instance.containsKey(0));
        Assert.assertEquals(1.0, instance.value(0), 0.01);
        Assert.assertEquals(0.0, instance.value(1), 0.0);
    }

    @Test
    public void testSparseInstanceWithMultipleAttributes() {
        SparseInstance instance = new SparseInstance(3); {};
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Assert.assertEquals(3, instance.noAttributes());
        Assert.assertFalse(instance.isEmpty());
        Assert.assertTrue(instance.containsKey(0));
        Assert.assertTrue(instance.containsKey(1));
        Assert.assertTrue(instance.containsKey(2));
        Assert.assertEquals(1.0, instance.value(0), 0.01);
        Assert.assertEquals(2.0, instance.value(1), 0.01);
        Assert.assertEquals(3.0, instance.value(2), 0.01);
        Assert.assertEquals(0.0, instance.value(3), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfAttributes() {
        new SparseInstance(-1);
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance instance = new SparseInstance(3, 0.5); {};
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        Assert.assertEquals(3, instance.noAttributes());
        Assert.assertFalse(instance.isEmpty());
        Assert.assertTrue(instance.containsKey(0));
        Assert.assertTrue(instance.containsKey(1));
        Assert.assertTrue(instance.containsKey(2));
        Assert.assertEquals(0.5, instance.value(3), 0.0);
        Assert.assertEquals(1.0, instance.value(0), 0.01);
        Assert.assertEquals(2.0, instance.value(1), 0.01);
        Assert.assertEquals(3.0, instance.value(2), 0.01);
    }

    @Test
    public void testSparseInstanceWithDataVector() {
        double[] dataVector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(dataVector); {};
        instance.put(4, 4.0);
        Assert.assertEquals(4, instance.noAttributes());
        Assert.assertFalse(instance.isEmpty());
        Assert.assertTrue(instance.containsKey(0));
        Assert.assertTrue(instance.containsKey(1));
        Assert.assertTrue(instance.containsKey(2));
        Assert.assertTrue(instance.containsKey(3));
        Assert.assertEquals(1.0, instance.value(0), 0.01);
        Assert.assertEquals(2.0, instance.value(1), 0.01);
        Assert.assertEquals(3.0, instance.value(2), 0.01);
        Assert.assertEquals(0.0, instance.value(3), 0.0);
        Assert.assertEquals(4.0, instance.value(3), 0.01);
    }

    @Test
    public void testSparseInstanceClear() {
        SparseInstance instance = new SparseInstance(3) {};
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.clear();
        Assert.assertEquals(0, instance.noAttributes());
        Assert.assertTrue(instance.isEmpty());
        Assert.assertFalse(instance.containsKey(0));
        Assert.assertFalse(instance.containsKey(1));
        Assert.assertFalse(instance.containsKey(2));
    }

    @Test
    public void testSparseInstanceRemoveAttribute() {
        SparseInstance instance = new SparseInstance(3) {};
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttribute(1);
        Assert.assertEquals(2, instance.noAttributes());
        Assert.assertFalse(instance.isEmpty());
        Assert.assertTrue(instance.containsKey(0));
        Assert.assertFalse(instance.containsKey(1));
        Assert.assertTrue(instance.containsKey(2));
        Assert.assertEquals(1.0, instance.value(0), 0.01);
        Assert.assertEquals(3.0, instance.value(2), 0.01);
    }

    @Test
    public void testSparseInstanceRemoveAllAttributes() {
        SparseInstance instance = new SparseInstance(3) {};
        instance.put(0, 1.0);
        instance.put(1, 2.0);
        instance.put(2, 3.0);
        instance.removeAttributes(Arrays.asList(0, 1, 2));
        Assert.assertEquals(0, instance.noAttributes());
        Assert.assertTrue(instance.isEmpty());
        Assert.assertFalse(instance.containsKey(0));
        Assert.assertFalse(instance.containsKey(1));
        Assert.assertFalse(instance.containsKey(2));
    }
}