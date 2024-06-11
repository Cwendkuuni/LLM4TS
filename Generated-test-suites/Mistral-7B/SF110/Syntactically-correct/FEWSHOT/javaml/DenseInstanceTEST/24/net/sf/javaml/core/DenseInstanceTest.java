package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithArray() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        assertEquals(3, inst.noAttributes());
        assertArrayEquals(att, inst.attributes, 0.0);
        assertEquals(null, inst.getClassValue());
    }

    @Test
    public void testConstructorWithArrayAndClassValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        Object classValue = "Class1";
        DenseInstance inst = new DenseInstance(att, classValue);
        assertEquals(3, inst.noAttributes());
        assertArrayEquals(att, inst.attributes, 0.0);
        assertEquals(classValue, inst.getClassValue());
    }

    @Test
    public void testConstructorWithSize() {
        DenseInstance inst = new DenseInstance(5);
        assertEquals(5, inst.noAttributes());
        assertArrayEquals(new double[5], inst.attributes, 0.0);
        assertEquals(null, inst.getClassValue());
    }

    @Test
    public void testValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        assertEquals(1.0, inst.value(0), 0.0);
        assertEquals(2.0, inst.value(1), 0.0);
        assertEquals(3.0, inst.value(2), 0.0);
        assertNull(inst.value(3));
    }

    @Test
    public void testClear() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        inst.clear();
        assertEquals(0, inst.noAttributes());
        assertArrayEquals(new double[0], inst.attributes, 0.0);
        assertEquals(null, inst.getClassValue());
    }

    @Test
    public void testContainsKey() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        for (int i = 0; i < inst.noAttributes(); ++i) {
            assertTrue(inst.containsKey(i));
        }
        assertFalse(inst.containsKey(-1));
        assertFalse(inst.containsKey(4));
    }

    @Test
    public void testContainsValue() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        for (int i = 0; i < inst.noAttributes(); ++i) {
            double value = inst.attributes[i];
            assertTrue(inst.containsValue(Double.valueOf(value)));
        }
        assertFalse(inst.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testEntrySet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entries = inst.entrySet();
        assertEquals(inst.noAttributes(), entries.size());
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(entry.getKey(), entry.getValue().intValue());
            assertEquals(inst.attributes[entry.getKey()], entry.getValue().doubleValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        for (int i = 0; i < inst.noAttributes(); ++i) {
            assertEquals(inst.attributes[i], inst.get(Integer.valueOf(i)));
        }
        assertNull(inst.get(Integer.valueOf(-1)));
    }

    @Test
    public void testIsEmpty() {
        double[] att = new double[0];
        DenseInstance inst = new DenseInstance(att);
        assertTrue(inst.isEmpty());

        att = new double[]{1.0};
        inst = new DenseInstance(att);
        assertFalse(inst.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        SortedSet<Integer> keys = inst.keySet();
        assertEquals(inst.noAttributes(), keys.size());
        for (int i = 0; i < inst.noAttributes(); ++i) {
            assertEquals(i, keys.iterator().next());
        }
    }

    @Test
    public void testPut() {
        double[] att = new double[]{1.0, 2.0};
        DenseInstance inst = new DenseInstance(att);
        inst.put(2, Double.valueOf(3.0));
        assertEquals(3, inst.noAttributes());
        assertEquals(3.0, inst.attributes[2], 0.0);

        try {
            inst.put(4, Double.valueOf(4.0));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testPutAll() {
        double[] att1 = new double[]{1.0, 2.0};
        double[] att2 = new double[]{3.0, 4.0};
        DenseInstance inst = new DenseInstance(att1);
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        map.put(2, Double.valueOf(3.0));
        map.put(3, Double.valueOf(4.0));
        inst.putAll(map);
        assertEquals(4, inst.noAttributes());
        assertEquals(3.0, inst.attributes[2], 0.0);
        assertEquals(4.0, inst.attributes[3], 0.0);
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        inst.removeAttribute(1);
        assertEquals(2, inst.noAttributes());
        assertArrayEquals(new double[]{1.0, 3.0}, inst.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst1 = new DenseInstance(att1);
        DenseInstance inst2 = new DenseInstance(att2);
        assertEquals(inst1.hashCode(), inst2.hashCode());

        att1[0] = 4.0;
        inst1 = new DenseInstance(att1);
        assertNotEquals(inst1.hashCode(), inst2.hashCode());
    }

    @Test
    public void testEquals() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst1 = new DenseInstance(att1);
        DenseInstance inst2 = new DenseInstance(att2);
        assertTrue(inst1.equals(inst2));

        att1[0] = 4.0;
        inst1 = new DenseInstance(att1);
        assertFalse(inst1.equals(inst2));

        DenseInstance inst3 = new DenseInstance(inst1);
        assertTrue(inst1.equals(inst3));

        inst3.removeAttribute(0);
        assertFalse(inst1.equals(inst3));
    }

    @Test
    public void testCopy() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance inst = new DenseInstance(att);
        Instance copy = inst.copy();
        assertEquals(inst, copy);
        assertNotSame(inst, copy);
        assertArrayEquals(inst.attributes, copy.attributes);
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance inst = new DenseInstance(att);
        Set<Integer> indices = new HashSet<Integer>();
        indices.add(1);
        indices.add(3);
        inst.removeAttributes(indices);
        assertEquals(2, inst.noAttributes());
        assertArrayEquals(new double[]{1.0, 4.0}, inst.attributes, 0.0);
    }
}