package net.sf.javaml.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class DenseInstanceTest {

    @Test
    public void testConstructorWithAttributeArray() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertNotNull(di);
        assertArrayEquals(att, di.attributes, 0.0);
    }

    @Test
    public void testConstructorWithAttributeArrayAndClassValue() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        Object classValue = "ClassValue";
        DenseInstance di = new DenseInstance(att, classValue);
        assertNotNull(di);
        assertArrayEquals(att, di.attributes, 0.0);
        assertEquals(classValue, di.classValue());
    }

    @Test
    public void testConstructorWithEmptyAttributeArray() {
        DenseInstance di = new DenseInstance();
        assertArrayEquals(new double[0], di.attributes, 0.0);
    }

    @Test
    public void testConstructorWithSize() {
        int size = 5;
        DenseInstance di = new DenseInstance(size);
        assertNotNull(di);
        double[] expected = new double[size];
        Arrays.fill(expected, 0.0);
        assertArrayEquals(expected, di.attributes, 0.0);
    }

    @Test
    public void testValue() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertEquals(1.0, di.value(0), 0.0);
        assertEquals(2.0, di.value(1), 0.0);
        assertEquals(3.0, di.value(2), 0.0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testValueOutOfBounds() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.value(3);
    }

    @Test
    public void testClear() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.clear();
        assertArrayEquals(new double[0], di.attributes, 0.0);
    }

    @Test
    public void testContainsKey() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertTrue(di.containsKey(0));
        assertTrue(di.containsKey(1));
        assertTrue(di.containsKey(2));
        assertFalse(di.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] att = new double[] { 1.0, 1.5, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertTrue(di.containsValue(1.0));
        assertTrue(di.containsValue(1.5));
        assertFalse(di.containsValue(2.0));
    }

    @Test
    public void testEntrySet() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        Set<Map.Entry<Integer, Double>> entries = di.entrySet();
        int index = 0;
        for (Map.Entry<Integer, Double> entry : entries) {
            assertEquals(index++, entry.getKey());
            assertEquals(att[index], entry.getValue(), 0.0);
        }
    }

    @Test
    public void testGet() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertEquals(1.0, di.get(0), 0.0);
        assertEquals(2.0, di.get(1), 0.0);
        assertEquals(3.0, di.get(2), 0.0);
        assertNull(di.get(3));
    }

    @Test(expected = ClassCastException.class)
    public void testGetWithNonIntegerKey() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.get("StringKey");
    }

    @Test
    public void testIsEmpty() {
        double[] att = new double[] {};
        DenseInstance di = new DenseInstance(att);
        assertTrue(di.isEmpty());

        double[] attNonEmpty = new double[] { 1.0 };
        DenseInstance diNonEmpty = new DenseInstance(attNonEmpty);
        assertFalse(diNonEmpty.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        SortedSet<Integer> keys = di.keySet();
        int index = 0;
        for (Integer key : keys) {
            assertEquals(index++, key.intValue());
        }
    }

    @Test
    public void testPut() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        Double oldValue = di.put(0, 4.0);
        assertEquals(1.0, oldValue, 0.0);
        assertEquals(4.0, di.attributes[0], 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullKey() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.put(null, 4.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutWithNullValue() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.put(0, null);
    }

    @Test
    public void testPutAll() {
        double[] attSource = new double[] { 1.0, 2.0, 3.0 };
        double[] attTarget = new double[] { 4.0, 5.0, 6.0 };
        DenseInstance diSource = new DenseInstance(attSource);
        DenseInstance diTarget = new DenseInstance(attTarget);
        Map<Integer, Double> map = new HashMap<>();
        map.put(0, 7.0);
        map.put(1, 8.0);
        diTarget.putAll(map);

        int indexSource = 0;
        int indexTarget = 0;
        while (indexSource < attSource.length || indexTarget < attTarget.length) {
            if (indexSource < attSource.length && indexTarget < attTarget.length) {
                if (indexSource == indexTarget) {
                    assertEquals(map.get(indexSource), diTarget.attributes[indexTarget], 0.0);
                } else if (indexSource < indexTarget) {
                    assertEquals(attSource[indexSource], diTarget.attributes[indexTarget], 0.0);
                    indexSource++;
                } else {
                    assertTrue(attTarget[indexTarget] > attSource[indexSource]);
                    indexTarget++;
                }
            } else if (indexSource < attSource.length) {
                assertEquals(attSource[indexSource], diTarget.attributes[indexTarget], 0.0);
                indexSource++;
            } else {
                assertTrue(attTarget[indexTarget] > attSource[indexSource]);
                indexTarget++;
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.remove(0);
    }

    @Test
    public void testRemoveAttributes() {
        double[] att = new double[] { 1.0, 2.0, 3.0, 4.0, 5.0 };
        DenseInstance di = new DenseInstance(att);
        Set<Integer> indices = new HashSet<>(Arrays.asList(1, 3));
        di.removeAttributes(indices);
        assertArrayEquals(new double[] { 1.0, 3.0, 5.0 }, di.attributes, 0.0);
    }

    @Test
    public void testSize() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertEquals(att.length, di.size());
    }

    @Deprecated
    @Test
    public void testSizeDeprecated() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertEquals(att.length, di.size());
    }

    @Test
    public void testValues() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        List<Double> values = new ArrayList<>(di.values());
        Collections.sort(values);
        assertEquals(att[0], values.get(0), 0.0);
        assertEquals(att[1], values.get(1), 0.0);
        assertEquals(att[2], values.get(2), 0.0);
    }

    @Test
    public void testNoAttributes() {
        double[] att = new double[] {};
        DenseInstance di = new DenseInstance(att);
        assertEquals(0, di.noAttributes());

        double[] attNonEmpty = new double[] { 1.0 };
        DenseInstance diNonEmpty = new DenseInstance(attNonEmpty);
        assertEquals(1, diNonEmpty.noAttributes());
    }

    @Test
    public void testToString() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        assertEquals("[1.0, 2.0, 3.0;ClassValue]", di.toString());
    }

    @Test
    public void testRemoveAttribute() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.removeAttribute(1);
        assertArrayEquals(new double[] { 1.0, 3.0 }, di.attributes, 0.0);
    }

    @Test
    public void testHashCode() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di1 = new DenseInstance(att);
        double[] att2 = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di2 = new DenseInstance(att2);
        double[] att3 = new double[] { 1.0, 3.0, 3.0 };
        DenseInstance di3 = new DenseInstance(att3);

        int hashCodeDi1 = di1.hashCode();
        int hashCodeDi2 = di2.hashCode();
        int hashCodeDi3 = di3.hashCode();

        double[] att4 = new double[] { 1.0, 2.0, 3.0, 4.0 };
        DenseInstance di4 = new DenseInstance(att4);

        int hashCodeDi4 = di4.hashCode();

        assertEquals(hashCodeDi1, hashCodeDi2);
        assertNotEquals(hashCodeDi1, hashCodeDi3);
        assertNotEquals(hashCodeDi1, hashCodeDi4);
    }

    @Test
    public void testEquals() {
        double[] att1 = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di1 = new DenseInstance(att1);
        double[] att2 = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di2 = new DenseInstance(att2);
        double[] att3 = new double[] { 1.0, 2.1, 3.0 };
        DenseInstance di3 = new DenseInstance(att3);
        double[] att4 = new double[] { 1.0, 2.0, 4.0 };
        DenseInstance di4 = new DenseInstance(att4);

        Object obj1 = new Object();
        DenseInstance diObj1 = new DenseInstance(att1);
        DenseInstance diObj2 = new DenseInstance(att1);

        assertTrue(di1.equals(di1));
        assertTrue(di1.equals(di2));
        assertFalse(di1.equals(di3));
        assertFalse(di1.equals(di4));
        assertFalse(di1.equals(obj1));
        assertTrue(di1.equals(diObj1));
        assertTrue(di1.equals(diObj2));
    }

    @Test
    public void testCopy() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        DenseInstance diCopy = di.copy();
        assertArrayEquals(att, di.attributes, 0.0);
        assertArrayEquals(att, diCopy.attributes, 0.0);
        assertArrayEquals(att, di.copy().attributes, 0.0);
    }

    @Test
    public void testInstanceCopyWithClassValue() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        Object classValue = "ClassValue";
        DenseInstance di = new DenseInstance(att, classValue);
        DenseInstance diCopy = di.copy();
        assertArrayEquals(att, di.attributes, 0.0);
        assertArrayEquals(att, diCopy.attributes, 0.0);
        assertEquals(classValue, di.classValue());
        assertEquals(classValue, diCopy.classValue());
    }

    @Test
    public void testRemoveAttributesWithEmptySet() {
        DenseInstance di = new DenseInstance();
        di.removeAttributes(Collections.emptySet());
        assertEquals(0, di.noAttributes());
    }

    @Test
    public void testRemoveAttributesWithNullSet() {
        double[] att = new double[] { 1.0, 2.0, 3.0 };
        DenseInstance di = new DenseInstance(att);
        di.removeAttributes(null);
        assertArrayEquals(att, di.attributes, 0.0);
    }
}