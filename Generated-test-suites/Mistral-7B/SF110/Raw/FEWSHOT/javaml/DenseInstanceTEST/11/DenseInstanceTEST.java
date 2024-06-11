package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithEmptyArray() {
        DenseInstance denseInstance = new DenseInstance(new double[0]);
        assertEquals(0, denseInstance.noAttributes());
        assertTrue(denseInstance.isEmpty());
        assertNull(denseInstance.classValue());
        assertTrue(denseInstance.entrySet().isEmpty());
        assertTrue(denseInstance.keySet().isEmpty());
        assertNull(denseInstance.get(0));
        assertNull(denseInstance.value(0));
        assertFalse(denseInstance.containsKey(0));
        assertFalse(denseInstance.containsValue(Double.valueOf(0.0)));
        assertFalse(denseInstance.toString().contains(":"));
        assertFalse(denseInstance.toString().contains(","));
    }

    @Test
    public void testDenseInstanceWithOneAttribute() {
        DenseInstance denseInstance = new DenseInstance(new double[] { 1.0 });
        assertEquals(1, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        assertNull(denseInstance.classValue());
        assertEquals(1, denseInstance.size());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(1.0, denseInstance.get(0));
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsValue(Double.valueOf(1.0)));
        assertEquals(1.0, denseInstance.entrySet().iterator().next().getValue().doubleValue());
        assertTrue(denseInstance.keySet().contains(0));
        assertEquals("{1.0;}", denseInstance.toString());
    }

    @Test
    public void testDenseInstanceWithMultipleAttributes() {
        DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(3, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        assertNull(denseInstance.classValue());
        assertEquals(3, denseInstance.size());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(1.0, denseInstance.get(0));
        assertEquals(2.0, denseInstance.value(1), 0.0);
        assertEquals(2.0, denseInstance.get(1));
        assertEquals(3.0, denseInstance.value(2), 0.0);
        assertEquals(3.0, denseInstance.get(2));
        assertTrue(denseInstance.containsKey(0));
        assertTrue(denseInstance.containsKey(1));
        assertTrue(denseInstance.containsKey(2));
        assertTrue(denseInstance.containsValue(Double.valueOf(1.0)));
        assertTrue(denseInstance.containsValue(Double.valueOf(2.0)));
        assertTrue(denseInstance.containsValue(Double.valueOf(3.0)));
        assertEquals(Arrays.asList(1.0, 2.0, 3.0), new ArrayList<>(denseInstance.values()));
        assertEquals(Arrays.asList(Map.entry(0, 1.0), Map.entry(1, 2.0), Map.entry(2, 3.0)), denseInstance.entrySet());
        assertEquals(Arrays.asList(0, 1, 2), denseInstance.keySet());
        assertEquals("{1.0, 2.0, 3.0;}", denseInstance.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveAttribute() {
        DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
        denseInstance.removeAttribute(0);
    }

    @Test
    public void testRemoveAttributes() {
        DenseInstance denseInstance = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(1, 2));
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(1, denseInstance.noAttributes());
        assertEquals(1, denseInstance.size());
        assertFalse(denseInstance.isEmpty());
        assertEquals(1.0, denseInstance.value(0), 0.0);
        assertEquals(1.0, denseInstance.get(0));
        assertFalse(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsValue(Double.valueOf(2.0)));
        assertFalse(denseInstance.containsValue(Double.valueOf(3.0)));
        assertFalse(denseInstance.toString().contains(", 2.0"));
        assertFalse(denseInstance.toString().contains(", 3.0"));
    }

    @Test
    public void testCopy() {
        DenseInstance original = new DenseInstance(new double[] { 1.0, 2.0, 3.0 });
        DenseInstance copied = original.copy();
        assertEquals(original.noAttributes(), copied.noAttributes());
        assertEquals(original.size(), copied.size());
        assertTrue(Arrays.equals(original.attributes, copied.attributes));
        assertEquals(original.classValue(), copied.classValue());
    }
}