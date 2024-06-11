package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceWithEmptyAttributeArray() {
        DenseInstance denseInstance = new DenseInstance();
        assertEquals(0, denseInstance.noAttributes());
        assertTrue(denseInstance.isEmpty());
        assertTrue(denseInstance.keySet().isEmpty());
        assertEquals(0, denseInstance.size());
        assertFalse(denseInstance.containsKey(0));
        assertFalse(denseInstance.containsValue(0.0));
        assertFalse(denseInstance.containsValue(1.0));
        assertNull(denseInstance.value(0));
        assertNull(denseInstance.get(0));
        assertThrows(UnsupportedOperationException.class, () -> denseInstance.removeAttribute(0));
    }

    @Test
    public void testDenseInstanceWithSingleAttribute() {
        double[] attributes = new double[1];
        attributes[0] = 1.0;
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(1, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue().doubleValue(), 0.0);
        assertEquals(1.0, denseInstance.value(0));
        assertEquals(1.0, denseInstance.get(0));
        assertThrows(UnsupportedOperationException.class, () -> denseInstance.removeAttribute(0));
    }

    @Test
    public void testDenseInstanceWithMultipleAttributes() {
        double[] attributes = new double[3];
        attributes[0] = 1.0;
        attributes[1] = 2.0;
        attributes[2] = 3.0;
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map.Entry<Integer, Double> entry1 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry2 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry3 = entrySet.iterator().next();
        assertEquals(0, entry1.getKey().intValue());
        assertEquals(1.0, entry1.getValue().doubleValue(), 0.0);
        assertEquals(1, denseInstance.size());
        assertEquals(1.0, denseInstance.value(0));
        assertEquals(1.0, denseInstance.get(0));
        assertEquals(2.0, denseInstance.value(1));
        assertEquals(2.0, denseInstance.get(1));
        assertEquals(3.0, denseInstance.value(2));
        assertEquals(3.0, denseInstance.get(2));
        assertFalse(denseInstance.containsValue(4.0));
        assertTrue(denseInstance.containsValue(1.0));
        assertFalse(denseInstance.containsValue(null));
        assertFalse(denseInstance.containsKey(4));
        assertTrue(denseInstance.containsKey(0));
        assertThrows(UnsupportedOperationException.class, () -> denseInstance.removeAttribute(0));
    }

    @Test
    public void testDenseInstanceWithClassValue() {
        double[] attributes = new double[2];
        attributes[0] = 1.0;
        attributes[1] = 2.0;
        DenseInstance denseInstance = new DenseInstance(attributes, "ClassValue");
        assertEquals("ClassValue", denseInstance.classValue());
        assertEquals(2, denseInstance.noAttributes());
        assertFalse(denseInstance.isEmpty());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map.Entry<Integer, Double> entry1 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry2 = entrySet.iterator().next();
        assertEquals(0, entry1.getKey().intValue());
        assertEquals(1.0, entry1.getValue().doubleValue(), 0.0);
        assertEquals(1, denseInstance.size());
        assertEquals(1.0, denseInstance.value(0));
        assertEquals(1.0, denseInstance.get(0));
        assertEquals(2.0, denseInstance.value(1));
        assertEquals(2.0, denseInstance.get(1));
        assertFalse(denseInstance.containsValue(4.0));
        assertTrue(denseInstance.containsValue(1.0));
        assertFalse(denseInstance.containsValue(null));
        assertFalse(denseInstance.containsKey(4));
        assertTrue(denseInstance.containsKey(0));
        assertFalse(denseInstance.containsKey(2));
        assertThrows(UnsupportedOperationException.class, () -> denseInstance.removeAttribute(0));
    }

    @Test
    public void testDenseInstanceWithCustomSize() {
        DenseInstance denseInstance = new DenseInstance(5);
        assertEquals(5, denseInstance.noAttributes());
        assertTrue(denseInstance.isEmpty());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        assertTrue(entrySet.isEmpty());
        assertEquals(5, denseInstance.size());
        assertArrayEquals(new double[5], denseInstance.attributes);
        assertNull(denseInstance.classValue());
    }

    @Test
    public void testDenseInstanceClone() {
        double[] attributes = new double[3];
        attributes[0] = 1.0;
        attributes[1] = 2.0;
        attributes[2] = 3.0;
        DenseInstance originalDenseInstance = new DenseInstance(attributes);
        DenseInstance clonedDenseInstance = originalDenseInstance.copy();
        assertEquals(3, originalDenseInstance.noAttributes());
        assertEquals(3, clonedDenseInstance.noAttributes());
        assertEquals(originalDenseInstance.hashCode(), clonedDenseInstance.hashCode());
        for (int i = 0; i < originalDenseInstance.attributes.length; i++) {
            assertEquals(originalDenseInstance.attributes[i], clonedDenseInstance.attributes[i]);
        }
        Set<Map.Entry<Integer, Double>> originalEntrySet = originalDenseInstance.entrySet();
        Set<Map.Entry<Integer, Double>> clonedEntrySet = clonedDenseInstance.entrySet();
        assertEquals(originalEntrySet.size(), clonedEntrySet.size());
        for (Map.Entry<Integer, Double> entryOriginal : originalEntrySet) {
            Map.Entry<Integer, Double> entryCloned = clonedEntrySet.stream()
                    .filter(entry -> entry.getKey().intValue() == entryOriginal.getKey().intValue())
                    .findFirst().orElse(null);
            assertEquals(entryOriginal.getKey().intValue(), entryCloned.getKey().intValue());
            assertEquals(entryOriginal.getValue().doubleValue(), entryCloned.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testDenseInstanceRemoveAttribute() {
        double[] attributes = new double[3];
        attributes[0] = 1.0;
        attributes[1] = 2.0;
        attributes[2] = 3.0;
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, denseInstance.size());
        denseInstance.removeAttribute(1);
        assertEquals(2, denseInstance.noAttributes());
        assertEquals(2, denseInstance.size());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map.Entry<Integer, Double> entry1 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry2 = entrySet.iterator().next();
        assertEquals(0, entry1.getKey().intValue());
        assertEquals(1.0, entry1.getValue().doubleValue(), 0.0);
        assertEquals(2, entry2.getKey().intValue());
        assertEquals(3.0, entry2.getValue().doubleValue(), 0.0);
        assertFalse(denseInstance.containsKey(1));
        assertFalse(denseInstance.containsValue(2.0));
    }

    @Test
    public void testDenseInstancePut() {
        double[] attributes = new double[3];
        attributes[0] = 1.0;
        attributes[1] = 2.0;
        attributes[2] = 3.0;
        DenseInstance denseInstance = new DenseInstance(attributes);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, denseInstance.size());
        denseInstance.put(1, 4.0);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, denseInstance.size());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map.Entry<Integer, Double> entry1 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry2 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry3 = entrySet.iterator().next();
        assertEquals(0, entry1.getKey().intValue());
        assertEquals(1.0, entry1.getValue().doubleValue(), 0.0);
        assertEquals(1, entry2.getKey().intValue());
        assertEquals(4.0, entry2.getValue().doubleValue(), 0.0);
        assertEquals(2, entry3.getKey().intValue());
        assertEquals(3.0, entry3.getValue().doubleValue(), 0.0);
    }

    @Test
    public void testDenseInstancePutAll() {
        double[] attributesSource = new double[2];
        attributesSource[0] = 1.0;
        attributesSource[1] = 2.0;
        DenseInstance denseInstance = new DenseInstance(new double[3]);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 1.0);
        map.put(1, 2.0);
        denseInstance.putAll(map);
        assertEquals(2, denseInstance.noAttributes());
        assertEquals(2, denseInstance.size());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map.Entry<Integer, Double> entry1 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry2 = entrySet.iterator().next();
        assertEquals(0, entry1.getKey().intValue());
        assertEquals(1.0, entry1.getValue().doubleValue(), 0.0);
        assertEquals(1, entry2.getKey().intValue());
        assertEquals(2.0, entry2.getValue().doubleValue(), 0.0);
    }

    @Test
    public void testDenseInstanceRemoveAttributes() {
        double[] attributes = new double[5];
        attributes[0] = 1.0;
        attributes[1] = 2.0;
        attributes[2] = 3.0;
        attributes[3] = 4.0;
        attributes[4] = 5.0;
        DenseInstance denseInstance = new DenseInstance(attributes);
        Set<Integer> indicesToRemove = new HashSet<>(Arrays.asList(2, 4));
        denseInstance.removeAttributes(indicesToRemove);
        assertEquals(3, denseInstance.noAttributes());
        assertEquals(3, denseInstance.size());
        Set<Map.Entry<Integer, Double>> entrySet = denseInstance.entrySet();
        Map.Entry<Integer, Double> entry1 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry2 = entrySet.iterator().next();
        Map.Entry<Integer, Double> entry3 = entrySet.iterator().next();
        assertEquals(0, entry1.getKey().intValue());
        assertEquals(1.0, entry1.getValue().doubleValue(), 0.0);
        assertEquals(1, entry2.getKey().intValue());
        assertEquals(3.0, entry2.getValue().doubleValue(), 0.0);
        assertNull(entry3);
        assertFalse(denseInstance.containsValue(3.0));
        assertFalse(denseInstance.containsValue(4.0));
        assertFalse(denseInstance.containsValue(5.0));
        assertFalse(denseInstance.containsKey(2));
        assertFalse(denseInstance.containsKey(3));
        assertFalse(denseInstance.containsKey(4));
        assertFalse(denseInstance.containsKey(5));
    }
}