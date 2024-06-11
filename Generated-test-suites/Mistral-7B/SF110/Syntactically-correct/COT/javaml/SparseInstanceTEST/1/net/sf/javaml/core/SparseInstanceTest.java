package net.sf.javaml.core.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class SparseInstanceTest {

    @Test
    public void testSparseInstance_NoArguments() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstance_WithNoAttributes() {
        SparseInstance instance = new SparseInstance(5);
        assertEquals(5, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstance_WithDefaultValue() {
        SparseInstance instance = new SparseInstance(5, 0.5);
        assertEquals(5, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstance_WithClassValue() {
        SparseInstance instance = new SparseInstance(5, "ClassValue");
        assertEquals("ClassValue", instance.getClassValue());
        assertEquals(5, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testSparseInstance_WithDataVector() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(5, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertEquals(4.0, instance.value(3), 0.001);
        assertEquals(5.0, instance.value(4), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSparseInstance_WithNegativeNoAttributes() {
        new SparseInstance(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSparseInstance_WithNullDataVector() {
        new SparseInstance(null);
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(2.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPut_NullKey() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(null, 1.0);
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance(5);
        Map<Integer, Double> map = new HashMap<>();
        map.put(1, 1.0);
        map.put(2, 2.0);
        instance.putAll(map);
        assertEquals(1.0, instance.get(1), 0.001);
        assertEquals(2.0, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutAll_NullMap() {
        SparseInstance instance = new SparseInstance(5);
        instance.putAll(null);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.remove(1);
        assertEquals(2.0, instance.get(0), 0.001);
        assertEquals(0.0, instance.get(1), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemove_NullKey() {
        SparseInstance instance = new SparseInstance(5);
        instance.remove(null);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        Set<Integer> keys = new HashSet<>(entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            keys.add(entry.getKey());
        }
        List<Integer> sortedKeys = new ArrayList<>(keys);
        Collections.sort(sortedKeys);
        List<Integer> expectedKeys = Arrays.asList(1, 2);
        assertEquals(expectedKeys, sortedKeys);
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(0.0, instance.value(2), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValue_NegativeIndex() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.value(-1);
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertTrue(instance.containsKey(1));
        assertFalse(instance.containsKey(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsKey_NullKey() {
        SparseInstance instance = new SparseInstance(5);
        instance.containsKey(null);
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(1, 2.0);
        assertTrue(instance.containsValue(1.0));
        assertFalse(instance.containsValue(3.0));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        List<Map.Entry<Integer, Double>> expectedEntries = Arrays.asList(
                Map.entry(1, 1.0),
                Map.entry(2, 2.0)
        );
        assertEquals(expectedEntries, instance.entrySet().stream().sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey())).collect(Collectors.toList()));
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance(5);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        assertEquals(2, instance.noAttributes());
        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.removeAttribute(1);
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(0.0, instance.value(2), 0.001);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveAttribute_NegativeIndex() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.removeAttribute(-1);
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        String expected = "{1=1.0, 2=2.0;}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance(5);
        instance1.put(1, 1.0);
        instance1.put(2, 2.0);
        SparseInstance instance2 = new SparseInstance(5);
        instance2.put(1, 1.0);
        instance2.put(2, 2.0);
        SparseInstance instance3 = new SparseInstance(5);
        instance3.put(1, 2.0);
        instance3.put(2, 2.0);
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance(5);
        instance1.put(1, 1.0);
        instance1.put(2, 2.0);
        SparseInstance instance2 = new SparseInstance(5);
        instance2.put(1, 1.0);
        instance2.put(2, 2.0);
        SparseInstance instance3 = new SparseInstance(5);
        instance3.put(1, 2.0);
        instance3.put(2, 2.0);
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        SparseInstance copiedInstance = instance.copy();
        assertEquals(1.0, copiedInstance.value(0), 0.001);
        assertEquals(2.0, copiedInstance.value(1), 0.001);
        assertEquals(5, copiedInstance.noAttributes());
        assertNotSame(instance, copiedInstance);
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance(5);
        instance.put(1, 1.0);
        instance.put(2, 2.0);
        instance.put(3, 3.0);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2));
        instance.removeAttributes(indices);
        assertEquals(0.0, instance.value(0), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertEquals(1, instance.noAttributes());
    }

    @Test
    public void testSparseInstance_WithDefaultValue_AndDataVector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.5);
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());
        assertEquals(1.0, instance.value(0), 0.001);
        assertEquals(2.0, instance.value(1), 0.001);
        assertEquals(3.0, instance.value(2), 0.001);
        assertEquals(0.5, instance.defaultValue, 0.001);
    }
}