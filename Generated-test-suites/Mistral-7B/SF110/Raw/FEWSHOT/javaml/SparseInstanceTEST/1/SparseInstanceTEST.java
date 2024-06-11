package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testEmptyConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
        assertNull(instance.get(0));
    }

    @Test
    public void testConstructorWithNoAttributes() {
        SparseInstance instance = new SparseInstance(0);
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructorWithNoAttributesAndDefaultValue() {
        SparseInstance instance = new SparseInstance(0, 0.0);
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructorWithNoAttributesAndClassValue() {
        SparseInstance instance = new SparseInstance(0, "StringClass");
        assertEquals("StringClass", instance.getClassValue());
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructorWithNoAttributesDefaultValueAndClassValue() {
        SparseInstance instance = new SparseInstance(0, 0.0, "StringClass");
        assertEquals("StringClass", instance.getClassValue());
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructorWithDatavector() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.get(i));
        }
    }

    @Test
    public void testConstructorWithDatavectorDefaultValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        assertEquals(datavector.length, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < datavector.length; i++) {
            if (datavector[i] != 0.0) {
                assertEquals(datavector[i], instance.get(i));
            } else {
                assertNull(instance.get(i));
            }
        }
    }

    @Test
    public void testConstructorWithDatavectorClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, "StringClass");
        assertEquals("StringClass", instance.getClassValue());
        assertEquals(datavector.length, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.get(i));
        }
    }

    @Test
    public void testConstructorWithDatavectorDefaultValueAndClassValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "StringClass");
        assertEquals("StringClass", instance.getClassValue());
        assertEquals(datavector.length, instance.noAttributes());
        assertFalse(instance.isEmpty());
        for (int i = 0; i < datavector.length; i++) {
            if (datavector[i] != 0.0) {
                assertEquals(datavector[i], instance.get(i));
            } else {
                assertNull(instance.get(i));
            }
        }
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.value(i));
        }
    }

    @Test
    public void testClear() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertTrue(instance.containsKey(Integer.valueOf(i)));
        }
        assertFalse(instance.containsKey(Integer.valueOf(4)));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertTrue(instance.containsValue(Double.valueOf(datavector[i])));
        }
        assertFalse(instance.containsValue(Double.valueOf(4.0)));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> set = instance.entrySet();
        for (int i = 0; i < datavector.length; i++) {
            Map.Entry<Integer, Double> entry = set.iterator().next();
            assertEquals(Integer.valueOf(i), entry.getKey());
            assertEquals(Double.valueOf(datavector[i]), entry.getValue());
        }
    }

    @Test
    public void testGet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(datavector[i], instance.get(Integer.valueOf(i)));
        }
        assertEquals(instance.defaultValue, instance.get(Integer.valueOf(4)));
    }

    @Test
    public void testIsEmpty() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> set = instance.keySet();
        for (int i = 0; i < datavector.length; i++) {
            assertTrue(set.contains(Integer.valueOf(i)));
        }
        assertFalse(set.contains(Integer.valueOf(4)));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(Integer.valueOf(0)));
        assertEquals(2.0, instance.get(Integer.valueOf(1)));
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.putAll(map);
        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(Integer.valueOf(0)));
        assertEquals(2.0, instance.get(Integer.valueOf(1)));
    }

    @Test
    public void testRemove() {
        double[] datavector = {1.0, 2.0, 3.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        Double value = instance.remove(Integer.valueOf(2));
        assertEquals(3.0, value.doubleValue());
        assertEquals(3, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(2)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.size();
    }

    @Test
    public void testValues() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        Collection<Double> values = instance.values();
        for (Double value : values) {
            assertTrue(datavectorContains(datavector, value.doubleValue()));
        }
    }

    @Test
    public void testNoAttributes() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(datavector.length, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        instance.removeAttribute(1);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(1)));
    }

    @Test
    public void testToString() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        String toString = instance.toString();
        String expectedString = "{1.0=1.0, 2.0=2.0, 3.0=3.0}";
        assertEquals(expectedString, toString);
    }

    @Test
    public void testHashCode() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector);
        double[] datavector2 = {1.0, 2.0, 3.0};
        SparseInstance instance2 = new SparseInstance(datavector2);
        SparseInstance instance3 = new SparseInstance();
        SparseInstance instance4 = new SparseInstance(datavector);
        assertEquals(instance1.hashCode(), instance1.hashCode());
        assertEquals(instance1.hashCode(), instance4.hashCode());
        assertNotEquals(instance1.hashCode(), instance2.hashCode());
        assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }

    @Test
    public void testEquals() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance1 = new SparseInstance(datavector);
        double[] datavector2 = {1.0, 2.0, 3.0};
        SparseInstance instance2 = new SparseInstance(datavector2);
        SparseInstance instance3 = new SparseInstance();
        SparseInstance instance4 = new SparseInstance(datavector);
        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance4));
        assertFalse(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));
    }

    @Test
    public void testCopy() {
        double[] datavector = {1.0, 2.0, 3.0};
        SparseInstance instance = new SparseInstance(datavector);
        SparseInstance copiedInstance = instance.copy();
        assertEquals(instance.noAttributes(), copiedInstance.noAttributes());
        for (int i = 0; i < datavector.length; i++) {
            assertEquals(instance.get(Integer.valueOf(i)), copiedInstance.get(Integer.valueOf(i)));
        }
        assertEquals(instance.getClassValue(), copiedInstance.getClassValue());
    }

    @Test
    public void testRemoveAttributes() {
        double[] datavector = {1.0, 2.0, 3.0, 4.0};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);
        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(Integer.valueOf(1)));
        assertFalse(instance.containsKey(Integer.valueOf(3)));
    }

    private boolean datavectorContains(double[] datavector, double value) {
        for (double d : datavector) {
            if (d == value) {
                return true;
            }
        }
        return false;
    }
}