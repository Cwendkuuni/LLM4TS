package net.sf.javaml.core;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class SparseInstanceTest {

    @Test
    public void testConstructor_NoArguments() {
        SparseInstance instance = new SparseInstance();
        assertEquals(-1, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructor_OneArgument() {
        SparseInstance instance = new SparseInstance(1);
        assertEquals(1, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructor_TwoArguments() {
        SparseInstance instance = new SparseInstance(1, 0.0);
        assertEquals(1, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructor_ThreeArguments() {
        SparseInstance instance = new SparseInstance(1, 0.0, "ClassValue");
        assertEquals(1, instance.noAttributes());
        assertEquals("ClassValue", instance.getClassValue());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testConstructor_DoubleArray() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testConstructor_DoubleArrayAndDefaultValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector, 0.0);
        assertEquals(3, instance.noAttributes());
        assertNull(instance.getClassValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testConstructor_DoubleArrayAndClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector, "ClassValue");
        assertEquals(3, instance.noAttributes());
        assertEquals("ClassValue", instance.getClassValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testConstructor_DoubleArrayAndDefaultValueAndClassValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector, 0.0, "ClassValue");
        assertEquals(3, instance.noAttributes());
        assertEquals("ClassValue", instance.getClassValue());
        assertFalse(instance.isEmpty());
    }

    @Test
    public void testSetNoAttributes() {
        SparseInstance instance = new SparseInstance();
        instance.setNoAttributes(3);
        assertEquals(3, instance.noAttributes());
    }

    @Test
    public void testValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(0.1, instance.value(0), 0.001);
        assertEquals(0.2, instance.value(1), 0.001);
        assertEquals(0.3, instance.value(2), 0.001);
    }

    @Test
    public void testClear() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        instance.clear();
        assertEquals(0, instance.noAttributes());
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testContainsKey() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsKey(0));
        assertTrue(instance.containsKey(1));
        assertTrue(instance.containsKey(2));
        assertFalse(instance.containsKey(3));
    }

    @Test
    public void testContainsValue() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertTrue(instance.containsValue(0.1));
        assertTrue(instance.containsValue(0.2));
        assertTrue(instance.containsValue(0.3));
        assertFalse(instance.containsValue(0.4));
    }

    @Test
    public void testEntrySet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Map.Entry<Integer, Double>> entrySet = instance.entrySet();
        assertEquals(3, entrySet.size());
        for (Map.Entry<Integer, Double> entry : entrySet) {
            int key = entry.getKey();
            double value = entry.getValue();
            assertEquals(key, instance.keyAt(key));
            assertEquals(value, instance.value(key), 0.001);
        }
    }

    @Test
    public void testGet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.2, instance.get(1), 0.001);
        assertEquals(0.3, instance.get(2), 0.001);
        assertEquals(0.0, instance.get(3), 0.001);
    }

    @Test
    public void testIsEmpty() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertFalse(instance.isEmpty());

        instance.clear();
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testKeySet() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Set<Integer> keySet = instance.keySet();
        assertEquals(3, keySet.size());
        for (int key : keySet) {
            assertEquals(key, instance.keyAt(key));
        }
    }

    @Test
    public void testPut() {
        SparseInstance instance = new SparseInstance();
        instance.put(Integer.valueOf(0), Double.valueOf(0.1));
        instance.put(Integer.valueOf(1), Double.valueOf(0.2));
        instance.put(Integer.valueOf(2), Double.valueOf(0.3));

        assertEquals(3, instance.noAttributes());
        assertFalse(instance.isEmpty());

        assertEquals(0.1, instance.get(0), 0.001);
        assertEquals(0.2, instance.get(1), 0.001);
        assertEquals(0.3, instance.get(2), 0.001);
    }

    @Test
    public void testPutAll() {
        double[] datavector1 = {0.1, 0.2, 0.3};
        double[] datavector2 = {0.4, 0.5, 0.6};

        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance();

        Map<Integer, Double> map = new HashMap<>();
        map.putAll(instance1.getDataMap());
        instance2.putAll(map);

        assertEquals(3, instance2.noAttributes());
        assertFalse(instance2.isEmpty());

        for (int i = 0; i < 3; i++) {
            assertEquals(datavector1[i], instance2.value(i), 0.001);
        }
    }

    @Test
    public void testRemove() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);

        Double removedValue = instance.remove(0);
        assertEquals(0.1, removedValue.doubleValue(), 0.001);
        assertEquals(2, instance.noAttributes());

        removedValue = instance.remove(1);
        assertEquals(0.2, removedValue.doubleValue(), 0.001);
        assertEquals(1, instance.noAttributes());

        removedValue = instance.remove(2);
        assertEquals(0.3, removedValue.doubleValue(), 0.001);
        assertEquals(0, instance.noAttributes());

        removedValue = instance.remove(3);
        assertNull(removedValue);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSize() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        instance.size();
    }

    @Test
    public void testValues() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        Collection<Double> values = instance.values();
        assertEquals(3, values.size());
        for (Double value : values) {
            assertEquals(value, instance.get(Collections.indexOfIterable(instance.keySet(), value.intValue())), 0.001);
        }
    }

    @Test
    public void testNoAttributes() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        assertEquals(3, instance.noAttributes());

        instance.clear();
        assertEquals(0, instance.noAttributes());

        instance.removeAttribute(0);
        assertEquals(2, instance.noAttributes());

        instance.removeAttribute(1);
        assertEquals(1, instance.noAttributes());

        instance.removeAttribute(2);
        assertEquals(0, instance.noAttributes());
    }

    @Test
    public void testRemoveAttribute() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);

        List<Integer> indices = new Vector<>();
        indices.add(0);
        indices.add(1);
        instance.removeAttributes(indices);

        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(0));
        assertFalse(instance.containsKey(1));

        indices.clear();
        indices.add(2);
        instance.removeAttributes(indices);

        assertEquals(0, instance.noAttributes());
        assertFalse(instance.containsKey(2));
    }

    @Test
    public void testToString() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        String expected = "{0.1=0.1, 0.2=0.2, 0.3=0.3;}";
        assertEquals(expected, instance.toString());
    }

    @Test
    public void testHashCode() {
        double[] datavector1 = {0.1, 0.2, 0.3};
        double[] datavector2 = {0.1, 0.2, 0.3};
        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance(datavector2);

        int hashCode1 = instance1.hashCode();
        int hashCode2 = instance2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        double[] datavector1 = {0.1, 0.2, 0.3};
        double[] datavector2 = {0.1, 0.2, 0.3};
        SparseInstance instance1 = new SparseInstance(datavector1);
        SparseInstance instance2 = new SparseInstance(datavector2);

        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
    }

    @Test
    public void testCopy() {
        double[] datavector = {0.1, 0.2, 0.3};
        SparseInstance instance = new SparseInstance(datavector);
        SparseInstance copy = instance.copy();

        assertEquals(instance.noAttributes(), copy.noAttributes());
        assertEquals(instance.getClassValue(), copy.getClassValue());

        for (int i = 0; i < instance.noAttributes(); i++) {
            assertEquals(instance.value(i), copy.value(i), 0.001);
        }
    }

    @Test
    public void testRemoveAttributes_EmptySet() {
        SparseInstance instance = new SparseInstance();
        Set<Integer> indices = new HashSet<>();
        instance.removeAttributes(indices);
        assertTrue(instance.isEmpty());
    }

    @Test
    public void testRemoveAttributes_NonEmptySet() {
        double[] datavector = {0.1, 0.2, 0.3, 0.4};
        SparseInstance instance = new SparseInstance(datavector);

        Set<Integer> indices = new HashSet<>();
        indices.add(1);
        indices.add(3);
        instance.removeAttributes(indices);

        assertEquals(2, instance.noAttributes());
        assertFalse(instance.containsKey(1));
        assertFalse(instance.containsKey(3));

        indices.clear();
        indices.add(2);
        instance.removeAttributes(indices);

        assertEquals(1, instance.noAttributes());
        assertFalse(instance.containsKey(2));
    }
}