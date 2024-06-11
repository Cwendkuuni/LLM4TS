package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testConstructor() {
        SparseInstance si = new SparseInstance();
        assertNull(si.getClassValue());
        assertEquals(-1, si.noAttributes());
        assertEmpty(si.keySet());
        assertEmpty(si.values());

        SparseInstance si1 = new SparseInstance(1);
        assertEquals(1, si1.noAttributes());
        assertNull(si1.getClassValue());
        assertEmpty(si1.keySet());
        assertEmpty(si1.values());

        SparseInstance si2 = new SparseInstance(1, 0.0);
        assertEquals(1, si2.noAttributes());
        assertNull(si2.getClassValue());
        assertEmpty(si2.keySet());
        assertEmpty(si2.values());

        SparseInstance si3 = new SparseInstance(1, "Class1.class");
        assertEquals(1, si3.noAttributes());
        assertEquals("Class1.class", si3.getClassValue().getClass().getName());
        assertEmpty(si3.keySet());
        assertEmpty(si3.values());

        SparseInstance si4 = new SparseInstance(1, 0.0, "Class1.class");
        assertEquals(1, si4.noAttributes());
        assertEquals("Class1.class", si4.getClassValue().getClass().getName());
        assertEmpty(si4.keySet());
        assertEmpty(si4.values());
    }

    @Test
    public void testConstructorWithDataVector() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0};
        SparseInstance si = new SparseInstance(datavector);
        assertEquals(4, si.noAttributes());
        assertNull(si.getClassValue());
        assertEquals(datavector[0], si.get(0));
        assertEquals(datavector[1], si.get(1));
        assertEquals(datavector[2], si.get(2));
        assertEquals(datavector[3], si.get(3));
        assertEmpty(si.keySet());
        assertEquals(Arrays.asList(1.0, 2.0, 3.0, 4.0), si.values());
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance si = new SparseInstance();
        si.setNoAttributes(5);
        assertEquals(5, si.noAttributes());
    }

    @Test
    public void testGetValue() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        assertEquals(1.0, si.value(0));
        assertEquals(2.0, si.value(1));
        assertEquals(3.0, si.value(2));
        assertEquals(4.0, si.value(3));
        assertEquals(5.0, si.value(4));
    }

    @Test
    public void testClear() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        si.clear();
        assertEquals(0, si.noAttributes());
        assertEmpty(si.keySet());
        assertEmpty(si.values());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        assertTrue(si.containsKey(Integer.valueOf(0)));
        assertTrue(si.containsKey(Integer.valueOf(1)));
        assertTrue(si.containsKey(Integer.valueOf(2)));
        assertTrue(si.containsKey(Integer.valueOf(3)));
        assertTrue(si.containsKey(Integer.valueOf(4)));
        assertFalse(si.containsKey(Integer.valueOf(5)));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        assertTrue(si.containsValue(1.0));
        assertTrue(si.containsValue(2.0));
        assertTrue(si.containsValue(3.0));
        assertTrue(si.containsValue(4.0));
        assertTrue(si.containsValue(5.0));
        assertFalse(si.containsValue(6.0));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entries = si.entrySet();
        int i = 0;
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(Integer.valueOf(i), entry.getKey());
            assertEquals(datavector[i++], entry.getValue());
        }
    }

    @Test
    public void testGet() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        assertEquals(1.0, si.get(0));
        assertEquals(2.0, si.get(1));
        assertEquals(3.0, si.get(2));
        assertEquals(4.0, si.get(3));
        assertEquals(5.0, si.get(4));
        assertEquals(0.0, si.get(5));
    }

    @Test
    public void testIsEmpty() {
        SparseInstance si = new SparseInstance();
        assertTrue(si.isEmpty());

        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si1 = new SparseInstance(datavector);
        assertFalse(si1.isEmpty());

        SparseInstance si2 = new SparseInstance(1);
        assertFalse(si2.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        Set<Integer> keys = si.keySet();
        int i = 0;
        for (Integer key : keys) {
            assertEquals(i++, key.intValue());
        }
    }

    @Test
    public void testPut() {
        SparseInstance si = new SparseInstance();
        si.put(Integer.valueOf(0), Double.valueOf(1.0));
        si.put(Integer.valueOf(1), Double.valueOf(2.0));
        assertEquals(1.0, si.get(0));
        assertEquals(2.0, si.get(1));
        assertEquals(0.0, si.get(2));

        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(3.0));
        si.putAll(map);
        assertEquals(3.0, si.get(0));
        assertEquals(2.0, si.get(1));
        assertEquals(0.0, si.get(2));
    }

    @Test
    public void testRemove() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        Double removedValue = si.remove(Integer.valueOf(2));
        assertEquals(3.0, removedValue);
        assertEquals(4, si.noAttributes());
        assertFalse(si.containsKey(Integer.valueOf(2)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        SparseInstance si = new SparseInstance();
        si.size();
    }

    @Test
    public void testValues() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        Collection<Double> values = si.values();
        List<Double> valueList = new ArrayList<>(values);
        assertEquals(5, valueList.size());
        assertEquals(1.0, valueList.get(0));
        assertEquals(2.0, valueList.get(1));
        assertEquals(3.0, valueList.get(2));
        assertEquals(4.0, valueList.get(3));
        assertEquals(5.0, valueList.get(4));
    }

    @Test
    public void testNoAttributes() {
        SparseInstance si = new SparseInstance();
        assertEquals(-1, si.noAttributes());

        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si1 = new SparseInstance(datavector);
        assertEquals(5, si1.noAttributes());

        SparseInstance si2 = new SparseInstance(1);
        assertEquals(1, si2.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        si.removeAttribute(2);
        assertEquals(4, si.noAttributes());
        assertFalse(si.containsKey(Integer.valueOf(2)));
    }

    @Test
    public void testToString() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        String toString = si.toString();
        String expectedString = "{" + datavector.toString() + ";Class1.class}";
        assertEquals(expectedString, toString);
    }

    @Test
    public void testHashCode() {
        double[] datavector1 = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si1 = new SparseInstance(datavector1);

        double[] datavector2 = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si2 = new SparseInstance(datavector2);

        double[] datavector3 = new double[]{1.0, 2.0, 3.0, 4.0, 6.0};
        SparseInstance si3 = new SparseInstance(datavector3);

        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));

        SparseInstance si4 = new SparseInstance();
        si4.putAll(map);

        assertEquals(si1.hashCode(), si1.hashCode());
        assertNotEquals(si1.hashCode(), si2.hashCode());
        assertNotEquals(si1.hashCode(), si3.hashCode());
        assertNotEquals(si1.hashCode(), si4.hashCode());
    }

    @Test
    public void testEquals() {
        double[] datavector1 = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si1 = new SparseInstance(datavector1);

        double[] datavector2 = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si2 = new SparseInstance(datavector2);

        double[] datavector3 = new double[]{1.0, 2.0, 3.0, 4.0, 6.0};
        SparseInstance si3 = new SparseInstance(datavector3);

        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));

        SparseInstance si4 = new SparseInstance();
        si4.putAll(map);

        assertTrue(si1.equals(si1));
        assertTrue(si1.equals(si2));
        assertFalse(si1.equals(si3));
        assertFalse(si1.equals(si4));
    }

    @Test
    public void testCopy() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        SparseInstance copiedSi = si.copy();
        assertEquals(si.noAttributes(), copiedSi.noAttributes());
        assertEquals(si.getClassValue(), copiedSi.getClassValue());
        assertEquals(si.data, copiedSi.data);
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = new double[]{1.0, 2.0, 3.0, 4.0, 5.0};
        SparseInstance si = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
        si.removeAttributes(indices);
        assertEquals(3, si.noAttributes());
        assertFalse(si.containsKey(Integer.valueOf(1)));
        assertFalse(si.containsKey(Integer.valueOf(3)));
    }
}