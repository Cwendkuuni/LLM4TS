package net.sf.javaml.core;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance instance = new SparseInstance();
        Assert.assertTrue(instance.isEmpty());
        Assert.assertEquals(0, instance.noAttributes());
        Assert.assertNull(instance.value(0));
    }

    @Test
    public void testDefaultValueSparseInstance() {
        SparseInstance instance = new SparseInstance(10);
        Assert.assertEquals(10, instance.noAttributes());
        Assert.assertEquals(0.0, instance.value(0), 0.001);
        Assert.assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstanceWithValues() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        Assert.assertEquals(4, instance.noAttributes());
        Assert.assertEquals(1.0, instance.value(0), 0.001);
        Assert.assertEquals(2.0, instance.value(1), 0.001);
        Assert.assertEquals(3.0, instance.value(2), 0.001);
        Assert.assertEquals(4.0, instance.value(3), 0.001);
        Assert.assertFalse(instance.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNumberOfAttributes() {
        new SparseInstance(-1);
    }

    @Test
    public void testSettingAndGettingNumberOfAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        Assert.assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testSettingAndGettingDefaultValue() {
        SparseInstance instance = new SparseInstance();
        instance.setDefaultValue(1.0);
        Assert.assertEquals(1.0, instance.defaultValue, 0.001);
    }

    @Test
    public void testSettingAndGettingClassValue() {
        SparseInstance instance = new SparseInstance();
        instance.setClassValue("TestClass");
        Assert.assertEquals("TestClass", instance.getClassValue());
    }

    @Test
    public void testGettingNonExistentAttributeValue() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        Assert.assertEquals(0.0, instance.value(5), 0.001);
    }

    @Test
    public void testSettingAndRemovingAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(5, 1.0);
        Assert.assertEquals(1.0, instance.get(5), 0.001);
        instance.removeAttribute(5);
        Assert.assertEquals(0.0, instance.get(5), 0.001);
    }

    @Test
    public void testSettingDuplicateAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(5, 1.0);
        instance.put(5, 2.0);
        Assert.assertEquals(1.0, instance.get(5), 0.001);
    }

    @Test
    public void testClearingAllAttributes() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0});
        instance.clear();
        Assert.assertTrue(instance.isEmpty());
        Assert.assertEquals(0, instance.noAttributes());
    }
}