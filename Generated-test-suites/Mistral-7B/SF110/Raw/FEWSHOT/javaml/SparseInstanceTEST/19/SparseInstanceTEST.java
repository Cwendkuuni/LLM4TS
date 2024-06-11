package net.sf.javaml.core;

import static org.junit.Assert.*;
import java.util.*;

public class SparseInstanceTest {

    @Test
    public void testConstructor() {
        SparseInstance instance = new SparseInstance();
        assertEquals(0, instance.noAttributes());
        assertNull(instance.getClassValue());

        SparseInstance instance1 = new SparseInstance(5);
        assertEquals(5, instance1.noAttributes());
        assertNull(instance1.getClassValue());

        SparseInstance instance2 = new SparseInstance(5, 0.5);
        assertEquals(5, instance2.noAttributes());
        assertEquals(0.5, instance2.getDefaultValue(), 0.001);
        assertNull(instance2.getClassValue());

        SparseInstance instance3 = new SparseInstance(5, "Class");
        assertEquals(5, instance3.noAttributes());
        assertEquals("Class", instance3.getClassValue().toString());
        assertNull(instance3.getDefaultValue());

        SparseInstance instance4 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0});
        assertEquals(5, instance4.noAttributes());
        assertEquals(1.0, instance4.get(0), 0.001);
        assertNull(instance4.getDefaultValue());
        assertNull(instance4.getClassValue());

        SparseInstance instance5 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 0.5);
        assertEquals(5, instance5.noAttributes());
        assertEquals(0.5, instance5.getDefaultValue(), 0.001);
        assertNull(instance5.getClassValue());

        SparseInstance instance6 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, "Class");
        assertEquals(5, instance6.noAttributes());
        assertEquals("Class", instance6.getClassValue().toString());
        assertNull(instance6.getDefaultValue());

        SparseInstance instance7 = new SparseInstance(new double[]{1.0, 2.0, 3.0, 4.0, 5.0}, 0.5, "Class");
        assertEquals(5, instance7.noAttributes());
        assertEquals(0.5, instance7.getDefaultValue(), 0.001);
        assertEquals("Class", instance7.getClassValue().toString());
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(5);
        assertEquals(5, instance.noAttributes());
    }

    @Test
    public void testValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1.0, instance.value(0), 0.001);
    }

    @Test
    public void testClear() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertTrue(instance.containsKey(Integer.valueOf(0)));
        assertFalse(instance.containsKey(Integer.valueOf(1)));
    }

    @Test
    public void testContainsValue() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertTrue(instance.containsValue(Double.valueOf(1.0)));
        assertFalse(instance.containsValue(Double.valueOf(2.0)));
    }

    @Test
    public void testEntrySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        Set<Map.Entry<Integer, Double>> set = instance.entrySet();
        assertEquals(1, set.size());
        Map.Entry<Integer, Double> entry = set.iterator().next();
        assertEquals(Integer.valueOf(0), entry.getKey());
        assertEquals(Double.valueOf(1.0), entry.getValue());
    }

    @Test
    public void testGet() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(instance.getDefaultValue(), instance.get(-1));
    }

    @Test
    public void testIsEmpty() {
        SparseInstance instance = new SparseInstance();
        assertTrue(instance.isEmpty());
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        Set<Integer> set = instance.keySet();
        assertEquals(1, set.size());
        assertTrue(set.contains(Integer.valueOf(0)));
        assertFalse(set.contains(Integer.valueOf(1)));
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1.0, instance.get(0), 0.001);

        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);

        assertEquals(instance.getDefaultValue(), instance.get(-1));

        try {
            instance.put(null, Double.valueOf(2.0));
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testPutAll() {
        SparseInstance instance = new SparseInstance();
        Map<Integer, Double> map = new HashMap<>();
        map.put(Integer.valueOf(0), Double.valueOf(1.0));
        map.put(Integer.valueOf(1), Double.valueOf(2.0));

        instance.putAll(map);

        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(2.0, instance.get(1), 0.001);
    }

    @Test
    public void testRemove() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));

        Double value = instance.remove(Integer.valueOf(0));
        assertEquals(1.0, value.doubleValue(), 0.001);

        assertEquals(2.0, instance.get(0), 0.001);
        assertTrue(instance.isEmpty());

        value = instance.remove(-1);
        assertNull(value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.size();
    }

    @Test
    public void testValues() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));

        Collection<Double> values = instance.values();
        assertEquals(2, values.size());
        assertTrue(values.contains(Double.valueOf(1.0)));
        assertTrue(values.contains(Double.valueOf(2.0)));
    }

    @Test
    public void testNoAttributes() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());

        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        assertEquals(1, instance.noAttributes());

        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        assertEquals(2, instance.noAttributes());

        instance.clear();
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));

        instance.removeAttribute(1);

        assertEquals(2, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertEquals(3.0, instance.get(1), 0.001);

        instance.removeAttribute(0);
        assertEquals(1, instance.noAttributes());
        assertEquals(3.0, instance.get(0), 0.001);

        instance.removeAttribute(2);
        assertEquals(0, instance.noAttributes());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHashCode() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.hashCode();
    }

    @Test
    public void testEquals() {
        SparseInstance instance1 = new SparseInstance();
        SparseInstance instance2 = new SparseInstance();
        SparseInstance instance3 = new SparseInstance();

        instance1.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance2.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance3.put(Integer.valueOf(1), Double.valueOf(1.0));

        assertTrue(instance1.equals(instance1));
        assertTrue(instance1.equals(instance2));
        assertFalse(instance1.equals(instance3));

        instance1.clear();
        assertFalse(instance1.equals(instance2));

        instance2.clear();
        assertFalse(instance1.equals(instance2));
    }

    @Test
    public void testCopy() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));

        SparseInstance copy = instance.copy();

        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.getDefaultValue(), copy.getDefaultValue(), 0.001);

        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        for (Map.Entry<Integer, Double> entry : entrySet) {
            Double value = copy.get(entry.getKey());
            assertEquals(entry.getValue().doubleValue(), value.doubleValue(), 0.001);
        }
    }

    @Test
    public void testRemoveAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(1.0));
        instance.put(Integer.valueOf(1), Double.valueOf(2.0));
        instance.put(Integer.valueOf(2), Double.valueOf(3.0));

        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(2);

        instance.removeAttributes(indices);

        assertEquals(1, instance.noAttributes());
        assertEquals(1.0, instance.get(0), 0.001);
        assertNull(instance.get(1));
        assertNull(instance.get(2));
    }
}