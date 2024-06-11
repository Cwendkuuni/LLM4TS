package net.sf.javaml.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testSparseInstanceCreation() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance(3);
        SparseInstance instance3 = new SparseInstance(3, 0.5);
        SparseInstance instance4 = new SparseInstance(3, "Class");
        SparseInstance instance5 = new SparseInstance(3, 0.5, "Class");
        SparseInstance instance6 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        SparseInstance instance7 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5);
        SparseInstance instance8 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, "Class");
        SparseInstance instance9 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 }, 0.5, "Class");

        assertEquals(instance1.getClass(), SparseInstance.class);
        assertEquals(instance2.getClass(), SparseInstance.class);
        assertEquals(instance2.noAttributes(), 3);
        assertEquals(instance3.getClass(), SparseInstance.class);
        assertEquals(instance3.noAttributes(), 3);
        assertEquals(instance3.defaultValue(), 0.5);
        assertEquals(instance4.getClass(), SparseInstance.class);
        assertEquals(instance4.noAttributes(), 3);
        assertEquals(instance4.defaultValue(), 0.0);
        assertEquals(instance4.classValue(), "Class");
        assertEquals(instance5.getClass(), SparseInstance.class);
        assertEquals(instance5.noAttributes(), 3);
        assertEquals(instance5.defaultValue(), 0.5);
        assertEquals(instance5.classValue(), "Class");
        assertEquals(instance6.getClass(), SparseInstance.class);
        assertEquals(instance6.noAttributes(), 3);
        assertEquals(instance6.defaultValue(), 0.0);
        assertEquals(instance6.classValue(), null);
        assertEquals(instance7.getClass(), SparseInstance.class);
        assertEquals(instance7.noAttributes(), 3);
        assertEquals(instance7.defaultValue(), 0.5);
        assertEquals(instance7.classValue(), null);
        assertEquals(instance8.getClass(), SparseInstance.class);
        assertEquals(instance8.noAttributes(), 3);
        assertEquals(instance8.defaultValue(), 0.0);
        assertEquals(instance8.classValue(), "Class");
        assertEquals(instance9.getClass(), SparseInstance.class);
        assertEquals(instance9.noAttributes(), 3);
        assertEquals(instance9.defaultValue(), 0.5);
        assertEquals(instance9.classValue(), "Class");
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance(3);
        instance.setNoAttributes(5);
        assertEquals(instance.noAttributes(), 5);
    }

    @Test
    public void testConstructorWithDatavector() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(instance.noAttributes(), 3);
        assertEquals(instance.defaultValue(), 0.0);
        assertEquals(instance.classValue(), null);
        assertEquals(instance.data.size(), 3);
        assertEquals(instance.data.containsKey(Integer.valueOf(0)), true);
        assertEquals(instance.data.get(Integer.valueOf(0)), Double.valueOf(1.0));
        assertEquals(instance.data.containsKey(Integer.valueOf(1)), true);
        assertEquals(instance.data.get(Integer.valueOf(1)), Double.valueOf(2.0));
        assertEquals(instance.data.containsKey(Integer.valueOf(2)), true);
        assertEquals(instance.data.get(Integer.valueOf(2)), Double.valueOf(3.0));
    }

    @Test
    public void testInitiate() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0, 0.5 });
        instance.initiate(new double[] { 1.0, 2.0, 3.0, 4.0 });
        assertEquals(instance.noAttributes(), 4);
        assertEquals(instance.defaultValue(), 0.5);
        assertTrue(instance.data.containsKey(Integer.valueOf(0)));
        assertTrue(instance.data.containsKey(Integer.valueOf(1)));
        assertTrue(instance.data.containsKey(Integer.valueOf(2)));
        assertTrue(instance.data.containsKey(Integer.valueOf(3)));
        assertEquals(instance.get(Integer.valueOf(0)), Double.valueOf(1.0));
        assertEquals(instance.get(Integer.valueOf(1)), Double.valueOf(2.0));
        assertEquals(instance.get(Integer.valueOf(2)), Double.valueOf(3.0));
        assertEquals(instance.get(Integer.valueOf(3)), Double.valueOf(4.0));
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(instance.value(0), 1.0);
        assertEquals(instance.value(1), 2.0);
        assertEquals(instance.value(2), 3.0);
        assertNull(instance.value(3));
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(instance.data.size(), 3);
        instance.clear();
        assertEquals(instance.data.size(), 0);
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertTrue(instance.containsKey(Integer.valueOf(0)));
        assertTrue(instance.containsKey(Integer.valueOf(1)));
        assertTrue(instance.containsKey(Integer.valueOf(2)));
        assertFalse(instance.containsKey(Integer.valueOf(3)));
        instance.put(Integer.valueOf(3), Double.valueOf(4.0));
        assertTrue(instance.containsKey(Integer.valueOf(3)));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        Double value1 = Double.valueOf(1.0);
        Double value2 = Double.valueOf(2.0);
        Double value3 = Double.valueOf(3.0);
        assertTrue(instance.containsValue(value1));
        assertTrue(instance.containsValue(value2));
        assertTrue(instance.containsValue(value3));
        assertFalse(instance.containsValue(Double.valueOf(4.0)));
        instance.put(Integer.valueOf(3), Double.valueOf(4.0));
        assertTrue(instance.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        Set<Map.Entry<Integer, Double>> entries = instance.entrySet();
        assertEquals(entries.size(), 3);
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(entries);
        assertEquals(list.get(0).getKey(), Integer.valueOf(0));
        assertEquals(list.get(0).getValue(), Double.valueOf(1.0));
        assertEquals(list.get(1).getKey(), Integer.valueOf(1));
        assertEquals(list.get(1).getValue(), Double.valueOf(2.0));
        assertEquals(list.get(2).getKey(), Integer.valueOf(2));
        assertEquals(list.get(2).getValue(), Double.valueOf(3.0));
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(instance.get(Integer.valueOf(0)), Double.valueOf(1.0));
        assertEquals(instance.get(Integer.valueOf(1)), Double.valueOf(2.0));
        assertEquals(instance.get(Integer.valueOf(2)), Double.valueOf(3.0));
        assertNull(instance.get(Integer.valueOf(3)));
        instance.put(Integer.valueOf(3), Double.valueOf(4.0));
        assertEquals(instance.get(Integer.valueOf(3)), Double.valueOf(4.0));
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        Set<Integer> keys = instance.keySet();
        assertEquals(keys.size(), 3);
        List<Integer> list = new ArrayList<>(keys);
        assertEquals(list.get(0), Integer.valueOf(0));
        assertEquals(list.get(1), Integer.valueOf(1));
        assertEquals(list.get(2), Integer.valueOf(2));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        instance.put(Integer.valueOf(3), Double.valueOf(4.0));
        assertEquals(instance.data.get(Integer.valueOf(3)), Double.valueOf(4.0));
        assertEquals(instance.noAttributes(), 4);
    }

    @Test
    public void testPutAll() {
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        instance.putAll(map);
        assertEquals(instance.data.size(), 3);
        List<Map.Entry<Integer, Double>> list = new ArrayList<>(instance.entrySet());
        assertEquals(list.get(0).getKey(), Integer.valueOf(0));
        assertEquals(list.get(0).getValue(), Double.valueOf(1.0));
        assertEquals(list.get(1).getKey(), Integer.valueOf(1));
        assertEquals(list.get(1).getValue(), Double.valueOf(2.0));
        assertEquals(list.get(2).getKey(), Integer.valueOf(2));
        assertEquals(list.get(2).getValue(), Double.valueOf(3.0));
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        Double value = Double.valueOf(2.0);
        instance.put(Integer.valueOf(3), value);
        assertEquals(instance.get(Integer.valueOf(3)), value);
        instance.remove(Integer.valueOf(3));
        assertNull(instance.get(Integer.valueOf(3)));
        assertEquals(instance.noAttributes(), 3);
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        instance.removeAttribute(0);
        assertEquals(instance.noAttributes(), 2);
        instance.removeAttribute(1);
        assertEquals(instance.noAttributes(), 1);
        instance.removeAttribute(2);
        assertEquals(instance.noAttributes(), 0);
    }

    @Test
    public void testSize() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(instance.data.size(), 3);
        instance.clear();
        assertEquals(instance.data.size(), 0);
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        Collection<Double> values = instance.values();
        assertEquals(values.size(), 3);
        List<Double> list = new ArrayList<>(values);
        assertEquals(list.get(0), Double.valueOf(1.0));
        assertEquals(list.get(1), Double.valueOf(2.0));
        assertEquals(list.get(2), Double.valueOf(3.0));
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        assertEquals(instance.noAttributes(), 3);
        instance.clear();
        assertEquals(instance.noAttributes(), 0);
        assertEquals(instance.noAttributes(), instance.data.keySet().size());
    }

    @Test
    public void testToString() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        String expected = "{" + instance.data.toString() + ";" + instance.classValue() + "}";
        String actual = instance.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        SparseInstance instance1 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        SparseInstance instance2 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        SparseInstance instance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.1 });

        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance2.hashCode();
        int hashCode3 = instance3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        SparseInstance instance2 = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        SparseInstance instance3 = new SparseInstance(new double[] { 1.0, 2.0, 3.1 });

        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        SparseInstance copy = instance.copy();
        assertEquals(instance.data, copy.data);
        assertEquals(instance.defaultValue, copy.defaultValue);
        assertEquals(instance.noAttributes, copy.noAttributes);
        assertEquals(instance.classValue, copy.classValue);
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance(new double[] { 1.0, 2.0, 3.0 });
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 2));
        instance.removeAttributes(indices);
        assertEquals(instance.noAttributes(), 1);
        assertEquals(instance.data.keySet().size(), 1);
    }
}