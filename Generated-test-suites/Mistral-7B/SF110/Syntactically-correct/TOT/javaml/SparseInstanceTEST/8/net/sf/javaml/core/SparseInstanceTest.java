package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance inst = new SparseInstance();
        Assert.assertEquals(0, inst.noAttributes());
        Assert.assertFalse(inst.containsKey(0));
        Assert.assertTrue(inst.isEmpty());
        Assert.assertEquals(0.0, inst.defaultValue);
    }

    @Test
    public void testSettingNoAttributes() {
        SparseInstance inst = new SparseInstance();
        inst.setNoAttributes(5);
        Assert.assertEquals(5, inst.noAttributes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNoAttributes() {
        SparseInstance inst = new SparseInstance();
        inst.setNoAttributes(-1);
    }

    @Test
    public void testSparseInstanceWithDefaultValue() {
        SparseInstance inst = new SparseInstance(3);
        Assert.assertEquals(3, inst.noAttributes());
        Assert.assertFalse(inst.isEmpty());
        Assert.assertEquals(0.0, inst.defaultValue);
        Assert.assertFalse(inst.containsKey(0));
        Assert.assertFalse(inst.containsKey(1));
        Assert.assertFalse(inst.containsKey(2));
    }

    @Test
    public void testSparseInstanceWithValues() {
        SparseInstance inst = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        Assert.assertEquals(4, inst.noAttributes());
        Assert.assertFalse(inst.isEmpty());
        Assert.assertEquals(0.0, inst.defaultValue);
        Assert.assertTrue(inst.containsKey(0));
        Assert.assertEquals(1.0, inst.get(0), 0.001);
        Assert.assertTrue(inst.containsKey(1));
        Assert.assertEquals(2.0, inst.get(1), 0.001);
        Assert.assertTrue(inst.containsKey(2));
        Assert.assertEquals(3.0, inst.get(2), 0.001);
        Assert.assertTrue(inst.containsKey(3));
        Assert.assertEquals(4.0, inst.get(3), 0.001);
    }

    @Test
    public void testSettingValues() {
        SparseInstance inst = new SparseInstance(3, new Object() {});
        inst.put(Integer.valueOf(0), Double.valueOf(1.0));
        inst.put(Integer.valueOf(1), Double.valueOf(2.0));
        inst.put(Integer.valueOf(2), Double.valueOf(3.0));
        Assert.assertEquals(3, inst.noAttributes());
        Assert.assertFalse(inst.isEmpty());
        Assert.assertEquals(0.0, inst.defaultValue);
        Assert.assertTrue(inst.containsKey(0));
        Assert.assertEquals(1.0, inst.get(0), 0.001);
        Assert.assertTrue(inst.containsKey(1));
        Assert.assertEquals(2.0, inst.get(1), 0.001);
        Assert.assertTrue(inst.containsKey(2));
        Assert.assertEquals(3.0, inst.get(2), 0.001);
    }

    @Test
    public void testSettingValuesWithDuplicateKey() {
        SparseInstance inst = new SparseInstance(3, new Object() {});
        inst.put(Integer.valueOf(0), Double.valueOf(1.0));
        inst.put(Integer.valueOf(0), Double.valueOf(2.0));
        Assert.assertEquals(3, inst.noAttributes());
        Assert.assertFalse(inst.isEmpty());
        Assert.assertEquals(0.0, inst.defaultValue);
        Assert.assertTrue(inst.containsKey(0));
        Assert.assertEquals(2.0, inst.get(0), 0.001); // the value 1.0 was replaced
    }

    @Test
    public void testSettingValuesWithNullKey() {
        SparseInstance inst = new SparseInstance(3, new Object() {});
        Assert.assertNull(inst.put(null, Double.valueOf(1.0)));
        Assert.assertFalse(inst.containsKey(null));
    }

    @Test
    public void testSettingValuesWithNullValue() {
        SparseInstance inst = new SparseInstance(3, new Object() {});
        inst.put(Integer.valueOf(0), null);
        Assert.assertEquals(0.0, inst.get(0));
    }

    @Test
    public void testClearingInstance() {
        SparseInstance inst = new SparseInstance(3, new Object() {});
        inst.put(Integer.valueOf(0), Double.valueOf(1.0));
        inst.put(Integer.valueOf(1), Double.valueOf(2.0));
        inst.put(Integer.valueOf(2), Double.valueOf(3.0));
        inst.clear();
        Assert.assertEquals(3, inst.noAttributes());
        Assert.assertTrue(inst.isEmpty());
        Assert.assertEquals(0.0, inst.defaultValue);
        Assert.assertFalse(inst.containsKey(0));
        Assert.assertFalse(inst.containsKey(1));
        Assert.assertFalse(inst.containsKey(2));
    }

    @Test
    public void testSettingDefaultValue() {
        SparseInstance inst = new SparseInstance(3, new Object() {}, 4.0);
        Assert.assertEquals(3, inst.noAttributes());
        Assert.assertFalse(inst.isEmpty());
        Assert.assertEquals(4.0, inst.defaultValue);
        Assert.assertFalse(inst.containsKey(0));
        Assert.assertFalse(inst.containsKey(1));
        Assert.assertFalse(inst.containsKey(2));
    }

    @Test
    public void testRemovingAttribute() {
        SparseInstance inst = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        inst.removeAttribute(1);
        Assert.assertEquals(2, inst.noAttributes());
        Assert.assertFalse(inst.isEmpty());
        Assert.assertEquals(0.0, inst.defaultValue);
        Assert.assertTrue(inst.containsKey(0));
        Assert.assertEquals(1.0, inst.get(0), 0.001);
        Assert.assertFalse(inst.containsKey(1));
        Assert.assertTrue(inst.containsKey(2));
        Assert.assertEquals(3.0, inst.get(2), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemovingAttributeOutOfBounds() {
        SparseInstance inst = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        inst.removeAttribute(4);
    }

    @Test
    public void testToString() {
        SparseInstance inst = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        String result = inst.toString();
        String expected = "{1.0=1.0, 2.0=2.0, 3.0=3.0}";
        Assert.assertEquals(expected, result);
    }
}