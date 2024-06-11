package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testEmptySparseInstance() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
        assertTrue(instance.containsValue(instance.getDefaultValue()));
        assertNull(instance.get(Integer.valueOf(0)));
        assertTrue(instance.toString().contains("{}"));
    }

    @Test
    public void testDefaultValueSparseInstance() {
        SparseInstance instance = new SparseInstance(1);
        assertEquals(1, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
        assertEquals(instance.getDefaultValue(), instance.get(Integer.valueOf(0)), 0.0);
        assertFalse(instance.toString().contains("{}"));
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1.0, instance.get(Integer.valueOf(0)), 0.0);
        assertFalse(instance.containsValue(instance.getDefaultValue()));
        assertTrue(instance.toString().contains("{1=1.0}"));
        instance.clear();
        assertEquals(instance.getDefaultValue(), instance.get(Integer.valueOf(0)), 0.0);
        assertTrue(instance.toString().contains("{}"));
    }

    @Test
    public void testSparseInstanceWithData() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertFalse(instance.containsKey(Integer.valueOf(0)));
        assertEquals(instance.getDefaultValue(), instance.get(Integer.valueOf(0)), 0.0);
        assertFalse(instance.containsValue(instance.getDefaultValue()));
        assertTrue(instance.toString().contains("{1=1.0, 2=2.0, 3=3.0}"));
        instance.put(Integer.valueOf(0), Double.valueOf(0.5));
        assertEquals(0.5, instance.get(Integer.valueOf(0)), 0.0);
        assertFalse(instance.containsValue(instance.getDefaultValue()));
        assertTrue(instance.toString().contains("{0=0.5, 1=1.0, 2=2.0, 3=3.0}"));
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        assertTrue(instance.toString().contains("{1=1.0, 2=2.0}"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeNoAttributes() {
        new SparseInstance(-1);
    }

    @Test(expected = NullPointerException.class)
    public void testNullClassValue() {
        new SparseInstance(new Object() {});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeIndex() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(-1), Double.valueOf(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(null, Double.valueOf(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateKeys() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(0), Double.valueOf(2.0));
    }

    @Test
    public void testSetValues() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        Map<Integer, Double> expectedMap = new HashMap<>();
        expectedMap.put(Integer.valueOf(1), Double.valueOf(2.0));
        expectedMap.put(Integer.valueOf(2), Double.valueOf(3.0));
        instance.putAll(expectedMap);
        assertEquals(expectedMap, instance.data);
        assertTrue(instance.toString().contains("{1=2.0, 2=3.0}"));
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertTrue(instance.toString().contains("{2=3.0}"));
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance(new double[]{1.0, 2.0, 3.0});
        instance.clear();
        assertEquals(instance.getDefaultValue(), instance.get(Integer.valueOf(0)), 0.0);
        assertTrue(instance.isEmpty());
        assertTrue(instance.toString().contains("{}"));
    }
}