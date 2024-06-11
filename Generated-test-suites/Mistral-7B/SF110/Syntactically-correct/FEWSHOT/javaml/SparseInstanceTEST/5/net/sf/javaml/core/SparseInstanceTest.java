package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testEmptyInstance() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertNull(instance.get(0));
    }

    @Test
    public void testInitializeWithDataVector() {
        double[] datavector = {2.0, 1.5, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.5, instance.get(1));
        assertEquals(2.0, instance.get(0));
        assertEquals(3.0, instance.get(2));
        assertEquals(0.0, instance.get(3));
    }

    @Test
    public void testPutAndGetValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(3.0, instance.get(2));
        assertNull(instance.get(3));
    }

    @Test
    public void testPutAllMap() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        map.put(Integer.valueOf(2), Double.valueOf(3.0));
        SparseInstance instance = new SparseInstance();
        instance.putAll(map);
        assertEquals(3, instance.noAttributes());
        assertEquals(1.0, instance.get(0));
        assertEquals(2.0, instance.get(1));
        assertEquals(3.0, instance.get(2));
        assertNull(instance.get(3));
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        instance.removeAttribute(1);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.get(0));
        assertEquals(3.0, instance.get(2));
        assertNull(instance.get(1));
    }

    @Test
    public void testRemoveAttributesSet() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2));
        instance.removeAttributes(indices);
        assertEquals(1, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertNull(instance.get(0));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        SparseInstance copiedInstance = instance.copy();
        assertEquals(3, copiedInstance.noAttributes());
        assertEquals(1.0, copiedInstance.get(0));
        assertEquals(2.0, copiedInstance.get(1));
        assertEquals(3.0, copiedInstance.get(2));
        assertNull(copiedInstance.get(3));
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(3, instance.size());
    }

    @Deprecated
    @Test
    public void testSizeDeprecated() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(3, instance.size());
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        List<Map.Entry<Integer, Double>> entryList = new ArrayList<>(entrySet);
        Map.Entry<Integer, Double> entry0 = entryList.get(0);
        Map.Entry<Integer, Double> entry1 = entryList.get(1);
        Map.Entry<Integer, Double> entry2 = entryList.get(2);
        assertEquals(0, entry0.getKey().intValue());
        assertEquals(1.0, entry0.getValue().doubleValue(), 0.0);
        assertEquals(1, entry1.getKey().intValue());
        assertEquals(2.0, entry1.getValue().doubleValue(), 0.0);
        assertEquals(2, entry2.getKey().intValue());
        assertEquals(3.0, entry2.getValue().doubleValue(), 0.0);
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Set<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        List<Integer> keyList = new ArrayList<>(keySet);
        assertEquals(0, keyList.get(0).intValue());
        assertEquals(1, keyList.get(1).intValue());
        assertEquals(2, keyList.get(2).intValue());
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        List<Double> valueList = new ArrayList<>(values);
        assertEquals(1.0, valueList.get(0).doubleValue(), 0.0);
        assertEquals(2.0, valueList.get(1).doubleValue(), 0.0);
        assertEquals(3.0, valueList.get(2).doubleValue(), 0.0);
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));
        assertEquals(3, instance.noAttributes());
        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());
        instance.removeAttribute(1);
        assertEquals(1, instance.noAttributes());
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }
}