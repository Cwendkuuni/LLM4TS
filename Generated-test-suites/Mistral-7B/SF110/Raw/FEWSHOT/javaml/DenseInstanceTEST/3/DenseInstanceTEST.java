package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testDenseInstanceCreation() {
        double[] att = new double[5];
        DenseInstance di1 = new DenseInstance(att);
        DenseInstance di2 = new DenseInstance(att, "classValue");
    }

    @Test
    public void testValueMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        assertEquals(1.0, di.value(0), 0.0);
        assertEquals(2.0, di.value(1), 0.0);
        assertEquals(3.0, di.value(2), 0.0);
        try {
            di.value(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testClearMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        di.clear();
        assertEquals(0, di.noAttributes());
        assertNull(di.classValue());
    }

    @Test
    public void testContainsKeyMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        assertTrue(di.containsKey(0));
        assertTrue(di.containsKey(1));
        assertTrue(di.containsKey(2));
        assertFalse(di.containsKey(-1));
        assertFalse(di.containsKey(3));
    }

    @Test
    public void testContainsValueMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0, 2.0};
        DenseInstance di = new DenseInstance(att);
        assertTrue(di.containsValue(2.0));
        assertFalse(di.containsValue(4.0));
    }

    @Test
    public void testEntrySetMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entrySet = di.entrySet();
        assertEquals(3, entrySet.size());
        Map.Entry<Integer, Double> entry = entrySet.iterator().next();
        assertEquals(0, entry.getKey().intValue());
        assertEquals(1.0, entry.getValue().doubleValue(), 0.0);
    }

    @Test
    public void testGetMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        assertEquals(1.0, di.get(0), 0.0);
        assertEquals(2.0, di.get(1), 0.0);
        assertEquals(3.0, di.get(2), 0.0);
        assertNull(di.get(-1));
    }

    @Test
    public void testIsEmptyMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        assertFalse(di.isEmpty());
        DenseInstance emptyDi = new DenseInstance();
        assertTrue(emptyDi.isEmpty());
    }

    @Test
    public void testKeySetMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        SortedSet<Integer> keySet = di.keySet();
        assertEquals(3, keySet.size());
        assertTrue(keySet.contains(0));
        assertTrue(keySet.contains(1));
        assertTrue(keySet.contains(2));
    }

    @Test
    public void testPutMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        double newValue = 4.0;
        di.put(0, newValue);
        assertEquals(newValue, di.value(0), 0.0);
        assertArrayEquals(new double[]{newValue, 2.0, 3.0}, di.attributes, 0.0);
    }

    @Test
    public void testPutAllMethod() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        double[] att2 = new double[]{4.0, 5.0, 6.0};
        DenseInstance di1 = new DenseInstance(att1);
        DenseInstance di2 = new DenseInstance(att2);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 4.0);
        map.put(1, 5.0);
        map.put(2, 6.0);
        di1.putAll(map);
        assertEquals(6, di1.noAttributes());
        assertArrayEquals(new double[]{4.0, 5.0, 6.0, 2.0, 3.0}, di1.attributes, 0.0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        di.remove(0);
    }

    @Test
    public void testSizeMethod() {
        double[] att = new double[5];
        DenseInstance di = new DenseInstance(att);
        assertEquals(5, di.noAttributes());
    }

    @Test
    public void testValuesMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        Collection<Double> values = di.values();
        assertEquals(3, values.size());
        assertTrue(values.contains(1.0));
        assertTrue(values.contains(2.0));
        assertTrue(values.contains(3.0));
    }

    @Test
    public void testNoAttributesMethod() {
        double[] att = new double[5];
        DenseInstance di = new DenseInstance(att);
        assertEquals(5, di.noAttributes());
    }

    @Test
    public void testToStringMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        String str = di.toString();
        assertTrue(str.contains("["));
        assertTrue(str.contains(";"));
        assertTrue(str.contains("]"));
        assertTrue(str.contains("{"));
        assertTrue(str.contains("}"));
    }

    @Test
    public void testRemoveAttributeMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        di.removeAttribute(1);
        assertArrayEquals(new double[]{1.0, 3.0}, di.attributes, 0.0);
    }

    @Test
    public void testHashCodeMethod() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance di1 = new DenseInstance(att1);
        DenseInstance di2 = new DenseInstance(att2);
        assertEquals(di1.hashCode(), di2.hashCode());

        double[] att3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance di3 = new DenseInstance(att3);
        assertNotEquals(di1.hashCode(), di3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        double[] att1 = new double[]{1.0, 2.0, 3.0};
        double[] att2 = new double[]{1.0, 2.0, 3.0};
        DenseInstance di1 = new DenseInstance(att1);
        DenseInstance di2 = new DenseInstance(att2);
        assertTrue(di1.equals(di2));

        double[] att3 = new double[]{1.0, 2.0, 4.0};
        DenseInstance di3 = new DenseInstance(att3);
        assertFalse(di1.equals(di3));

        DenseInstance di4 = new DenseInstance(att1);
        assertTrue(di1.equals(di4));

        DenseInstance di5 = new DenseInstance();
        assertFalse(di1.equals(di5));
    }

    @Test
    public void testCopyMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0};
        DenseInstance di = new DenseInstance(att);
        DenseInstance diCopy = di.copy();
        assertEquals(di.noAttributes(), diCopy.noAttributes());
        assertArrayEquals(di.attributes, diCopy.attributes, 0.0);
        assertEquals(di.classValue(), diCopy.classValue());
    }

    @Test
    public void testRemoveAttributesMethod() {
        double[] att = new double[]{1.0, 2.0, 3.0, 4.0};
        DenseInstance di = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>();
        indices.add(2);
        indices.add(3);
        di.removeAttributes(indices);
        assertArrayEquals(new double[]{1.0, 2.0}, di.attributes, 0.0);
    }
}