package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithAttributes() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(att, instance.attributes, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testConstructorWithAttributesAndClassValue() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        Object classValue = "1";
        DenseInstance instance = new DenseInstance(att, classValue);
        assertEquals(3, instance.noAttributes());
        assertArrayEquals(att, instance.attributes, 0.0);
        assertEquals(classValue, instance.classValue());
    }

    @Test
    public void testConstructorWithEmptyArray() {
        DenseInstance instance = new DenseInstance(new double[0]);
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testValue() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(1.0, instance.value(0), 0.0);
        assertEquals(2.0, instance.value(1), 0.0);
        assertEquals(3.0, instance.value(2), 0.0);
    }

    @Test
    public void testClear() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertArrayEquals(new double[0], instance.attributes, 0.0);
        assertNull(instance.classValue());
    }

    @Test
    public void testContainsKey() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < att.length; ++i) {
            assertTrue(instance.containsKey(i));
        }
        assertFalse(instance.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < att.length; ++i) {
            assertTrue(instance.containsValue(att[i]));
        }
        assertFalse(instance.containsValue(4.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(att.length, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
        }
    }

    @Test
    public void testGet() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        for (int i = 0; i < att.length; ++i) {
            assertEquals(att[i], instance.get(i), 0.0);
        }
        assertNull(instance.get(4));
    }

    @Test
    public void testIsEmpty() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> keySet = instance.keySet();
        assertEquals(att.length, keySet.size());
        for (Integer key : keySet) {
            assertTrue(instance.containsKey(key));
        }
    }

    @Test
    public void testPut() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        Double oldValue = instance.put(0, 4.0);
        assertEquals(1.0, oldValue.doubleValue());
        assertEquals(4.0, instance.attributes[0], 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPutThrowsException() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        instance.put(null, null);
    }

    @Test
    public void testPutAll() {
        double[] att1 = new double[] { 1.0, 2.0, 3.0 };
        double[] att2 = new double[] { 4.0, 5.0, 6.0 };
        DenseInstance instance = new DenseInstance(att1);
        instance.putAll(Arrays.asList(new Map.Entry[0]));
        assertArrayEquals(att1, instance.attributes, 0.0);
        instance.putAll(new HashMap<Integer, Double>() {{
            put(1, 5.0);
            put(3, 6.0);
        }});
        assertArrayEquals(new double[] { 1.0, 5.0, 3.0, 6.0 }, instance.attributes, 0.0);
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        DenseInstance newInstance = instance.copy();
        instance.removeAttribute(1);
        assertArrayEquals(new double[] { 1.0, 3.0 }, instance.attributes, 0.0);
        assertArrayEquals(att, newInstance.attributes, 0.0);
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = new double[] { 1.0, 2.0, 3.0, 4.0 };
        DenseInstance instance = new DenseInstance(att);
        Set<Integer> indicesToRemove = new HashSet<Integer>() {{
            add(1);
            add(3);
        }};
        instance.removeAttributes(indicesToRemove);
        assertArrayEquals(new double[] { 1.0, 4.0 }, instance.attributes, 0.0);
    }

    @Test
    public void testSize() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        assertEquals(att.length, instance.noAttributes());
    }

    @Test
    public void testValues() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        List<Double> values = new ArrayList<Double>(instance.values());
        assertEquals(att.length, values.size());
        for (int i = 0; i < att.length; ++i) {
            assertEquals(att[i], values.get(i), 0.0);
        }
    }

    @Test
    public void testToString() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att, "classValue");
        String expected = "{" + Arrays.toString(att) + ";classValue}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] att1 = new double[] { 1.0, 2.0, 3.0 };
        double[] att2 = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = new double[] { 1.0, 2.0, 3.0 };
        double[] att2 = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance1 = new DenseInstance(att1);
        DenseInstance instance2 = new DenseInstance(att2);
        DenseInstance copy1 = instance1.copy();
        DenseInstance copy2 = instance2.copy();
        assertSame(instance1, instance1);
        assertSame(instance1, copy1);
        assertSame(instance2, instance2);
        assertSame(instance2, copy2);
        assertSame(instance1, instance2);
        assertSame(instance1, copy1);
        assertSame(instance2, copy2);
    }

    @Test
    public void testCopy() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance instance = new DenseInstance(att);
        DenseInstance copy = instance.copy();
        assertEquals(att.length, copy.noAttributes());
        assertArrayEquals(att, copy.attributes, 0.0);
        assertNull(copy.classValue());
    }
}